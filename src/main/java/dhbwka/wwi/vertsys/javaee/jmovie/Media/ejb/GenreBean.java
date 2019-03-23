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

import dhbwka.wwi.vertsys.javaee.jmovie.common.ejb.EntityBean;
import dhbwka.wwi.vertsys.javaee.jmovie.Media.jpa.Genre;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;

/**
 *
 * @author D070429
 */

@Stateless
@RolesAllowed("app-user")
public class GenreBean extends EntityBean<Genre, Long>{
    
     public GenreBean() {
        super(Genre.class);
    }
     
     
     /**
     * Auslesen aller Genres, alphabetisch sortiert.
     *
     * @return Liste mit allen Genres
     */
    public List<Genre> findAllSorted() {
        return this.em.createQuery("SELECT c FROM Genre c ORDER BY c.name").getResultList();
    }
    
}
