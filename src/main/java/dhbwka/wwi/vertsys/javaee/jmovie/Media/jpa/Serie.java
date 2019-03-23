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

import dhbwka.wwi.vertsys.javaee.jmovie.common.jpa.User;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

/**
 *
 * @author D070429
 */
@Entity
public class Serie extends Media implements Serializable {

    private static final long serialVersionUID = 1L;

    @OneToMany(mappedBy="serie")
    private List<Season> seasons;
    
    
    //<editor-fold defaultstate="collapsed" desc="Konstruktoren">
    public Serie() {
        super();
    }

    public Serie(List<Season> seasons) {
        this.seasons = seasons;
    }

    public Serie(List<Season> seasons, User owner, List<Genere> genere, String title, String description) {
        super(owner, genere, title, description);
        this.seasons = seasons;
    }

    
    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Setter und Getter">
    public List<Season> getSeasons(){
        return seasons;
    }
 
    //</editor-fold>
    
}
