/*
 * Copyright © 2018 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbwka.wwi.vertsys.javaee.jmovie.media.web;

import dhbwka.wwi.vertsys.javaee.jmovie.media.ejb.GenreBean;
import dhbwka.wwi.vertsys.javaee.jmovie.media.ejb.SerieBean;
import dhbwka.wwi.vertsys.javaee.jmovie.media.jpa.Season;
import dhbwka.wwi.vertsys.javaee.jmovie.media.jpa.Serie;
import dhbwka.wwi.vertsys.javaee.jmovie.common.web.WebUtils;
import dhbwka.wwi.vertsys.javaee.jmovie.common.web.FormValues;
import dhbwka.wwi.vertsys.javaee.jmovie.common.ejb.UserBean;
import dhbwka.wwi.vertsys.javaee.jmovie.common.ejb.ValidationBean;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Seite zum Anlegen oder Bearbeiten einer Aufgabe.
 */
@WebServlet(urlPatterns = "/app/medias/serie/*")
public class SerieEditServlet extends MediaEditServlet {

    @EJB
    SerieBean serieBean;

    @EJB
    GenreBean genreBean;

    @EJB
    UserBean userBean;

    @EJB
    ValidationBean validationBean;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Serie serie = this.getRequestedSerie(request);
        super.doGet(request, serie);
                
        if(serie.getSeasons()!=null){
            Collections.sort(serie.getSeasons(), new Comparator<Season>() {
            public int compare(Season e1, Season e2) {
                    return e1.getNr()-e2.getNr();
                }
            });
            request.setAttribute("seasons", serie.getSeasons());
        }
        
        
        // Anfrage an die JSP weiterleiten
        request.getRequestDispatcher("/WEB-INF/medias/serie_edit.jsp").forward(request, response);
        
        request.getSession().removeAttribute("media_form");
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
            case "save":
                this.saveSerie(request, response);
                break;
            case "delete":
                this.deleteSerie(request, response);
                break;
        }
    }

    /**
     * Aufgerufen in doPost(): Neue oder vorhandene Aufgabe speichern
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void saveSerie(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Formulareingaben prüfen
        List<String> errors = new ArrayList<>();
        
        Serie serie = this.getRequestedSerie(request);
        
        saveMedia(request, serie, errors);
        
        // Datensatz speichern
        if (errors.isEmpty()) {
            this.serieBean.update(serie);
        }

        // Weiter zur nächsten Seite
        if (errors.isEmpty()) {
            // Keine Fehler: Startseite aufrufen
            response.sendRedirect(WebUtils.appUrl(request, "/app/medias/list/"));
        } else {
            // Fehler: Formuler erneut anzeigen
            FormValues formValues = new FormValues();
            formValues.setValues(request.getParameterMap());
            formValues.setErrors(errors);

            HttpSession session = request.getSession();
            session.setAttribute("media_form", formValues);

            response.sendRedirect(request.getRequestURI());
        }
    }

    /**
     * Aufgerufen in doPost: Vorhandene Aufgabe löschen
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void deleteSerie(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Datensatz löschen
        Serie serie = this.getRequestedSerie(request);
        this.serieBean.delete(serie);

        // Zurück zur Übersicht
        response.sendRedirect(WebUtils.appUrl(request, "/app/medias/list/"));
    }

    /**
     * Zu bearbeitende Aufgabe aus der URL ermitteln und zurückgeben. Gibt
     * entweder einen vorhandenen Datensatz oder ein neues, leeres Objekt
     * zurück.
     *
     * @param request HTTP-Anfrage
     * @return Zu bearbeitende Aufgabe
     */
    private Serie getRequestedSerie(HttpServletRequest request) {
        // Zunächst davon ausgehen, dass ein neuer Satz angelegt werden soll
        Serie serie = new Serie();
        serie.setOwner(this.userBean.getCurrentUser());
        serie.setReleaseDate(new Date(System.currentTimeMillis()));

        // ID aus der URL herausschneiden
        String mediaId = request.getPathInfo();

        if (mediaId == null) {
            mediaId = "";
        }

        mediaId = mediaId.substring(1);

        if (mediaId.endsWith("/")) {
            mediaId = mediaId.substring(0, mediaId.length() - 1);
        }

        // Versuchen, den Datensatz mit der übergebenen ID zu finden
        try {
            serie = this.serieBean.findById(Long.parseLong(mediaId));
        } catch (NumberFormatException ex) {
            // Ungültige oder keine ID in der URL enthalten
        }

        return serie;
    }

    

}
