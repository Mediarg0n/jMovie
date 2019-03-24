/*
 * Copyright © 2019 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbwka.wwi.vertsys.javaee.jmovie.Media.jpa;

import java.io.Serializable;
import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author bpall
 */
@Entity
public class Episode implements Serializable, Playable {
    
    @Id
    @GeneratedValue
    private long id;
    
    @Min(value =1, message = "Die Episodennummer muss zwischen 1 und 50 liegen")
    @Max(value = 50, message = "Die Episodennummer muss zwischen 1 und 50 liegen")
    private int nr;
    
    @ManyToOne
    @NotNull(message = "Die Episode muss einer Season zugeordet werden")
    private Season season;
    
    @Column(length = 50)
    @NotNull(message = "Der Titel darf nicht leer sein.")
    @Size(min = 1, max = 30, message = "Der Titel muss zwischen ein und 30 Zeichen lang sein.")
    private String title;
    
    @NotNull(message = "Das Erscheinungsdatum der Episode darf nicht leer sein.")
    private Date releaseDate;
    
    @NotNull(message = "Die Länge des Episode darf nicht leer sein.")
    private int movieLength;

    @NotNull(message = "Der Zeitpunkt darf nicht leer sein.")
    private int watchedUntil = 0;

    @Lob
    @NotNull
    private String description;
    
    @Enumerated(EnumType.STRING)
    @NotNull
    private WatchStatus status = WatchStatus.NOT_WATCHED;

    //<editor-fold defaultstate="collapsed" desc="Konstruktoren">
    public Episode(){
        
    }

    public Episode(long id, int nr, Season season, String title, Date releaseDate, int movieLength, String description) {
        this.id = id;
        this.nr = nr;
        this.season = season;
        this.title = title;
        this.releaseDate = releaseDate;
        this.movieLength = movieLength;
        this.description = description;
    }
    
    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Setter und Getter">
    
    
    //</editor-fold>

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

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getMovieLength() {
        return movieLength;
    }

    public void setMovieLength(int movieLength) {
        this.movieLength = movieLength;
    }

    public int getWatchedUntil() {
        return watchedUntil;
    }

    public void setWatchedUntil(int watchedUntil) {
        this.watchedUntil = watchedUntil;
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
}
