<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib tagdir="/WEB-INF/tags/templates" prefix="template"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<template:base>
    <jsp:attribute name="title">
        User Profil Bearbeiten
    </jsp:attribute>

    <jsp:attribute name="head">
        <link rel="stylesheet" href="<c:url value="/css/form.css"/>" />
    </jsp:attribute>

    <jsp:attribute name="menu">
            <div class="menuitem">
                <a href="<c:url value="/app/tasks/list/"/>">Liste</a>
            </div>

            <div class="menuitem">
                <a href="<c:url value="/app/tasks/task/new/"/>">Aufgabe anlegen</a>
            </div>

            <div class="menuitem">
                <a href="<c:url value="/app/tasks/categories/"/>">Kategorien bearbeiten</a>
            </div>
    </jsp:attribute>

    <jsp:attribute name="content">
        <div class="container">
            <form method="post" class="stacked">
                <div class="column">
                    <%-- CSRF-Token --%>
                    <input type="hidden" name="csrf_token" value="${csrf_token}">

                    <%-- Eingabefelder --%>
                    <label for="j_username">
                        Benutzername:
                    </label>
                    <input type="text" name="profil_username" value="${user.username}" readonly="readonly">

                    <label for="j_username">
                        Vorname:
                        <span class="required">*</span>
                    </label>
                    <input type="text" name="profil_vorname" value="${user.vorname}">
                    <label for="j_username">
                        Nachname:
                        <span class="required">*</span>
                    </label>
                    <input type="text" name="profil_nachname" value="${user.nachname}">

                    <button type="submit" name="action" value="save">Änderung speichern</button>
                </div>
            </form>
                    
            <a href="<c:url value="/app/profil/edit/pw"/>">Passwort ändern</a> 
            
        </div>
    </jsp:attribute>
</template:base>
