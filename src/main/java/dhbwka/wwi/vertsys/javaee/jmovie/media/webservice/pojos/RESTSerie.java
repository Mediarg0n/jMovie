/*
 * Copyright © 2019 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbwka.wwi.vertsys.javaee.jmovie.media.webservice.pojos;

import dhbwka.wwi.vertsys.javaee.jmovie.media.jpa.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mediarg0n
 */
public class RESTSerie extends RESTMedia implements Serializable {

    private List<RESTSeason> seasons;
    
    
    //<editor-fold defaultstate="collapsed" desc="Konstruktoren">
    public RESTSerie() {
        super();
    }

    public RESTSerie(List<RESTSeason> seasons, Media media) {
        super(media);
        this.seasons = seasons;
    }
    
    public RESTSerie(Serie serie) {
        super(serie);
        //this.seasons = serie.getSeasons();
        this.seasons = new ArrayList<>();
        serie.getSeasons().forEach((season) -> {
            this.seasons.add(new RESTSeason(season));
        });
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Setter und Getter">
    public List<RESTSeason> getSeasons(){
        return seasons;
    }
 
    public void setSeasons(List<RESTSeason> seasons){
        this.seasons= seasons;
    }
    //</editor-fold>
    
}

