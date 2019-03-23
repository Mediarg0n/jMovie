/*
 * Copyright © 2018 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbwka.wwi.vertsys.javaee.jmovie.Media.web;

import dhbwka.wwi.vertsys.javaee.jmovie.Media.ejb.GenreBean;
import dhbwka.wwi.vertsys.javaee.jmovie.Media.ejb.SerieBean;
import dhbwka.wwi.vertsys.javaee.jmovie.Media.jpa.Genre;
import dhbwka.wwi.vertsys.javaee.jmovie.Media.jpa.Media;
import dhbwka.wwi.vertsys.javaee.jmovie.Media.jpa.Serie;
import dhbwka.wwi.vertsys.javaee.jmovie.Media.jpa.WatchStatus;
import dhbwka.wwi.vertsys.javaee.jmovie.common.web.WebUtils;
import dhbwka.wwi.vertsys.javaee.jmovie.common.web.FormValues;
import dhbwka.wwi.vertsys.javaee.jmovie.common.ejb.UserBean;
import dhbwka.wwi.vertsys.javaee.jmovie.common.ejb.ValidationBean;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Seite zum Anlegen oder Bearbeiten einer Aufgabe.
 */
@WebServlet(urlPatterns = "/app/medias/serie/*")
public class SerieEditServlet extends HttpServlet {

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

        // Verfügbare Kategorien und Stati für die Suchfelder ermitteln
        request.setAttribute("genres", this.genreBean.findAllSorted());
        request.setAttribute("statuses", WatchStatus.values());

        // Zu bearbeitende Serie einlesen
        HttpSession session = request.getSession();

        Serie serie = this.getRequestedSerie(request);
        request.setAttribute("edit", serie.getId() != 0);
                                
        if (session.getAttribute("media_form") == null) {
            // Keine Formulardaten mit fehlerhaften Daten in der Session,
            // daher Formulardaten aus dem Datenbankobjekt übernehmen
            request.setAttribute("media_form", this.createMediaForm(serie));
        }

        // Anfrage an die JSP weiterleiten
        request.getRequestDispatcher("/WEB-INF/medias/serie_edit.jsp").forward(request, response);
        
        session.removeAttribute("media_form");
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

        String mediaGenre = request.getParameter("media_genre");
        String mediaReleaseDate = request.getParameter("media_release_date");
        String mediaStatus = request.getParameter("media_status");
        String mediaTitle = request.getParameter("media_title");
        String mediaDescription = request.getParameter("media_description");

        Serie serie = this.getRequestedSerie(request);

        if (mediaGenre != null && !mediaGenre.trim().isEmpty()) {
            try {
                List<Genre> genres = new ArrayList<Genre>();
                genres.add(this.genreBean.findById(Long.parseLong(mediaGenre)));
                serie.setGenre(genres);
            } catch (NumberFormatException ex) {
                // Ungültige oder keine ID mitgegeben
            }
        }

        Date releaseDate = WebUtils.parseDate(mediaReleaseDate);

        if (releaseDate != null) {
            serie.setReleaseDate(releaseDate);
        } else {
            errors.add("Das Datum muss dem Format dd.mm.yyyy entsprechen.");
        }

        try {
            serie.setStatus(WatchStatus.valueOf(mediaStatus));
        } catch (IllegalArgumentException ex) {
            errors.add("Der ausgewählte Status ist nicht vorhanden.");
        }

        serie.setTitle(mediaTitle);
        serie.setDescription(mediaDescription);

        this.validationBean.validate(serie, errors);

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

    /**
     * Neues FormValues-Objekt erzeugen und mit den Daten eines aus der
     * Datenbank eingelesenen Datensatzes füllen. Dadurch müssen in der JSP
     * keine hässlichen Fallunterscheidungen gemacht werden, ob die Werte im
     * Formular aus der Entity oder aus einer vorherigen Formulareingabe
     * stammen.
     *
     * @param task Die zu bearbeitende Aufgabe
     * media Neues, gefülltes FormValues-Objekt
     */
    private FormValues createMediaForm(Media media) {
        Map<String, String[]> values = new HashMap<>();

        values.put("media_owner", new String[]{
            media.getOwner().getUsername()
        });

        if (media.getGenre()!= null) {
            values.put("media_category", new String[]{
                "" + media.getGenre().get(0).getId()
            });
        }

        values.put("media_release_date", new String[]{
            WebUtils.formatDate(media.getReleaseDate())
        });


        values.put("media_status", new String[]{
            media.getStatus().toString()
        });

        values.put("media_title", new String[]{
            media.getTitle()
        });

        values.put("media_description", new String[]{
            media.getDescription()
        });

        FormValues formValues = new FormValues();
        formValues.setValues(values);
        return formValues;
    }

}
