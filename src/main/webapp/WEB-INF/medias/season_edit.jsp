<%-- 
    Document   : season_edit
    Created on : 24.03.2019, 20:36:59
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
                Staffel bearbeiten
            </c:when>
            <c:otherwise>
                Staffel anlegen
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
            <a href="<c:url value="/app/tasks/list/"/>">Liste</a>
        </div>
    </jsp:attribute>

    <jsp:attribute name="content">
        <form method="post" class="stacked">
            <div class="column">
                <%-- CSRF-Token --%>
                <input type="hidden" name="csrf_token" value="${csrf_token}">

                <%-- Eingabefelder --%>
                <label for="season_serie">Serie:</label>
                <div class="side-by-side">
                    <input type="text" name="season_serie" value="${season_form.values["season_serie"][0]}" readonly="readonly">
                </div>

                <label for="season_release_date">
                    Veröffentlicht am:
                    <span class="required">*</span>
                </label>
                <div class="side-by-side">
                    <input type="date" name="season_release_date" value="${season_form.values["season_relesase_date"][0]}">
                    
                </div>

                <label for="season_status">
                    Status:
                    <span class="required">*</span>
                </label>
                <div class="side-by-side margin">
                    <select name="season_status">
                        <c:forEach items="${statuses}" var="status">
                            <option value="${status}" ${season_form.values["season_status"][0] == status ? 'selected' : ''}>
                                <c:out value="${status.label}"/>
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <label for="season_nr">
                    Nr:
                    <span class="required">*</span>
                </label>
                <div class="side-by-side">
                    <input type="number" name="season_nr" value="${season_form.values["season_nr"][0]}">
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
            <c:if test="${!empty season_form.errors}">
                <ul class="errors">
                    <c:forEach items="${season_form.errors}" var="error">
                        <li>${error}</li>
                    </c:forEach>
                </ul>
            </c:if>
        </form>
    </jsp:attribute>
</template:base>
