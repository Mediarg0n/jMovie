<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib tagdir="/WEB-INF/tags/templates" prefix="template"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<template:base>
    <jsp:attribute name="title">
        <c:choose>
            <c:when test="${edit}">
                Episode bearbeiten
            </c:when>
            <c:otherwise>
                Episode anlegen
            </c:otherwise>
        </c:choose>
    </jsp:attribute>

    <jsp:attribute name="head">
        
        <link rel="stylesheet" href="<c:url value="/css/media_edit.css"/>" />
        
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
                <%-- CSRF-Token --%>
                <input type="hidden" name="csrf_token" value="${csrf_token}">

                <%-- Eingabefelder --%>
                <label for="episode_season">Serie,Season </label>
                <div class="side-by-side">
                    <input type="text" name="episode_season" value="${episode_form.values["episode_season"][0]}" readonly="readonly">
                </div>

                <label for="episode_nr">
                    Nr:
                    <span class="required">*</span>
                </label>
                <div class="side-by-side">
                    <input type="number" name="episode_nr" value="${episode_form.values["episode_nr"][0]}">
                </div>
                
                <label for="episode_title">
                    Titel:
                    <span class="required">*</span>
                </label>
                <div class="side-by-side">
                    <input type="text" name="episode_title" value="${episode_form.values["episode_title"][0]}">
                </div>
                
                <label for="episode_length">Länge:</label>
                <div class="side-by-side">
                    <input type="number" name="episode_length" value="${episode_form.values["episode_length"][0]}">
                </div>

                <label for="episode_release_date">
                    Veröffentlicht am:
                    <span class="required">*</span>
                </label>
                <div class="side-by-side">
                    <input type="text" name="episode_release_date" value="${episode_form.values["episode_release_date"][0]}">
                </div>

                <label for="episode_status">
                    Status:
                    <span class="required">*</span>
                </label>
                <div class="side-by-side margin">
                    <select name="episode_status">
                        <c:forEach items="${statuses}" var="status">
                            <option value="${status}" ${episode_form.values["episode_status"][0] == status ? 'selected' : ''}>
                                <c:out value="${status.label}"/>
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <label for="episode_description">
                    Beschreibung:
                </label>
                <div class="side-by-side">
                    <textarea name="episode_description"><c:out value="${episode_form.values['episode_description'][0]}"/></textarea>
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
                
            <%-- Fehlermeldungen --%>
            <c:if test="${!empty episode_form.errors}">
                <ul class="errors">
                    <c:forEach items="${episode_form.errors}" var="error">
                        <li>${error}</li>
                    </c:forEach>
                </ul>
            </c:if>
            
            
        </form>
            
           
    </jsp:attribute>
</template:base>

