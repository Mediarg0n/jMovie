<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib tagdir="/WEB-INF/tags/templates" prefix="template"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<template:base>
    <jsp:attribute name="title">
        User Profil
    </jsp:attribute>

    <jsp:attribute name="head">
        <link rel="stylesheet" href="<c:url value="/css/dashboard.css"/>" />
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
            <div>
                Benutzername:
                <span>${user.username}</span>
            </div>
            <div>
                Vorname:
                <span>${user.vorname}</span>
            </div>
            <div>
                Nachname:
                <span>${user.nachname}</span>
            </div>
             
            <a href="<c:url value="/app/profil/edit/"/>">Daten ändern</a>  
            <a href="<c:url value="/app/profil/edit/pw/"/>">Passwort ändern</a>  
        </div>
    </jsp:attribute>
</template:base>