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

import dhbwka.wwi.vertsys.javaee.jmovie.Media.jpa.Media;
import dhbwka.wwi.vertsys.javaee.jmovie.common.ejb.EntityBean;
import dhbwka.wwi.vertsys.javaee.jmovie.Media.jpa.Genre;
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
 * @author bpall
 */
@Stateless
@RolesAllowed("app-user")
public class MediaBean extends EntityBean<Media, Long> { 
   
    public MediaBean() {
        super(Media.class);
    }
    
    /**
     * Alle Aufgaben eines Benutzers, nach Fälligkeit sortiert zurückliefern.
     * @param username Benutzername
     * @return Alle Aufgaben des Benutzers
     */
    public List<Media> findByUsername(String username) {
        return em.createQuery("SELECT m FROM Media m WHERE m.owner.username = :username ORDER BY m.releaseDate")
                 .setParameter("username", username)
                 .getResultList();
    }
    
    public Media getMedia(long id){
        return em.find(Media.class, id);
    }
    
   //TODO noch mehr Suchkriterien
     /**
     * Suche nach Medien anhand ihres Titels, Genres und Status. Sortiert nach Erscheinungsjahr.
     * 
     * @param title Titel des Films (optional)
     * @param genre Genre des Films (optional)
     * @param status Status (optional)
     * @return Liste mit den gefundenen Filmen
     */
    public List<Media> search(String title, Genre genre, WatchStatus status) {
        // Hilfsobjekt zum Bauen des Query
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        
        // SELECT t FROM Media m
        CriteriaQuery<Media> query = cb.createQuery(Media.class);
        Root<Media> from = query.from(Media.class);
        query.select(from);

        // ORDER BY dueDate, dueTime
        query.orderBy(cb.desc(from.get("releaseDate")));
        
        // WHERE m.title LIKE :title
        Predicate p = cb.conjunction();
        
        if (title != null && !title.trim().isEmpty()) {
            p = cb.and(p, cb.like(from.get("title"), "%" + title + "%"));
            query.where(p);
        }
        
        // WHERE m.genre = :genre
        if (genre != null) {
            p = cb.and(p, cb.isMember(genre,from.get("genres")));
            query.where(p);
        }
        
        // WHERE m.status = :status
        if (status != null) {
            p = cb.and(p, cb.equal(from.get("status"), status));
            query.where(p);
        }
        
        return em.createQuery(query).getResultList();
    }
}

