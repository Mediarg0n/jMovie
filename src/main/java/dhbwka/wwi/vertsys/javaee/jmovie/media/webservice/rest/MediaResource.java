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
 * @author D070429
 */

@Path("Medias")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MediaResource {
    
    
    @EJB
    private MediaBean mediaBean;
    
    //Alle Medien finden
    @GET
    public List<RESTMedia> findMedias() {
        ArrayList<RESTMedia> list = new ArrayList<RESTMedia>();
        for(Media media: this.mediaBean.findAll()){
            if(media instanceof Serie)
                list.add(new RESTSerie((Serie) media));
            if(media instanceof Movie)
                list.add(new RESTMovie((Movie) media));
        }
            
        return list;    
    }
    
    //Medium durch Id finden
    @GET
    @Path("/{id}/")
    public RESTMedia getMedia(@PathParam("id") long id) {
        Media media = this.mediaBean.findById(id);
        if(media instanceof Serie)
            return new RESTSerie((Serie) media);
        if(media instanceof Movie)
            return new RESTMovie((Movie) media);
        return null;
    }
   
    
    //Medien nach Titel, Genre und Status suchen
    //Sortiert nach Datum
    /*@GET
    public List<Media> search(String title, Genre genre,WatchStatus status){
        
        return mediaBean.search(title,genre,status);
        
         
    }*/
    
    
    
}
