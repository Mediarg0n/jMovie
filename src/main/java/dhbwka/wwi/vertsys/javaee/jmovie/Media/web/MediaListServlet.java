/*
 * Copyright © 2019 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbwka.wwi.vertsys.javaee.jmovie.Media.web;

import dhbwka.wwi.vertsys.javaee.jmovie.Media.ejb.GenreBean;
import dhbwka.wwi.vertsys.javaee.jmovie.Media.ejb.MediaBean;
import dhbwka.wwi.vertsys.javaee.jmovie.Media.jpa.Genre;
import dhbwka.wwi.vertsys.javaee.jmovie.Media.jpa.Media;
import dhbwka.wwi.vertsys.javaee.jmovie.common.ejb.ValidationBean;
import dhbwka.wwi.vertsys.javaee.jmovie.common.web.FormValues;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author D070429
 */
/**
 * Seite zum Anzeigen und Bearbeiten der Kategorien. Die Seite besitzt ein
 * Formular, mit dem ein neue Kategorie angelegt werden kann, sowie eine Liste,
 * die zum Löschen der Kategorien verwendet werden kann.
 */
@WebServlet(urlPatterns = {"/app/medias/genres/"})
public class MediaListServlet extends HttpServlet {

    @EJB
    GenreBean genreBean;

    @EJB
    MediaBean mediaBean;

    @EJB
    ValidationBean validationBean;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Alle vorhandenen Kategorien ermitteln
        request.setAttribute("genres", this.genreBean.findAllSorted());

        // Anfrage an dazugerhörige JSP weiterleiten
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/medias/genre_list.jsp");
        dispatcher.forward(request, response);

        // Alte Formulardaten aus der Session entfernen
        HttpSession session = request.getSession();
        session.removeAttribute("genres_form");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Angeforderte Aktion ausführen        
        String action = request.getParameter("action");

        if (action == null) {
            action = "";
        }

        switch (action) {
            case "create":
                this.createGenre(request, response);
                break;
            case "delete":
                this.deleteGenres(request, response);
                break;
        }
    }

    /**
     * Aufgerufen in doPost(): Neues Genre anlegen
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void createGenre(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Formulareingaben prüfen
        String name = request.getParameter("name");

        Genre genre = new Genre(name);
        List<String> errors = this.validationBean.validate(genre);

        // Neue Kategorie anlegen
        if (errors.isEmpty()) {
            this.genreBean.saveNew(genre);
        }

        // Browser auffordern, die Seite neuzuladen
        if (!errors.isEmpty()) {
            FormValues formValues = new FormValues();
            formValues.setValues(request.getParameterMap());
            formValues.setErrors(errors);

            HttpSession session = request.getSession();
            session.setAttribute("genres_form", formValues);
        }

        response.sendRedirect(request.getRequestURI());
    }

    /**
     * Aufgerufen in doPost(): Markierte Genres löschen
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void deleteGenres(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Markierte Kategorie IDs auslesen
        String[] categoryIds = request.getParameterValues("genre");

        if (categoryIds == null) {
            categoryIds = new String[0];
        }

        // Kategorien löschen
        for (String categoryId : categoryIds) {
            // Zu löschende Kategorie ermitteln
            Genre genre;

            try {
                genre = this.genreBean.findById(Long.parseLong(categoryId));
            } catch (NumberFormatException ex) {
                continue;
            }

            if (genre == null) {
                continue;
            }

            // Bei allen betroffenen Aufgaben, den Bezug zum Genre aufheben
            List<Media> medias = genre.getMedias();

            if (medias != null) {
                medias.forEach((Media media) -> {
                    media.getGenres().remove(genre);
                    this.mediaBean.update(media);
                });
            }

            // Und weg damit
            this.genreBean.delete(genre);
        }

        // Browser auffordern, die Seite neuzuladen
        response.sendRedirect(request.getRequestURI());
    }

}
