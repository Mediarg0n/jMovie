/*
 * Copyright © 2019 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbwka.wwi.vertsys.javaee.jmovie.media.web;

import dhbwka.wwi.vertsys.javaee.jmovie.media.ejb.GenreBean;
import dhbwka.wwi.vertsys.javaee.jmovie.media.ejb.MediaBean;
import dhbwka.wwi.vertsys.javaee.jmovie.media.jpa.Genre;
import dhbwka.wwi.vertsys.javaee.jmovie.media.jpa.Media;
import dhbwka.wwi.vertsys.javaee.jmovie.media.jpa.WatchStatus;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet für die tabellarische Auflisten der Medien.
 * @author bpall
 */
@WebServlet(urlPatterns = {"/app/medias/list/"})
public class MediaListServlet  extends HttpServlet {

    @EJB
    private GenreBean genreBean;
    
    @EJB
    private MediaBean mediaBean;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verfügbare Kategorien und Stati für die Suchfelder ermitteln
        request.setAttribute("genres", this.genreBean.findAllSorted());
        request.setAttribute("statuses", WatchStatus.values());

        // Suchparameter aus der URL auslesen
        String searchTitle = request.getParameter("search_title");
        String searchGenre = request.getParameter("search_genre");
        String searchStatus = request.getParameter("search_status");

        // Anzuzeigende Aufgaben suchen
        Genre genre = null;
        WatchStatus status = null;

        if (searchGenre != null) {
            try {
                genre = this.genreBean.findById(Long.parseLong(searchGenre));
            } catch (NumberFormatException ex) {
                genre = null;
            }
        }

        if (searchStatus != null) {
            try {
                status = WatchStatus.valueOf(searchStatus);
            } catch (IllegalArgumentException ex) {
                status = null;
            }

        }

        List<Media> medias = this.mediaBean.search(searchTitle, genre, status);
        request.setAttribute("medias", medias);

        // Anfrage an die JSP weiterleiten
        request.getRequestDispatcher("/WEB-INF/medias/media_list.jsp").forward(request, response);
    }
}
