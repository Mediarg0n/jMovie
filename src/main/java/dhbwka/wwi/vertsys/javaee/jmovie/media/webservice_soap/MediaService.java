/*
 * Copyright © 2019 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbwka.wwi.vertsys.javaee.jmovie.media.webservice_soap;

import dhbwka.wwi.vertsys.javaee.jmovie.common.ejb.UserBean;
import dhbwka.wwi.vertsys.javaee.jmovie.media.ejb.MediaBean;
import dhbwka.wwi.vertsys.javaee.jmovie.media.jpa.Media;
import java.util.List;
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
    public List<Media> getAllMedia(
            @WebParam(name = "username", header = true) String username,
            @WebParam(name = "password", header = true) String password) throws UserBean.InvalidCredentialsException
            {
                
                //userBean.validateUser(username, password); 
                
                return mediaBean.search(null,null,null);
    }

}