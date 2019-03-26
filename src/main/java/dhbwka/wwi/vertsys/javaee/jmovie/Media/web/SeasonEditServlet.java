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
import dhbwka.wwi.vertsys.javaee.jmovie.Media.ejb.SeasonBean;
import dhbwka.wwi.vertsys.javaee.jmovie.Media.ejb.SerieBean;
import dhbwka.wwi.vertsys.javaee.jmovie.Media.jpa.Episode;
import dhbwka.wwi.vertsys.javaee.jmovie.Media.jpa.Genre;
import dhbwka.wwi.vertsys.javaee.jmovie.Media.jpa.Media;
import dhbwka.wwi.vertsys.javaee.jmovie.Media.jpa.Season;
import dhbwka.wwi.vertsys.javaee.jmovie.Media.jpa.Serie;
import dhbwka.wwi.vertsys.javaee.jmovie.Media.jpa.WatchStatus;
import dhbwka.wwi.vertsys.javaee.jmovie.common.web.WebUtils;
import dhbwka.wwi.vertsys.javaee.jmovie.common.web.FormValues;
import dhbwka.wwi.vertsys.javaee.jmovie.common.ejb.UserBean;
import dhbwka.wwi.vertsys.javaee.jmovie.common.ejb.ValidationBean;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Seite zum Anlegen oder Bearbeiten einer Aufgabe.
 */
@WebServlet(urlPatterns = "/app/medias/season/*")
public class SeasonEditServlet extends MediaEditServlet {

    @EJB
    SeasonBean seasonBean;

    @EJB
    SerieBean serieBean;
    
    @EJB
    ValidationBean validationBean;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Season season = this.getRequestedSeason(request);
        request.setAttribute("statuses", WatchStatus.values());
        
        if(season.getEpisodes()!=null){
            Collections.sort(season.getEpisodes(), new Comparator<Episode>() {
            public int compare(Episode e1, Episode e2) {
                    return e1.getNr()-e2.getNr();
                }
            });
            request.setAttribute("episodes", season.getEpisodes());
        }

        // Zu bearbeitende Serie einlesen
        HttpSession session = request.getSession();

        
        request.setAttribute("edit", season.getId() != 0);
                                
        if (session.getAttribute("season_form") == null) {
            // Keine Formulardaten mit fehlerhaften Daten in der Session,
            // daher Formulardaten aus dem Datenbankobjekt übernehmen
            request.setAttribute("season_form", createSeasonForm(season));
        }
        
        
        // Anfrage an die JSP weiterleiten
        request.getRequestDispatcher("/WEB-INF/medias/season_edit.jsp").forward(request, response);
        
        request.getSession().removeAttribute("season_form");
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
                this.saveSeason(request, response);
                break;
            case "delete":
                this.deleteSeason(request, response);
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
    private void saveSeason(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Formulareingaben prüfen
        List<String> errors = new ArrayList<>();
        
        Season season = this.getRequestedSeason(request);
        
        //saveMedia(request, season, errors);
        //public void saveMedia(HttpServletRequest request, Media media, List<String> errors){
        String seasonNr = request.getParameter("season_nr");
        String seasonReleaseDate = request.getParameter("season_release_date");
        String seasonStatus = request.getParameter("season_status");

        

        if (seasonNr != null && !seasonNr.trim().isEmpty()) {
            season.setNr(Integer.valueOf(seasonNr));
        }else {
            errors.add("Es muss eine Nummer vergeben werden");
        }
        
        try{
            Date releaseDate= Date.valueOf(seasonReleaseDate);        
            season.setReleaseDate(releaseDate);
        } catch (IllegalArgumentException ex){
            //Kein Date
        }

        try {
            season.setStatus(WatchStatus.valueOf(seasonStatus));
        } catch (IllegalArgumentException ex) {
            errors.add("Der ausgewählte Status ist nicht vorhanden.");
        }
        this.validationBean.validate(season, errors);
    
        // Datensatz speichern
        if (errors.isEmpty()) {
            try{
                this.seasonBean.update(season);
            }catch (EJBTransactionRolledbackException ex){
                errors.add("Die Staffel Nr  ist bereits vergeben");
            }
        }

        // Weiter zur nächsten Seite
        if (errors.isEmpty()) {
            // Keine Fehler: Startseite aufrufen
            response.sendRedirect(WebUtils.appUrl(request, "/app/medias/serie/"+season.getSerie().getId()));
        } else {
            // Fehler: Formuler erneut anzeigen
            FormValues formValues = new FormValues();
            formValues.setValues(request.getParameterMap());
            formValues.setErrors(errors);

            HttpSession session = request.getSession();
            session.setAttribute("season_form", formValues);

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
    private void deleteSeason(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Datensatz löschen
        Season season = this.getRequestedSeason(request);
        this.seasonBean.delete(season);

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
    private Season getRequestedSeason(HttpServletRequest request) {
        // Zunächst davon ausgehen, dass ein neuer Satz angelegt werden soll
        Season season = new Season();
        season.setReleaseDate(new Date(System.currentTimeMillis()));

        // ID aus der URL herausschneiden
        String seasonId = request.getPathInfo();

        if (seasonId == null) {
            seasonId = "";
        }

        seasonId = seasonId.substring(1);

        if (seasonId.endsWith("/")) {
            seasonId = seasonId.substring(0, seasonId.length() - 1);
        }

        // Versuchen, den Datensatz mit der übergebenen ID zu finden
        try {
            season = this.seasonBean.findById(Long.parseLong(seasonId));
        } catch (NumberFormatException ex) {
            // Ungültige oder keine ID in der URL enthalten
            try{ //Auf Serie zur neuen Staffel Prüfen
                if(seasonId.startsWith("new")){
                season.setSerie(this.serieBean.findById(Long.parseLong(seasonId.substring(3))));
            }
            } catch(NumberFormatException exs){
                
            }
            
            
        }

        return season;
    }


    private FormValues createSeasonForm(Season season) {
        Map<String, String[]> values = new HashMap<>();

        values.put("season_id", new String[]{
            ""+season.getId()
        });   
        
        values.put("season_serie", new String[]{
            season.getSerie().getTitle()
        });

        
        values.put("season_nr", new String[]{
            "" + season.getNr()
        });
        

        values.put("season_release_date", new String[]{
            season.getReleaseDate().toString()
        });


        values.put("season_status", new String[]{
            season.getStatus().toString()
        });

       /* values.put("media_title", new String[]{
            season.getTitle()
        });

        values.put("media_description", new String[]{
            season.getDescription()
        });
*/

        FormValues formValues = new FormValues();
        formValues.setValues(values);
        return formValues;
    }

    

}
