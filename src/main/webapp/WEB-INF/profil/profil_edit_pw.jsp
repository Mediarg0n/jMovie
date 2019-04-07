<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib tagdir="/WEB-INF/tags/templates" prefix="template"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<template:base>
    <jsp:attribute name="title">
         Passwort ändern
    </jsp:attribute>

    <jsp:attribute name="head">
        <link rel="stylesheet" href="<c:url value="/css/form.css"/>" />
    </jsp:attribute>

    <jsp:attribute name="menu">
            <div class="menuitem">
                <a href="<c:url value="/app/dashboard/"/>">Dashboard</a>
            </div>
            <div class="menuitem">
                <a href="<c:url value="/app/medias/list/"/>">Liste</a>
            </div>

            <div class="menuitem">
                <a href="<c:url value="/app/medias/serie/new/"/>">Serie anlegen</a>
            </div>
            <div class="menuitem">
                <a href="<c:url value="/app/medias/movie/new/"/>">Film anlegen</a>
            </div>

            <div class="menuitem">
                <a href="<c:url value="/app/medias/genres/"/>">Genre bearbeiten</a>
            </div>
    </jsp:attribute>

    <jsp:attribute name="content">
        <div class="container">
            <form method="post" class="stacked">
                <div class="column">
                    <%-- CSRF-Token --%>
                    <input type="hidden" name="csrf_token" value="${csrf_token}">

                    <%-- Eingabefelder --%>
                    <label for="pw_old">
                        Aktuelles Passwort:
                        <span class="required">*</span>
                    </label>
                    <input type="password" name="pw_old">

                    <label for="pw_new1">
                        Neues Passwort:
                        <span class="required">*</span>
                    </label>
                    <input type="password" name="pw_new1">
                    
                    <label for="pw_new2">
                        Neues Passwort wiederholen
                        <span class="required">*</span>
                    </label>
                    <input type="password" name="pw_new2">

                    <button type="submit" name="action" value="save">Änderung speichern</button>
                </div>
                
                <%-- Fehlermeldungen --%>
                <c:if test="${!empty profil_form.errors}">
                    <ul class="errors">
                        <c:forEach items="${profil_form.errors}" var="error">
                            <li>${error}</li>
                            </c:forEach>
                    </ul>
                </c:if>
            </form>
                    
            <a href="<c:url value="/app/profil/"/>">Zurück zum Profil</a> 
            <a href="<c:url value="/app/profil/edit/"/>">Profil bearbeiten</a> 
            
        </div>
    </jsp:attribute>
</template:base>
