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

import java.sql.Date;

/**
 *
 * @author bpall
 */
public interface Playable {
    
    public String getTitle();

    public void setTitle(String title) ;
    
    public String getDescription();

    public void setDescription(String description);
    
    public Date getReleaseDate() ;

    public void setReleaseDate(Date releaseDate) ;

    public int getMovieLength();

    public void setMovieLength(int length) ;
    
    public int getWatchedUntil();

    public void setWatchedUntil(int watchedUntil);
    
}
