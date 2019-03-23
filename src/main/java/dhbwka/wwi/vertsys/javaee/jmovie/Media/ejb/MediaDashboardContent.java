/*
 * Copyright © 2019 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbwka.wwi.vertsys.javaee.jmovie.Media.ejb;


import dhbwka.wwi.vertsys.javaee.jmovie.common.web.WebUtils;
import dhbwka.wwi.vertsys.javaee.jmovie.dashboard.ejb.DashboardContentProvider;
import dhbwka.wwi.vertsys.javaee.jmovie.dashboard.ejb.DashboardSection;
import dhbwka.wwi.vertsys.javaee.jmovie.dashboard.ejb.DashboardTile;
import dhbwka.wwi.vertsys.javaee.jmovie.Media.jpa.Genere;
import dhbwka.wwi.vertsys.javaee.jmovie.Media.jpa.WatchStatus;
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
    private GenereBean genereBean;

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
        List<Genere> generes = this.genereBean.findAllSorted();

        for (Genere genere: generes) {
            section = this.createSection(genere);
            sections.add(section);
        }
    }

    /**
     * Hilfsmethode, die für die übergebene Media-Genere eine neue Rubrik
     * mit Kacheln im Dashboard erzeugt. Je Watchstatus wird eine Kachel
     * erzeugt. Zusätzlich eine Kachel für alle Medien innerhalb des
     * jeweiligen Generes.
     *
     * Ist das Genere null, bedeutet dass, dass eine Rubrik für alle Medien
     * aus allen Generes erzeugt werden soll.
     *
     * @param genere Genere-Kategorie, für die Kacheln erzeugt werden sollen
     * @return Neue Dashboard-Rubrik mit den Kacheln
     */
    private DashboardSection createSection(Genere genere) {
        // Neue Rubrik im Dashboard erzeugen
        DashboardSection section = new DashboardSection();
        String cssClass = "";

        if (genere != null) {
            section.setLabel(genere.getName());
        } else {
            section.setLabel("Alle Generes");
            cssClass = "overview";
        }

        // Eine Kachel für alle Aufgaben in dieser Rubrik erzeugen
        DashboardTile tile = this.createTile(genere, null, "Alle", cssClass + " status-all", "calendar");//???????????????????????????
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

            tile = this.createTile(genere, status, status.getLabel(), cssClass1, icon);
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
     * @param genere
     * @param status
     * @param label
     * @param cssClass
     * @param icon
     * @return
     */
    private DashboardTile createTile(Genere genere, WatchStatus status, String label, String cssClass, String icon) {
        int amount = mediaBean.search(null, genere, status).size();
        String href = "/app/tasks/list/";

        //URL Parameter für Genere wird gesetzt
        if (genere != null) {
            href = WebUtils.addQueryParameter(href, "search_category", "" + genere.getId());
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
