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
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author bpall
 */
@Entity
//@IdClass(EpisodenId.class)
public class Episode implements Serializable, Playable {
    
    @Id
    @Size(min = 1, max = 50, message = "Eine Serien Staffel kann höchstens 50 Episoden haben.")
    private int nr;
    
  //  @Id
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

    //<editor-fold defaultstate="collapsed" desc="Konstruktoren">
    public Episode(){
        
    }
    
    public Episode(int nr, Season season, String title, Date releaseDate, int movieLength, String description) {
        this.nr = nr;
        this.season = season;
        this.title = title;
        this.releaseDate = releaseDate;
        this.movieLength = movieLength;
        this.description = description;
    }
    
    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Setter und Getter">
    
    
    
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
    
    //@Override
    public Date getReleaseDate() {
        return releaseDate;
    }

    //@Override
    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    //@Override
    public int getMovieLength() {
        return movieLength;
    }

    //@Override
    public void setMovieLength(int movieLength) {
        this.movieLength = movieLength;
    }
    
    
    //@Override
    public int getWatchedUntil() {
        return watchedUntil;
    }

    //@Override
    public void setWatchedUntil(int watchedUntil) {
        this.watchedUntil = watchedUntil;
    }

    //@Override
    public String getTitle() {
        return title;
     }

    //@Override
    public void setTitle(String title) {
       this.title = title;
    }

    //@Override
    public String getDescription() {
        return description;
     }

    //@Override
    public void setDescription(String description) {
        this.description = description;
    }
    //</editor-fold>
}
