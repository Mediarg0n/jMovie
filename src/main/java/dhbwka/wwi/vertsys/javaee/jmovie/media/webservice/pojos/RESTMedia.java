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
import java.util.List;

/**
 *
 * @author Mediarg0n
 */
public class RESTMedia implements Serializable {

    private long id;
    private RESTUser owner;
    private List<Genre> genres;
    private String title;
    private String description;
    private String releaseDate;
    private WatchStatus status = WatchStatus.NOT_WATCHED;
    
    private String type;
    
    
    //<editor-fold defaultstate="collapsed" desc="Konstruktoren">
    public RESTMedia() {
        super();
        if(this instanceof RESTSerie)
            type = "SERIE";
        if(this instanceof RESTMovie)
            type = "MOVIE";
    }


    
    public RESTMedia(RESTUser owner, List<Genre> genres, String title, String description, Date releaseDate) {
        this();
        this.owner = owner;
        this.genres = genres;
        this.title = title;
        this.description = description;
        this.releaseDate = releaseDate.toString();
    }
    
    public RESTMedia(Media media) {
        this();
        this.id = media.getId();
        this.owner = new RESTUser(media.getOwner());
        this.genres = media.getGenres();
        this.title = media.getTitle();
        this.description = media.getDescription();
        this.releaseDate = media.getReleaseDate().toString();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Setter und Getter">
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public RESTUser getOwner() {
        return owner;
    }

    public void setOwner(RESTUser owner) {
        this.owner = owner;
    }


    public List<Genre> getGenres() {
        return genres;
    }


    public void setGenres(List<Genre> genre) {

        this.genres = genre;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    
    public WatchStatus getStatus() {
        return status;
    }

    public void setStatus(WatchStatus status) {
        this.status = status;
    }
    
    public String getType(){
        return type;
    }
    
    public void setType(String type){
        this.type = type;
    }
    //</editor-fold>
    
}
