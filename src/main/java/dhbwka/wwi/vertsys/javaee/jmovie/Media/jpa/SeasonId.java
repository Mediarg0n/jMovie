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
import java.util.Objects;



/**
 *
 * @author bpall
 */
public class SeasonId implements Serializable {
    private int nr;
    private Serie serie;

    //<editor-fold defaultstate="collapsed" desc="Konstruktoren">
    public SeasonId(){
        
    }
    
    public SeasonId(int nr, Serie serie) {
        this.nr = nr;
        this.serie = serie;
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
    
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="hashCode und Equals">
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + this.nr;
        hash = 47 * hash + Objects.hashCode(this.serie);
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
        final SeasonId other = (SeasonId) obj;
        if (this.nr != other.nr) {
            return false;
        }
        if (!Objects.equals(this.serie, other.serie)) {
            return false;
        }
        return true;
    }
      //</editor-fold>
    
}
