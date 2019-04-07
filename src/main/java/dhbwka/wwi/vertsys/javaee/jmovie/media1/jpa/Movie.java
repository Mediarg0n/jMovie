/*
 * Copyright © 2019 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbwka.wwi.vertsys.javaee.jmovie.media.jpa;

import dhbwka.wwi.vertsys.javaee.jmovie.common.jpa.User;
import java.io.Serializable;
import java.sql.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;


/**
 *
 * @author D070429
 */
@Entity
public class Movie extends Media implements Serializable {

    
    @NotNull(message = "Die Länge des Films darf nicht leer sein.")
    private int movieLength;

    //@NotNull(message = "Der Zeitpunkt darf nicht leer sein.")
    private int watchedUntil = 0;

        
    //<editor-fold defaultstate="collapsed" desc="Konstruktoren">
    public Movie() {
    }


    public Movie(User owner, List<Genre> genre, String title, String description, Date releaseDate, int movieLength) {
        super(owner,genre,title,description,releaseDate);
        this.movieLength = movieLength;

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Setter und Getter">
    
    

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
    
    //</editor-fold>
    
}
