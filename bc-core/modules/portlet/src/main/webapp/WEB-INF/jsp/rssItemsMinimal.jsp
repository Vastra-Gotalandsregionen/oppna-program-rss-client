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

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<portlet:defineObjects/>

<script src="${pageContext.request.contextPath}/script/jquery.excerpt.js"></script>

<fmt:setBundle basename="se.vgregion.portal.rss.client.rssClient"/>

<script>
  jQuery(document).ready(function() {
      jQuery("#p_p_id<portlet:namespace/> .news-title-minimal").excerpt({lines: '${portletPreferencesValues.numberOfExcerptRows[0]}', end: '...'});
  });
  
</script>

<div id="rss-item-container" class="rss-item-container-minimal">
  <ul id="list-news" class="list-news-minimal">
    <c:forEach items="${rssEntries}" var="item" varStatus="status">
      <c:choose>
        <c:when test="${not empty portletPreferencesValues.rssStandardClientPortletLink[0]}">
          <li class="news-item-minimal" onClick="location.href='${portletPreferencesValues.rssStandardClientPortletLink[0]}&selectedRssItemTitle=${item.title}#${item.title}'">
        </c:when>
        <c:otherwise>
          <li class="news-item-minimal">
        </c:otherwise>
      </c:choose>
        <div class="news-title-minimal" title="(<fmt:formatDate value="${item.publishedDate}" type="both" pattern="HH:mm" />) ${item.feedTitle}">
          <c:out value="${item.title}" escapeXml="false"/>
        </div>
      </li>
    </c:forEach>
  </ul>
</div>
