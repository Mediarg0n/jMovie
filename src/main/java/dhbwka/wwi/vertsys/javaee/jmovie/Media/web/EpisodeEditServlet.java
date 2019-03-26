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

import dhbwka.wwi.vertsys.javaee.jmovie.Media.ejb.EpisodeBean;
import dhbwka.wwi.vertsys.javaee.jmovie.Media.ejb.SeasonBean;
import dhbwka.wwi.vertsys.javaee.jmovie.Media.ejb.SerieBean;
import dhbwka.wwi.vertsys.javaee.jmovie.Media.jpa.Episode;
import dhbwka.wwi.vertsys.javaee.jmovie.Media.jpa.Season;
import dhbwka.wwi.vertsys.javaee.jmovie.Media.jpa.Serie;
import dhbwka.wwi.vertsys.javaee.jmovie.Media.jpa.WatchStatus;
import dhbwka.wwi.vertsys.javaee.jmovie.common.ejb.ValidationBean;
import dhbwka.wwi.vertsys.javaee.jmovie.common.web.FormValues;
import dhbwka.wwi.vertsys.javaee.jmovie.common.web.WebUtils;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author bpall
 */
@WebServlet(urlPatterns = "/app/medias/episode/*")
public class EpisodeEditServlet extends MediaEditServlet {

    @EJB
    EpisodeBean episodeBean;

    @EJB
    SeasonBean seasonBean;
    
    @EJB
    ValidationBean validationBean;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Episode episode = this.getRequestedEpisode(request);
        request.setAttribute("statuses", WatchStatus.values());

        // Zu bearbeitende Serie einlesen
        HttpSession session = request.getSession();

        
        request.setAttribute("edit", episode.getId() != 0);
                                
        if (session.getAttribute("episode_form") == null) {
            // Keine Formulardaten mit fehlerhaften Daten in der Session,
            // daher Formulardaten aus dem Datenbankobjekt übernehmen
            request.setAttribute("episode_form", createEpisodeForm(episode));
        }
        
        
        // Anfrage an die JSP weiterleiten
        request.getRequestDispatcher("/WEB-INF/medias/episode_edit.jsp").forward(request, response);
        
        request.getSession().removeAttribute("episode_form");
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
                this.saveEpisode(request, response);
                break;
            case "delete":
                this.deleteEpisode(request, response);
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
    private void saveEpisode(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Formulareingaben prüfen
        List<String> errors = new ArrayList<>();
        
        Episode episode = this.getRequestedEpisode(request);
        
        String episodeNr = request.getParameter("episode_nr");
        String episodeTitle = request.getParameter("episode_title");
        String episodeLength = request.getParameter("episode_length");
        String episodeReleaseDate = request.getParameter("episode_release_date");
        String episodeStatus = request.getParameter("episode_status");
        String episodeDescription = request.getParameter("episode_description");

        

        if (episodeNr != null && !episodeNr.trim().isEmpty()) {
            episode.setNr(Integer.valueOf(episodeNr));
        }else {
            errors.add("Es muss eine Nummer vergeben werden");
        }
        
        if (episodeTitle != null && !episodeNr.trim().isEmpty()) {
            episode.setTitle(episodeTitle);
        }else {
            errors.add("Es muss ein Titel vergeben werden");
        }
        
        if (episodeLength != null && !episodeLength.trim().isEmpty()) {
            episode.setLength(Integer.valueOf(episodeLength));
        }
        
        try{
            Date releaseDate= Date.valueOf(episodeReleaseDate);        
            episode.setReleaseDate(releaseDate);
        } catch (IllegalArgumentException ex){
            //Kein Date
        }

        try {
            episode.setStatus(WatchStatus.valueOf(episodeStatus));
        } catch (IllegalArgumentException ex) {
            errors.add("Der ausgewählte Status ist nicht vorhanden.");
        }
        this.validationBean.validate(episode, errors);
        
        if (episodeDescription != null && !episodeDescription.trim().isEmpty()) {
            episode.setDescription(episodeDescription);
        }
    
        // Datensatz speichern
        if (errors.isEmpty()) {
            try{
                this.episodeBean.update(episode);
            }catch (EJBTransactionRolledbackException ex){
                errors.add("Die Episoden Nr  ist bereits vergeben");
            }
        }

        // Weiter zur nächsten Seite
        if (errors.isEmpty()) {
            // Keine Fehler: Startseite aufrufen
            response.sendRedirect(WebUtils.appUrl(request, "/app/medias/season/"+episode.getSeason().getId()));
        } else {
            // Fehler: Formuler erneut anzeigen
            FormValues formValues = new FormValues();
            formValues.setValues(request.getParameterMap());
            formValues.setErrors(errors);

            HttpSession session = request.getSession();
            session.setAttribute("episode_form", formValues);

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
    private void deleteEpisode(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Datensatz löschen
        Episode episode = this.getRequestedEpisode(request);
        this.episodeBean.delete(episode);

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
    private Episode getRequestedEpisode(HttpServletRequest request) {
        // Zunächst davon ausgehen, dass ein neuer Satz angelegt werden soll
        Episode episode = new Episode();
        episode.setReleaseDate(new Date(System.currentTimeMillis()));

        // ID aus der URL herausschneiden
        String episodeId = request.getPathInfo();

        if (episodeId == null) {
            episodeId = "";
        }

        episodeId = episodeId.substring(1);

        if (episodeId.endsWith("/")) {
            episodeId = episodeId.substring(0, episodeId.length() - 1);
        }

        // Versuchen, den Datensatz mit der übergebenen ID zu finden
        try {
            episode = this.episodeBean.findById(Long.parseLong(episodeId));
        } catch (NumberFormatException ex) {
            // Ungültige oder keine ID in der URL enthalten
            try{ //Auf Serie zur neuen Staffel Prüfen
                if(episodeId.startsWith("new")){
                episode.setSeason(this.seasonBean.findById(Long.parseLong(episodeId.substring(3))));
            }
            } catch(NumberFormatException exs){
                
            }
            
            
        }

        return episode;
    }


    private FormValues createEpisodeForm(Episode episode) {
        Map<String, String[]> values = new HashMap<>();

            
        values.put("episode_season", new String[]{
            episode.getSeason().getSerie().getTitle()+", Staffel "+episode.getSeason().getNr()
        });

        
        values.put("episode_nr", new String[]{
            "" + episode.getNr()
        });
        

        values.put("episode_release_date", new String[]{
            episode.getReleaseDate().toString()
        });


        values.put("episode_status", new String[]{
            episode.getStatus().toString()
        });

        values.put("episode_title", new String[]{
            episode.getTitle()
        });

        values.put("episode_description", new String[]{
            episode.getDescription()
        });
        
        values.put("episode_length", new String[]{
            "" + episode.geLength()
        });


        FormValues formValues = new FormValues();
        formValues.setValues(values);
        return formValues;
    }

    

}
