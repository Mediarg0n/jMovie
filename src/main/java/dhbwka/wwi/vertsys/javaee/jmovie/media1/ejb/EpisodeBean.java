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

import dhbwka.wwi.vertsys.javaee.jmovie.media.jpa.Episode;
import dhbwka.wwi.vertsys.javaee.jmovie.media.jpa.Season;
import dhbwka.wwi.vertsys.javaee.jmovie.common.ejb.EntityBean;
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
 * @author bpall
 */

@Stateless
@RolesAllowed("app-user")
public class EpisodeBean extends EntityBean<Episode,Long>{

    public EpisodeBean() {
        super(Episode.class);
    }
    
    public Episode getEpisode(long id){
        return em.find(Episode.class, id);
    }
   
    
    public List<Episode> search(Integer nr, String title, Season season, WatchStatus status) {
        // Hilfsobjekt zum Bauen des Query
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        
        // SELECT t FROM Serie s
        CriteriaQuery<Episode> query = cb.createQuery(Episode.class);
        Root<Episode> from = query.from(Episode.class);
        query.select(from);

        // ORDER BY dueDate, dueTime
        query.orderBy(cb.desc(from.get("releaseDate")));
        
        // WHERE e.title LIKE :title
        Predicate p = cb.conjunction();
        
        if (title != null && !title.trim().isEmpty()) {
            p = cb.and(p, cb.like(from.get("title"), "%" + title + "%"));
            query.where(p);
        }
        
        if (nr != null) {
             p = cb.and(p, cb.equal(from.get("nr"), nr));
            query.where(p);
        }
        
        // WHERE s.genre = :genre
        if (season != null) {
            p = cb.and(p, cb.equal(from.get("season"), season));
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
