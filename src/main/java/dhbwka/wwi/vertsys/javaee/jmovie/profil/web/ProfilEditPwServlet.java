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
import dhbwka.wwi.vertsys.javaee.jmovie.common.ejb.ValidationBean;
import dhbwka.wwi.vertsys.javaee.jmovie.common.jpa.User;
import dhbwka.wwi.vertsys.javaee.jmovie.common.web.FormValues;
import dhbwka.wwi.vertsys.javaee.jmovie.common.web.WebUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;



@WebServlet(urlPatterns = {"/app/profil/edit/pw/"})
public class ProfilEditPwServlet extends HttpServlet {

    @EJB
    ValidationBean validationBean;
    
    @EJB
    UserBean userBean;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //User user = userBean.getCurrentUser();
        //request.setAttribute("user", user);
        
        // Anfrage an die JSP weiterleiten
        request.getRequestDispatcher("/WEB-INF/profil/profil_edit_pw.jsp").forward(request, response);
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
    
    /**
     * Updatet den aktuellen User mit dem übermittelten Werten.
     * @param request
     * @param response
     * @throws IOException 
     */
    private void saveChanges(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String pwOld = request.getParameter("pw_old");
        String pwNew1 = request.getParameter("pw_new1");
        String pwNew2 = request.getParameter("pw_new2");
        
       // User user = userBean.getCurrentUser();
        
        
        
        // Eingaben prüfen
        User user = userBean.getCurrentUser();
        List<String> errors = new ArrayList<String>();
        
        if(!user.checkPassword(pwOld)){
            errors.add("Aktuelles Passowrt ist nicht korrekt.");
        }
        
        user.setPassword(pwNew1);
        validationBean.validate(user.getPassword(), errors);
        
        if (pwNew1 != null && pwNew2 != null && !pwNew1.equals(pwNew2)) {
            errors.add("Die beiden Passwörter stimmen nicht überein.");
        }
        
        
        if (errors.isEmpty()) {
            userBean.update(user);
            response.sendRedirect(WebUtils.appUrl(request, "/app/profil/"));
        }else{
            // Fehler: Formuler erneut anzeigen
            FormValues formValues = new FormValues();
            formValues.setValues(request.getParameterMap());
            formValues.setErrors(errors);
            
            HttpSession session = request.getSession();
            session.setAttribute("profil_form", formValues);
            
            response.sendRedirect(request.getRequestURI());
        }
        
    }

}
