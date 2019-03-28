/*
 * Copyright © 2019 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbwka.wwi.vertsys.javaee.jmovie.media.ejb;


import dhbwka.wwi.vertsys.javaee.jmovie.common.web.WebUtils;
import dhbwka.wwi.vertsys.javaee.jmovie.dashboard.ejb.DashboardContentProvider;
import dhbwka.wwi.vertsys.javaee.jmovie.dashboard.ejb.DashboardSection;
import dhbwka.wwi.vertsys.javaee.jmovie.dashboard.ejb.DashboardTile;
import dhbwka.wwi.vertsys.javaee.jmovie.media.jpa.Genre;
import dhbwka.wwi.vertsys.javaee.jmovie.media.jpa.WatchStatus;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author D070429
 */

/**
 * EJB zur Definition der Dashboard-Kacheln für Medien.
 */
@Stateless(name = "medias")
public class MediaDashboardContent implements DashboardContentProvider {
    
    @EJB
    private GenreBean genreBean;


    @EJB
    private MediaBean mediaBean;
    
    /**
     * Vom Dashboard aufgerufenen Methode, um die anzuzeigenden Rubriken und
     * Kacheln zu ermitteln.
     *
     * @param sections Liste der Dashboard-Rubriken, an die die neuen Rubriken
     * angehängt werden müssen
     */
    @Override
    public void createDashboardContent(List<DashboardSection> sections) {
        // Zunächst einen Abschnitt mit einer Gesamtübersicht aller Aufgaben
        // in allen Kategorien erzeugen
        DashboardSection section = this.createSection(null);
        sections.add(section);

        // Anschließend je Kategorie einen weiteren Abschnitt erzeugen

        List<Genre> genres = this.genreBean.findAllSorted();



        for (Genre genre: genres) {
            section = this.createSection(genre);
            sections.add(section);
        }
    }

    /**
     * Hilfsmethode, die für die übergebene Media-Genre eine neue Rubrik
     * mit Kacheln im Dashboard erzeugt. Je Watchstatus wird eine Kachel
     * erzeugt. Zusätzlich eine Kachel für alle Medien innerhalb des
     * jeweiligen genres.
     *
     * Ist das Genre null, bedeutet dass, dass eine Rubrik für alle Medien
     * aus allen Genres erzeugt werden soll.
     *
     * @param genre Genre-Kategorie, für die Kacheln erzeugt werden sollen
     * @return Neue Dashboard-Rubrik mit den Kacheln
     */

    private DashboardSection createSection(Genre genre) {

        // Neue Rubrik im Dashboard erzeugen
        DashboardSection section = new DashboardSection();
        String cssClass = "";

        if (genre != null) {
            section.setLabel(genre.getName());
        } else {
            section.setLabel("Alle Genres");
            cssClass = "overview";
        }

        // Eine Kachel für alle Aufgaben in dieser Rubrik erzeugen
        DashboardTile tile = this.createTile(genre, null, "Alle", cssClass + " status-all", "calendar");//???????????????????????????
        section.getTiles().add(tile);

        // Ja Aufgabenstatus eine weitere Kachel erzeugen
        for (WatchStatus status : WatchStatus.values()) {
            String cssClass1 = cssClass + " status-" + status.toString().toLowerCase();
            String icon = "";

            
            //TODO Icons verändern und schauen dass diese angezeigt werden
            switch (status) {
                case NOT_WATCHED:
                    icon = "doc-text";
                    break;
                case NOT_COMPLETLY_WATCHED:
                    icon = "rocket";
                    break;
                case WATCHED:
                    icon = "ok";
                    break;
                case STOPPED_WATCHING:
                    icon = "cancel";
                    break;
                
            }

            tile = this.createTile(genre, status, status.getLabel(), cssClass1, icon);
            section.getTiles().add(tile);
        }

        // Erzeugte Dashboard-Rubrik mit den Kacheln zurückliefern
        return section;
    }

    /**
     * Hilfsmethode zum Erzeugen einer einzelnen Dashboard-Kachel. In dieser
     * Methode werden auch die in der Kachel angezeigte Anzahl sowie der Link,
     * auf den die Kachel zeigt, ermittelt.
     *
     * @param genre
     * @param status
     * @param label
     * @param cssClass
     * @param icon
     * @return
     */

    private DashboardTile createTile(Genre genre, WatchStatus status, String label, String cssClass, String icon) {
        int amount = mediaBean.search(null, genre, status).size();
        String href = "/app/medias/list/";

        //URL Parameter für Genre wird gesetzt
        if (genre != null) {
            href = WebUtils.addQueryParameter(href, "search_genre", "" + genre.getId());
        }

        //URL Parameter für Status wird gesetzt
        if (status != null) {
            href = WebUtils.addQueryParameter(href, "search_status", status.toString());
        }

        DashboardTile tile = new DashboardTile();
        tile.setLabel(label);
        tile.setCssClass(cssClass);
        tile.setHref(href);
        tile.setIcon(icon);
        tile.setAmount(amount);
        tile.setShowDecimals(false);
        return tile;
    }
    
}
