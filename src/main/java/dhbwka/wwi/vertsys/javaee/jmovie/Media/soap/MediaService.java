/*
 * Copyright © 2019 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbwka.wwi.vertsys.javaee.jmovie.Media.soap;

import dhbwka.wwi.vertsys.javaee.jmovie.Media.ejb.MediaBean;
import dhbwka.wwi.vertsys.javaee.jmovie.common.ejb.UserBean;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;

/**
 *
 * @author D070429
 */
@WebService(serviceName = "MediaService")
public class MediaService {

    @EJB
    private UserBean userBean;
    
    @EJB
    private MediaBean mediaBean;
    
    @WebMethod
    @WebResult(name = "Medien")
    public String getSecretMessage(
            @WebParam(name = "username", header = true) String username,
            @WebParam(name = "password", header = true) String password)
            {

        
        return "Streng geheim! For your eyes only!";
    }

}
