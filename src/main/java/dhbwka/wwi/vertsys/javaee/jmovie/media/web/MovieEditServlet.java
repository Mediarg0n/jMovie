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
import dhbwka.wwi.vertsys.javaee.jmovie.media.ejb.MovieBean;
import dhbwka.wwi.vertsys.javaee.jmovie.media.jpa.Media;
import dhbwka.wwi.vertsys.javaee.jmovie.media.jpa.Movie;
import dhbwka.wwi.vertsys.javaee.jmovie.media.jpa.WatchStatus;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Seite zum Anlegen oder Bearbeiten eines Films.
 */
@WebServlet(urlPatterns = "/app/medias/movie/*")
public class MovieEditServlet extends MediaEditServlet {

    @EJB
    MovieBean movieBean;

    @EJB
    GenreBean genreBean;

    @EJB
    UserBean userBean;

    @EJB
    ValidationBean validationBean;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Movie movie = this.getRequestedMovie(request);
        
        
        // Verfügbare Kategorien und Stati für die Suchfelder ermitteln
        request.setAttribute("genres", this.genreBean.findAllSorted());
        request.setAttribute("statuses", WatchStatus.values());

        // Zu bearbeitende Serie einlesen
        HttpSession session = request.getSession();

        
        request.setAttribute("edit", movie.getId() != 0);
                      
        
        if (session.getAttribute("media_form") == null) {
            // Keine Formulardaten mit fehlerhaften Daten in der Session,
            // daher Formulardaten aus dem Datenbankobjekt übernehmen
            request.setAttribute("media_form", this.createMediaForm(movie));
        }

        
        
        // Anfrage an die JSP weiterleiten
        request.getRequestDispatcher("/WEB-INF/medias/movie_edit.jsp").forward(request, response);
        
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
                this.saveMovie(request, response);
                break;
            case "delete":
                this.deleteMovie(request, response);
                break;
        }
    }

    /**
     * Aufgerufen in doPost(): Neuen oder vorhanden Film speichern
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void saveMovie(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Formulareingaben prüfen
        List<String> errors = new ArrayList<>();
        
        Movie movie = this.getRequestedMovie(request);
        
        
        String movieLength = request.getParameter("movie_length");
        
        if (movieLength != null && !movieLength.trim().isEmpty()) {
         
            try{
            movie.setMovieLength(Integer.valueOf(movieLength));
            }
            catch(NumberFormatException nfe){
                errors.add("Die Dauer muss eine ganze Zahl sein.");       
            }
            
            if(Integer.valueOf(movieLength) < 0) errors.add("Die Dauer muss positiv sein.");
            
            
        }else {
            errors.add("Es muss eine Dauer angegeben werden.");
        }
        
        super.saveMedia(request, movie, errors);
        
        // Datensatz speichern
        if (errors.isEmpty()) {
            this.movieBean.update(movie);
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
     * Aufgerufen in doPost: Vorhandenen Film löschen
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void deleteMovie(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Datensatz löschen
        Movie movie = this.getRequestedMovie(request);
        this.movieBean.delete(movie);

        // Zurück zur Übersicht
        response.sendRedirect(WebUtils.appUrl(request, "/app/medias/list/"));
    }

    /**
     * Zu bearbeitender Film aus der URL ermitteln und zurückgeben. Gibt
     * entweder einen vorhandenen Datensatz oder ein neues, leeres Objekt
     * zurück.
     *
     * @param request HTTP-Anfrage
     * @return Zu bearbeitender Film
     */
    private Movie getRequestedMovie(HttpServletRequest request) {
        // Zunächst davon ausgehen, dass ein neuer Satz angelegt werden soll
        Movie movie = new Movie();
        movie.setOwner(this.userBean.getCurrentUser());
        movie.setReleaseDate(new Date(System.currentTimeMillis()));
        movie.setMovieLength(0);

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
            movie = this.movieBean.findById(Long.parseLong(mediaId));
        } catch (NumberFormatException ex) {
            // Ungültige oder keine ID in der URL enthalten
        }

        return movie;
    }
    
    
    /**
     * Neues FormValues-Objekt erzeugen und mit den Daten eines aus der
     * Datenbank eingelesenen Datensatzes füllen.Dadurch müssen in der JSP
 keine hässlichen Fallunterscheidungen gemacht werden, ob die Werte im
 Formular aus der Entity oder aus einer vorherigen Formulareingabe
 stammen.
     *
     * @param media Die zu bearbeitende Medium
     * media Neues, gefülltes FormValues-Objekt
     * @return FormValues
     */
    @Override
    public FormValues createMediaForm(Media media) {
        
        
        Map<String, String[]> values = new HashMap<>();

        values.put("movie_length", new String[]{
            String.valueOf(((Movie) media).getMovieLength())
        });
        

        FormValues formValues = super.createMediaForm(media);
        formValues.getValues().putAll(values);
        return formValues;
    }
}
    









    

