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
public class RESTMovie extends RESTMedia implements Serializable {

    private int movieLength;
    private int watchedUntil;

        
    //<editor-fold defaultstate="collapsed" desc="Konstruktoren">
    public RESTMovie() {
        super();
    }

    public RESTMovie(int movieLength, int watchedUntil, RESTUser owner, List<Genre> genres, String title, String description, Date releaseDate) {
        super(owner, genres, title, description, releaseDate);
        this.movieLength = movieLength;
        this.watchedUntil = watchedUntil;
    }
    
    public RESTMovie(Movie movie) {
        super(movie);
        this.movieLength = movie.getMovieLength();
        this.watchedUntil = movie.getWatchedUntil();
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
