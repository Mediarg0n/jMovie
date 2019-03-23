/*
 * Copyright © 2019 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbwka.wwi.vertsys.javaee.jmovie.Media.jpa;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author bpall
 */
public class EpisodenId implements Serializable {
    private int nr;
    private Season season;
    
    //<editor-fold defaultstate="collapsed" desc="Konstruktoren">
    public EpisodenId(){
    }
    
    public EpisodenId(int nr, Season season){
        this.nr = nr;
        this.season = season;
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Setter und Getter">
    
    public int getNr() {
        return nr;
    }

    public void setNr(int nr) {
        this.nr = nr;
    }

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    //</editor-fold>
    
     //<editor-fold defaultstate="collapsed" desc="hashCode und Equals">
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + this.nr;
        hash = 89 * hash + Objects.hashCode(this.season);
        return hash;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EpisodenId other = (EpisodenId) obj;
        if (this.nr != other.nr) {
            return false;
        }
        if (!Objects.equals(this.season, other.season)) {
            return false;
        }
        return true;
    }
    
      //</editor-fold>
}
