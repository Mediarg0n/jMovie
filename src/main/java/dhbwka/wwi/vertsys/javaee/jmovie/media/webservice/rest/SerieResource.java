/*
 * Copyright © 2019 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbwka.wwi.vertsys.javaee.jmovie.media.webservice.rest;

import dhbwka.wwi.vertsys.javaee.jmovie.media.ejb.GenreBean;
import dhbwka.wwi.vertsys.javaee.jmovie.media.ejb.MediaBean;
import dhbwka.wwi.vertsys.javaee.jmovie.media.ejb.SerieBean;
import dhbwka.wwi.vertsys.javaee.jmovie.media.jpa.Media;
import dhbwka.wwi.vertsys.javaee.jmovie.media.jpa.Movie;
import dhbwka.wwi.vertsys.javaee.jmovie.media.jpa.Serie;
import dhbwka.wwi.vertsys.javaee.jmovie.media.jpa.WatchStatus;
import dhbwka.wwi.vertsys.javaee.jmovie.media.webservice.pojos.RESTMedia;
import dhbwka.wwi.vertsys.javaee.jmovie.media.webservice.pojos.RESTMovie;
import dhbwka.wwi.vertsys.javaee.jmovie.media.webservice.pojos.RESTSerie;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author bpall
 */
@Path("Serien")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SerieResource {


    
    
    @EJB
    private SerieBean serieBean;
    
    @EJB 
    private GenreBean GenreBean;
    
    //Alle Serien finden
    @GET
    public List<RESTSerie> findSerien() {
        ArrayList<RESTSerie> list = new ArrayList<RESTSerie>();
        for(Serie serie: this.serieBean.findAll()){
            list.add(new RESTSerie((Serie) serie));
        }
        return list;    
    }
    
    //Serie durch Id finden
    @GET
    @Path("/{id}/")
    public RESTSerie getSerie(@PathParam("id") long id) {
        Serie serie = this.serieBean.findById(id);
        return new RESTSerie((Serie) serie);
    }
   
    
    //Serien nach Titel, Genre und Status suchen
    @GET
    @Path("/search/")
    public List<RESTSerie> search(
            @QueryParam("title") String title,
            @QueryParam("genre") String genre,
            @QueryParam("status") String status) {
        
        List<RESTSerie> list = new ArrayList<>();
        for(Serie serie : serieBean.search(title,
                ((genre!=null)?(GenreBean.find(genre)):null),
                ((status!=null)?(WatchStatus.valueOf(status)):null))){
         
            list.add(new RESTSerie((Serie) serie));
        }
        return list;
        
         
    }
    
    
    
}
