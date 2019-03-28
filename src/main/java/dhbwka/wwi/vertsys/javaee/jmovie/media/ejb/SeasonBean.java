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

import dhbwka.wwi.vertsys.javaee.jmovie.media.jpa.Season;
import dhbwka.wwi.vertsys.javaee.jmovie.media.jpa.Serie;
import dhbwka.wwi.vertsys.javaee.jmovie.common.ejb.EntityBean;
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
public class SeasonBean extends EntityBean<Season, Long> { 
   
    public SeasonBean() {
        super(Season.class);
    }
    
    public Season getSeason(long id){
        return em.find(Season.class, id);
    }
    
    public List<Season> search(Serie serie, Integer nr) {
        // Hilfsobjekt zum Bauen des Query
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        
        // SELECT s FROM Season s
        CriteriaQuery<Season> query = cb.createQuery(Season.class);
        Root<Season> from = query.from(Season.class);
        query.select(from);

        // ORDER BY dueDate, dueTime
        query.orderBy(cb.desc(from.get("nr")));
        
        // WHERE m.title LIKE :title
        Predicate p = cb.conjunction();
        
        if (serie != null) {
            p = cb.and(p, cb.equal(from.get("serie"), serie));
            query.where(p);
        }
        
        // WHERE m.genre = :genre
        if (nr != null) {
            p = cb.and(p, cb.equal(from.get("nr"), nr));
            query.where(p);
        }
        
        
        return em.createQuery(query).getResultList();
    }
    
}
