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
import dhbwka.wwi.vertsys.javaee.jmovie.tasks.jpa.Category;
import dhbwka.wwi.vertsys.javaee.jmovie.tasks.jpa.Genere;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;

/**
 *
 * @author D070429
 */

@Stateless
@RolesAllowed("app-user")
public class GenereBean extends EntityBean<Genere, Long>{
    
     public GenereBean() {
        super(Genere.class);
    }
     
     
     /**
     * Auslesen aller Generes, alphabetisch sortiert.
     *
     * @return Liste mit allen Generes
     */
    public List<Genere> findAllSorted() {
        return this.em.createQuery("SELECT c FROM Genere c ORDER BY c.name").getResultList();
    }
    
}
