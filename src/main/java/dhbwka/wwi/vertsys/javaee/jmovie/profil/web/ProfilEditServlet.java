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
import dhbwka.wwi.vertsys.javaee.jmovie.common.web.WebUtils;
import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
    import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet für die Startseite mit dem Übersichts-Dashboard.
 */
@WebServlet(urlPatterns = {"/app/profil/edit/"})
public class ProfilEditServlet extends HttpServlet {

    
    @EJB
    UserBean userBean;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        
        User user = userBean.getCurrentUser();
        request.setAttribute("user", user);
        
        

        // Anfrage an die JSP weiterleiten
        request.getRequestDispatcher("/WEB-INF/profil/profil_edit.jsp").forward(request, response);
    }
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Angeforderte Aktion ausführen
        String action = request.getParameter("action");

        if (action == null) {
            action = "";
        }

        switch (action) {
            case "save":
                saveChanges(request, response);
                break;
        }
    }
    
    private void saveChanges(HttpServletRequest request, HttpServletResponse response) throws IOException{
        response.sendRedirect(WebUtils.appUrl(request, "/app/profil/"));
    }

}
