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


import dhbwka.wwi.vertsys.javaee.jmovie.media.jpa.Serie;
import dhbwka.wwi.vertsys.javaee.jmovie.common.ejb.EntityBean;
import dhbwka.wwi.vertsys.javaee.jmovie.media.jpa.Genre;
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
public class SerieBean extends EntityBean<Serie, Long> { 
   
    public SerieBean() {
        super(Serie.class);
    }
    
    /**
     * Alle Aufgaben eines Benutzers, nach Fälligkeit sortiert zurückliefern.
     * @param username Benutzername
     * @return Alle Aufgaben des Benutzers
     */
    public List<Serie> findByUsername(String username) {
        return em.createQuery("SELECT s FROM Serie s WHERE s.owner.username = :username ORDER BY s.releaseDate")
                 .setParameter("username", username)
                 .getResultList();
    }
    
    public Serie getSerie(long id){
        return em.find(Serie.class, id);
    }
    
    /**
     * Suche nach Aufgaben anhand ihrer Bezeichnung, Kategorie und Status.
     * 
     * Anders als in der Vorlesung behandelt, wird die SELECT-Anfrage hier
     * mit der CriteriaBuilder-API vollkommen dynamisch erzeugt.
     * 
     * @param search In der Kurzbeschreibung enthaltener Text (optional)
     * @param category Kategorie (optional)
     * @param status Status (optional)
     * @return Liste mit den gefundenen Aufgaben
     */
    public List<Serie> search(String title, Genre genre, WatchStatus status) {
        // Hilfsobjekt zum Bauen des Query
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        
        // SELECT t FROM Serie s
        CriteriaQuery<Serie> query = cb.createQuery(Serie.class);
        Root<Serie> from = query.from(Serie.class);
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
            p = cb.and(p, cb.equal(from.get("genre"), genre));
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
