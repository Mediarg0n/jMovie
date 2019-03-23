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
import dhbwka.wwi.vertsys.javaee.jmovie.Media.jpa.Genere;
import dhbwka.wwi.vertsys.javaee.jmovie.Media.jpa.Movie;
import dhbwka.wwi.vertsys.javaee.jmovie.Media.jpa.WatchStatus;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author D070429
 */
@Stateless
@RolesAllowed("app-user")
public class MovieBean extends EntityBean<Movie, Long> {
    
     public MovieBean() {
        super(Movie.class);
    }
     
}
    

