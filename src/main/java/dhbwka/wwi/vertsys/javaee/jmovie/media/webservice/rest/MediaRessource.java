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

import dhbwka.wwi.vertsys.javaee.jmovie.media.ejb.MediaBean;
import dhbwka.wwi.vertsys.javaee.jmovie.media.jpa.Genre;
import dhbwka.wwi.vertsys.javaee.jmovie.media.jpa.Media;
import dhbwka.wwi.vertsys.javaee.jmovie.media.jpa.WatchStatus;
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
 * @author D070429
 */

@Path("Medias")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MediaRessource {
    
    
    @EJB
    private MediaBean mediaBean;
    
    //Alle Medien finden
    @GET
    public List<Media> findSongs() {
        return this.mediaBean.findAll();
    }
    
    //Medium durch Id finden
    @GET
    @Path("{id}")
    public Media getSong(@PathParam("id") long id) {
        return this.mediaBean.findById(id);
    }
   
    
    //Medien nach Titel, Genre und Status suchen
    //Sortiert nach Datum
    /*@GET
    public List<Media> search(String title, Genre genre,WatchStatus status){
        
        return mediaBean.search(title,genre,status);
        
         
    }*/
    
    
    
}
