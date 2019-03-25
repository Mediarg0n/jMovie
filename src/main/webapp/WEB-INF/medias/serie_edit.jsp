<%-- 
    Document   : serie_edit
    Created on : 23.03.2019, 20:36:59
    Author     : bpall
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib tagdir="/WEB-INF/tags/templates" prefix="template"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<template:base>
    <jsp:attribute name="title">
        <c:choose>
            <c:when test="${edit}">
                Serie bearbeiten
            </c:when>
            <c:otherwise>
                Serie anlegen
            </c:otherwise>
        </c:choose>
    </jsp:attribute>

    <jsp:attribute name="head">
        
        <link rel="stylesheet" href="<c:url value="/css/media_edit.css"/>" />
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.13.1/css/bootstrap-select.css" />
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.bundle.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.13.1/js/bootstrap-select.min.js"></script>
        
    </jsp:attribute>

    <jsp:attribute name="menu">
        <div class="menuitem">
            <a href="<c:url value="/app/dashboard/"/>">Dashboard</a>
        </div>
        
        <div class="menuitem">
            <a href="<c:url value="/app/medias/list/"/>">Liste</a>
        </div>
    </jsp:attribute>

    <jsp:attribute name="content">
        <form method="post" class="stacked">
            <div class="column">
                <c:if test="${edit}">
                    <div class="menuitem">
                        <a href="<c:url value="/app/medias/season/new${media_form.values['media_id'][0]}/"/>">Staffel hinzufügen</a>
                    </div>
                </c:if>
                <%-- CSRF-Token --%>
                <input type="hidden" name="csrf_token" value="${csrf_token}">

                <%-- Eingabefelder --%>
                <label for="media_owner">Eigentümer:</label>
                <div class="side-by-side">
                    <input type="text" name="media_owner" value="${media_form.values["media_owner"][0]}" readonly="readonly">
                </div>

                <label for="media_genre">Genre:</label>
                <div class="side-by-side">
                    
                    <select   class="selectpicker" multiple data-live-search="false" name="media_genre">
                        <!--<option value="">Kein Genre</option>-->

                        <c:forEach items="${genres}" var="genre">
                            <option value="${genre.id}" ${media_form.values["media_genre"][0] == genre.id.toString() ? 'selected' : ''}>
                                <c:out value="${genre.name}" />
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <label for="media_release_date">
                    Veröffentlicht am:
                    <span class="required">*</span>
                </label>
                <div class="side-by-side">
                    <input type="text" name="media_release_date" value="${media_form.values["media_relesase_date"][0]}">
                    
                </div>

                <label for="media_status">
                    Status:
                    <span class="required">*</span>
                </label>
                <div class="side-by-side margin">
                    <select name="media_status">
                        <c:forEach items="${statuses}" var="status">
                            <option value="${status}" ${media_form.values["media_status"][0] == status ? 'selected' : ''}>
                                <c:out value="${status.label}"/>
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <label for="media_title">
                    Titel:
                    <span class="required">*</span>
                </label>
                <div class="side-by-side">
                    <input type="text" name="media_title" value="${media_form.values["media_title"][0]}">
                </div>

                <label for="media_description">
                    Beschreibung:
                </label>
                <div class="side-by-side">
                    <textarea name="media_description"><c:out value="${media_form.values['media_description'][0]}"/></textarea>
                </div>

                <%-- Button zum Abschicken --%>
                <div class="side-by-side">
                    <button class="icon-pencil" type="submit" name="action" value="save">
                        Sichern
                    </button>

                    <c:if test="${edit}">
                        <button class="icon-trash" type="submit" name="action" value="delete">
                            Löschen
                        </button>
                    </c:if>
                </div>
            </div>
 <%-- Keine Serienspezielle Elemente--%>
                

            <%-- Fehlermeldungen --%>
            <c:if test="${!empty media_form.errors}">
                <ul class="errors">
                    <c:forEach items="${media_form.errors}" var="error">
                        <li>${error}</li>
                    </c:forEach>
                </ul>
            </c:if>
        </form>
    </jsp:attribute>
</template:base>
