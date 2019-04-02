/*
 * Copyright © 2019 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbwka.wwi.vertsys.javaee.jmovie.media.webservice.pojos;

import dhbwka.wwi.vertsys.javaee.jmovie.media.jpa.*;
import java.io.Serializable;
import java.sql.Date;

/**
 *
 * @author bpall
 */
public class RESTEpisode implements Serializable {
    
    private long id;
    private int nr;
    private String title;
    private String releaseDate;
    private int length;
    private String description;
    private WatchStatus status = WatchStatus.NOT_WATCHED;

    //<editor-fold defaultstate="collapsed" desc="Konstruktoren">
    public RESTEpisode(){
        super();
    }

    public RESTEpisode(long id, int nr, String title, Date releaseDate, int length, String description) {
        this.id = id;
        this.nr = nr;
        this.title = title;
        this.releaseDate = releaseDate.toString();
        this.length = length;
        this.description = description;
    }
    
    public RESTEpisode(Episode episode) {
        this.id = episode.getId();
        this.nr = episode.getNr();
        this.title = episode.getTitle();
        this.releaseDate = episode.getReleaseDate().toString();
        this.length = episode.getLength();
        this.description = episode.getDescription();
    }
    
    
    
    
    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Setter und Getter">
    

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNr() {
        return nr;
    }

    public void setNr(int nr) {
        this.nr = nr;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public WatchStatus getStatus() {
        return status;
    }

    public void setStatus(WatchStatus status) {
        this.status = status;
    }

    
    //</editor-fold>
}
