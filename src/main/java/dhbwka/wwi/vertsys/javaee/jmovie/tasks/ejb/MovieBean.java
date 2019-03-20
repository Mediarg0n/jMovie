/*
 * Copyright © 2019 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbwka.wwi.vertsys.javaee.jmovie.tasks.ejb;

import dhbwka.wwi.vertsys.javaee.jmovie.common.ejb.EntityBean;
import dhbwka.wwi.vertsys.javaee.jmovie.tasks.jpa.Genere;
import dhbwka.wwi.vertsys.javaee.jmovie.tasks.jpa.Movie;
import dhbwka.wwi.vertsys.javaee.jmovie.tasks.jpa.WatchStatus;
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
     
     
     
     //TODO noch mehr Suchkriterien
     /**
     * Suche nach Filme anhand ihres Titels, Generes und Status. Sortiert nach Erscheinungsjahr.
     * 
     * @param title Titel des Films (optional)
     * @param genere Genere des Films (optional)
     * @param status Status (optional)
     * @return Liste mit den gefundenen Filmen
     */
    public List<Movie> search(String title, Genere genere, WatchStatus status) {
        // Hilfsobjekt zum Bauen des Query
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        
        // SELECT t FROM Task t
        CriteriaQuery<Movie> query = cb.createQuery(Movie.class);
        Root<Movie> from = query.from(Movie.class);
        query.select(from);

        // ORDER BY dueDate, dueTime
        query.orderBy(cb.desc(from.get("releaseDate")));
        
        // WHERE t.shortText LIKE :search
        Predicate p = cb.conjunction();
        
        if (title != null && !title.trim().isEmpty()) {
            p = cb.and(p, cb.like(from.get("title"), "%" + title + "%"));
            query.where(p);
        }
        
        // WHERE t.category = :category
        if (genere != null) {
            p = cb.and(p, cb.equal(from.get("genere"), genere));
            query.where(p);
        }
        
        // WHERE t.status = :status
        if (status != null) {
            p = cb.and(p, cb.equal(from.get("status"), status));
            query.where(p);
        }
        
        return em.createQuery(query).getResultList();
    }
}
    

