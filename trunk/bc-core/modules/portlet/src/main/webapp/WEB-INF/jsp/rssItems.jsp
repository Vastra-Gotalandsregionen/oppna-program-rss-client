<%--

    Copyright 2010 Västra Götalandsregionen

      This library is free software; you can redistribute it and/or modify
      it under the terms of version 2.1 of the GNU Lesser General Public
      License as published by the Free Software Foundation.

      This library is distributed in the hope that it will be useful,
      but WITHOUT ANY WARRANTY; without even the implied warranty of
      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
      GNU Lesser General Public License for more details.

      You should have received a copy of the GNU Lesser General Public
      License along with this library; if not, write to the
      Free Software Foundation, Inc., 59 Temple Place, Suite 330,
      Boston, MA 02111-1307  USA


--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<portlet:defineObjects/>

<fmt:setBundle basename="se.vgregion.portal.rss.client.rssClient"/>

<c:forEach items="${listOfRssEntriesLists}" varStatus="index" var="rssEntries">
    <div id="tab-${index}">
        <ul id="listNews" class="vgr-list-view vgr-list-view-condensed vgr-list-view-news">
            <c:set var="maxLengthContent" value="50"/>

            <c:forEach items="${rssEntries}" var="item" varStatus="status">

                <c:set var="listItemCssClass" value="" scope="page" />

                <c:if test="${(status.index)%2 ne 0}">
                    <c:set var="listItemCssClass" value="${listItemCssClass} vgr-list-view-item-odd" scope="page" />
                </c:if>

                <c:if test="${status.last}">
                    <c:set var="listItemCssClass" value="${listItemCssClass} vgr-list-view-item-last" scope="page" />
                </c:if>

                <li class="vgr-list-view-item ${listItemCssClass}">

                    <div class="hd clearfix">
                        <h3 class="title">
                            <a href="${item.link}" target="_BLANK">
                                ${item.title}
                            </a>
                        </h3>
                    </div>
                    <div class="bd description">

                        <c:if test="${not empty item.publishedDate}">
                            <div class="meta">
                                <span class="date"><fmt:formatDate value="${item.publishedDate}" type="both" pattern="yyyy-MM-dd"/></span>
                            </div>
                        </c:if>

                        <c:choose>
                            <c:when test="${fn:length(item.contentsString) <= maxLengthContent }">
                                <c:out value="${item.contentsString}" escapeXml="false"/>
                            </c:when>
                            <c:otherwise>
                                <c:out value="${fn:substring(item.contentsString, 0, maxLengthContent)}" escapeXml="false"/>...
                            </c:otherwise>
                        </c:choose>

                    </div>
                    <div class="ft">
                    </div>
                </li>
            </c:forEach>
        </ul>
    </div>
</c:forEach>
