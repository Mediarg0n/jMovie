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
import dhbwka.wwi.vertsys.javaee.jmovie.media.ejb.MovieBean;
import dhbwka.wwi.vertsys.javaee.jmovie.media.jpa.Movie;
import dhbwka.wwi.vertsys.javaee.jmovie.media.jpa.WatchStatus;
import dhbwka.wwi.vertsys.javaee.jmovie.media.webservice.pojos.RESTMovie;
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
@Path("Filme")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MovieResource {

    
    @EJB
    private MovieBean movieBean;
    
    @EJB 
    private GenreBean GenreBean;
    
    //Alle Serien finden
    @GET
    public List<RESTMovie> findMovies() {
        ArrayList<RESTMovie> list = new ArrayList<RESTMovie>();
        for(Movie movie: this.movieBean.findAll()){
            list.add(new RESTMovie((Movie) movie));
        }
        return list;    
    }
    
    //Serie durch Id finden
    @GET
    @Path("/{id}/")
    public RESTMovie getMovie(@PathParam("id") long id) {
        Movie movie = this.movieBean.findById(id);
        return new RESTMovie((Movie) movie);
    }
   
    
    //Serien nach Titel, Genre und Status suchen
    @GET
    @Path("/search/")
    public List<RESTMovie> search(
            @QueryParam("title") String title,
            @QueryParam("genre") String genre,
            @QueryParam("status") String status) {
        
        List<RESTMovie> list = new ArrayList<>();
        for(Movie movie : movieBean.search(title,
                GenreBean.find(genre),
                ((status!=null)?(WatchStatus.valueOf(status)):null))){
         
            list.add(new RESTMovie((Movie) movie));
        }
        return list;
        
         
    }
    
    
    
}
