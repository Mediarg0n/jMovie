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

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author bpall
 */
@Entity
//@IdClass(SeasonId.class)
public class Season implements Serializable {
    
    @Id
    @Size(min = 1, max = 50, message = "Eine Serien kann höchstens 50 Episoden haben.")
    private int nr;
    
    
    //@Id
    @ManyToOne
    @NotNull(message = "Die Episode muss einer Season zugeordet werden")
    private Serie serie;
    
    
    @OneToMany(mappedBy = "season")
    private List<Episode> episodes;
   

    
    //<editor-fold defaultstate="collapsed" desc="Konstruktoren">
    public Season(){
        
    }

    public Season(int nr, Serie serie, List<Episode> episodes) {
        this.nr = nr;
        this.serie = serie;
        this.episodes = episodes;
    }
    
    
    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Setter und Getter">

    public int getNr() {
        return nr;
    }
 
    public void setNr(int nr) {
        this.nr = nr;
    }

    public Serie getSerie() {
        return serie;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }
    
    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
    }
    
    //</editor-fold>
    
}
