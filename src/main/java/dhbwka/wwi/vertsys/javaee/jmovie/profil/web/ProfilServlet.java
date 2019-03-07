/*
 * Copyright © 2019 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbwka.wwi.vertsys.javaee.jmovie.profil.web;

import dhbwka.wwi.vertsys.javaee.jmovie.common.ejb.UserBean;
import dhbwka.wwi.vertsys.javaee.jmovie.common.jpa.User;
import dhbwka.wwi.vertsys.javaee.jmovie.dashboard.ejb.DashboardContentProvider;
import dhbwka.wwi.vertsys.javaee.jmovie.dashboard.ejb.DashboardSection;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet für die Startseite mit dem Übersichts-Dashboard.
 */
@WebServlet(urlPatterns = {"/app/profil/"})
public class ProfilServlet extends HttpServlet {

    
    @EJB
    UserBean userBean;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        
        User user = userBean.getCurrentUser();
        request.setAttribute("user", user);
        
        

        // Anfrage an die JSP weiterleiten
        request.getRequestDispatcher("/WEB-INF/profil/profil_view.jsp").forward(request, response);
    }

}
