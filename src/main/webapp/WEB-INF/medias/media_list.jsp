<%-- 
    Document   : media_list
    Created on : 24.03.2019, 17:34:23
    Author     : bpall
--%>
<%@page import="com.sun.tools.javac.comp.Todo"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib tagdir="/WEB-INF/tags/templates" prefix="template"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<template:base>
    <jsp:attribute name="title">
        Liste der Medien
    </jsp:attribute>

    <jsp:attribute name="head">
        <link rel="stylesheet" href="<c:url value="/css/media_list.css"/>" />
    </jsp:attribute>

    <jsp:attribute name="menu">
        <div class="menuitem">
            <a href="<c:url value="/app/dashboard/"/>">Dashboard</a>
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
        <%-- Suchfilter --%>
        <form method="GET" class="horizontal" id="search">
            <input type="text" name="search_title" value="${param.search_text}" placeholder="Title"/>

            <select name="search_genre">
                <option value="">Alle Genre</option>

                <c:forEach items="${genres}" var="genre">
                    <option value="${genre.id}" ${param.search_genre == genre.id ? 'selected' : ''}>
                        <c:out value="${genre.name}" />
                    </option>
                </c:forEach>
            </select>

            <select name="search_status">
                <option value="">Alle Stati</option>

                <c:forEach items="${statuses}" var="status">
                    <option value="${status}" ${param.search_status == status ? 'selected' : ''}>
                        <c:out value="${status.label}"/>
                    </option>
                </c:forEach>
            </select>

            <button class="icon-search" type="submit">
                Suchen
            </button>
        </form>

        <%-- Gefundene Aufgaben --%>
        <c:choose>
            <c:when test="${empty medias}">
                <p>
                    Es wurden keine Medien gefunden. 🐈
                </p>
            </c:when>
            <c:otherwise>
                <jsp:useBean id="utils" class="dhbwka.wwi.vertsys.javaee.jmovie.common.web.WebUtils"/>
                
                <table>
                    <thead>
                        <tr>
                            <th>Typ</th>
                            <th>Title</th>
                            <th>Genre</th>
                            <th>Eigentümer</th>
                            <th>Status</th>
                            <th>Veröffentlicht am</th>
                        </tr>
                    </thead>
                    <c:forEach items="${medias}" var="media">
                        <tr>
                            
                        <c:if test="${media['class'].name == 'dhbwka.wwi.vertsys.javaee.jmovie.media.jpa.Movie'}">
                            <td> 
                                <c:out value="Film"/>
                            </td>
                            
                            <td>
                                <a href="<c:url value="/app/medias/movie/${media.id}/"/>">
                                    <c:out value="${media.title}"/>
                                </a> 
                            </td>
                        </c:if>
                            
                        <c:if test="${media['class'].name == 'dhbwka.wwi.vertsys.javaee.jmovie.media.jpa.Serie'}">
                            <td>
                                <c:out value="Serie"/>
                            </td>
                            
                            <td>
                                <a href="<c:url value="/app/medias/serie/${media.id}/"/>">
                                    <c:out value="${media.title}"/>
                                </a>
                            </td>
                        </c:if>
                            
                            <td style="text-overflow: ellipsis; max-width: 10em">
                              
                                <c:out value="${media.genres[0].name}"/>
                                
                                <c:if test="${fn:length(media.genres) > 1}">
                                    <c:forEach items="${media.genres}" var="genre" begin="1">
                                       <c:out value=", ${genre.name}"/>
                                    </c:forEach> 
                                </c:if>
                            
                            </td>
                            <td>
                                <c:out value="${media.owner.username}"/>
                            </td>
                            <td>
                                <c:out value="${media.status.label}"/>
                            </td>
                            <td>
                                <c:out value="${utils.formatDate(media.releaseDate)}"/>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </c:otherwise>
        </c:choose>
    </jsp:attribute>
</template:base>
