/*
 * Copyright © 2019 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbwka.wwi.vertsys.javaee.jmovie.media.ejb;

import dhbwka.wwi.vertsys.javaee.jmovie.common.ejb.EntityBean;
import dhbwka.wwi.vertsys.javaee.jmovie.media.jpa.Genre;
import dhbwka.wwi.vertsys.javaee.jmovie.media.jpa.Movie;
import dhbwka.wwi.vertsys.javaee.jmovie.media.jpa.WatchStatus;
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
     
     
     public List<Movie> search(String title, Genre genre, WatchStatus status) {
        // Hilfsobjekt zum Bauen des Query
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        
        // SELECT t FROM Serie s
        CriteriaQuery<Movie> query = cb.createQuery(Movie.class);
        Root<Movie> from = query.from(Movie.class);
        query.select(from);

        // ORDER BY dueDate, dueTime
        query.orderBy(cb.desc(from.get("releaseDate")));
        
        // WHERE s.title LIKE :title
        Predicate p = cb.conjunction();
        
        if (title != null && !title.trim().isEmpty()) {
            p = cb.and(p, cb.like(from.get("title"), "%" + title + "%"));
            query.where(p);
        }
        
        // WHERE s.genre = :genre
        if (genre != null) {
            p = cb.and(p, cb.isMember(genre,from.get("genres")));
            query.where(p);
        }
        
        // WHERE s.status = :status
        if (status != null) {
            p = cb.and(p, cb.equal(from.get("status"), status));
            query.where(p);
        }
        
        return em.createQuery(query).getResultList();
    }
     
}
    

