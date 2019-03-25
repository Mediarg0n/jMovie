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
import dhbwka.wwi.vertsys.javaee.jmovie.Media.jpa.WatchStatus;
import dhbwka.wwi.vertsys.javaee.jmovie.common.web.WebUtils;
import dhbwka.wwi.vertsys.javaee.jmovie.common.ejb.UserBean;
import dhbwka.wwi.vertsys.javaee.jmovie.common.ejb.ValidationBean;
import dhbwka.wwi.vertsys.javaee.jmovie.common.web.FormValues;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;
import java.util.Map;
import javax.ejb.EJB;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 *
 * @author bpall
 */
public class MediaEditServlet extends HttpServlet{
    
    @EJB
    MediaBean mediaBean;

    @EJB
    GenreBean genreBean;

    @EJB
    UserBean userBean;

    @EJB
    ValidationBean validationBean;
    
    public void doGet(HttpServletRequest request, Media media){
        // Verfügbare Kategorien und Stati für die Suchfelder ermitteln
        request.setAttribute("genres", this.genreBean.findAllSorted());
        request.setAttribute("statuses", WatchStatus.values());

        // Zu bearbeitende Serie einlesen
        HttpSession session = request.getSession();

        
        request.setAttribute("edit", media.getId() != 0);
                      
        
        if (session.getAttribute("media_form") == null) {
            // Keine Formulardaten mit fehlerhaften Daten in der Session,
            // daher Formulardaten aus dem Datenbankobjekt übernehmen
            request.setAttribute("media_form", this.createMediaForm(media));
        }
    }
    
    public void saveMedia(HttpServletRequest request, Media media, List<String> errors){
        String[] mediaGenres = request.getParameterValues("media_genre");
        String mediaReleaseDate = request.getParameter("media_release_date");
        String mediaStatus = request.getParameter("media_status");
        String mediaTitle = request.getParameter("media_title");
        String mediaDescription = request.getParameter("media_description");

        

        if (mediaGenres != null && mediaGenres.length>0) {
            try {
                List<Genre> genres = new ArrayList<Genre>();
                for(String mediaGenre: mediaGenres)
                    genres.add(this.genreBean.findById(Long.parseLong(mediaGenre)));
                media.setGenres(genres);
            } catch (NumberFormatException ex) {
                // Ungültige oder keine ID mitgegeben
            }
        }

        Date releaseDate = WebUtils.parseDate(mediaReleaseDate);
        if (releaseDate != null) {
            media.setReleaseDate(releaseDate);
        } else {
            errors.add("Das Datum muss dem Format dd.mm.yyyy entsprechen.");
        }

        try {
            media.setStatus(WatchStatus.valueOf(mediaStatus));
        } catch (IllegalArgumentException ex) {
            errors.add("Der ausgewählte Status ist nicht vorhanden.");
        }

        media.setTitle(mediaTitle);
        media.setDescription(mediaDescription);

        this.validationBean.validate(media, errors);
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
    public FormValues createMediaForm(Media media) {
        Map<String, String[]> values = new HashMap<>();

        values.put("media_id", new String[]{
            String.valueOf(media.getId())
        });
        
        values.put("media_owner", new String[]{
            media.getOwner().getUsername()
        });

        if (media.getGenres()!= null) {
            values.put("media_category", new String[]{
                "" + media.getGenres().get(0).getId()
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
