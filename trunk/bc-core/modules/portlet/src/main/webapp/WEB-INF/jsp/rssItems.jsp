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

<portlet:actionURL escapeXml="false" var="sortByDate" name="sortByDate" />
<portlet:actionURL escapeXml="false" var="groupBySource" name="groupBySource" />
<portlet:resourceURL id="sortByDate" escapeXml="false" var="sortByDateResource" />
<portlet:resourceURL id="groupBySource" escapeXml="false" var="groupBySourceResource" />

<fmt:setBundle basename="se.vgregion.portal.rss.client.rssClient"/>

<script>
  jQuery(document).ready(function() {
    jQuery(".news-excerpt").show;
    jQuery(".news-content").hide;

	jQuery('#group-by-source').click(function() {
      updateSorting("${groupBySourceResource}");
      return false;
    });
    
    jQuery('#sort-by-date').click(function() {
      updateSorting("${sortByDateResource}");
      return false;
    });
    
    jQuery('.read-more, .read-less').click(function() {
      var li = jQuery(this).parents("li");
      li.find(".news-excerpt").toggle("medium");
      li.find(".news-content").toggle("medium");
      return false;
    });
  });
  
  function updateSorting(sortingUrl) {
    jQuery.ajax({
      url: sortingUrl,
      success: function(data) {
        jQuery("#rss-item-container").html(data);
       }
    });
  }
</script>

<div id="rss-item-container">
  <c:if test="${!empty rssEntries}">
    <div class="sort-box">
    <fmt:message key="sorton"/>: 
    <c:choose>
      <c:when test="${sort_order == 'SORT_BY_NAME'}">
          <span id="sort-by-date-selected"><a id="group-by-source" href="${groupBySource}"><fmt:message key="source"/></a> | <strong><fmt:message key="date"/></strong></span>
      </c:when>
      <c:when test="${sort_order == 'GROUP_BY_SOURCE'}">
          <span id="group-by-source-selected"><strong><fmt:message key="source"/></strong> | <a id="sort-by-date" href="${sortByDate}"><fmt:message key="date"/></a></span>
      </c:when>
      <c:otherwise>
          <span id="sort-by-date-selected"><a id="group-by-source" href="${groupBySource}"><fmt:message key="source"/></a> | <strong><fmt:message key="date"/></strong></span>
      </c:otherwise>
    </c:choose>
    </div>
  </c:if>
  <ul id="list-news" class="list-news">
    <c:forEach items="${rssEntries}" var="item" varStatus="status">
      <li class="news-item"><span class="news-source"><c:out value="${item.feedTitle}" escapeXml="true" /></span>
      <span class="news-date"><c:if test="${!empty item.publishedDate}">[</c:if><fmt:formatDate value="${item.publishedDate}" type="both"
        pattern="yyyy-MM-dd hh:mm" /><c:if test="${!empty item.publishedDate}">]</c:if></span> <a class="news-title" href="${item.link}">${item.title}</a>
      <div class="news-excerpt">
        <p class="news-excerpt">
          <c:out value="${item.shortExcerpt}" escapeXml="false"/><c:if test="${fn:length(item.contentsString) gt 200 or (fn:length(item.contentsString) ne fn:length(item.shortExcerpt))}"><a class="read-more" href="#">&raquo;</a></c:if>
        </p>
      </div>
      <div class="news-content"><c:out value="${item.contentsString}"  escapeXml="false" />
        <a href="#" class="read-less"><fmt:message key="close"/></a>
      </div>
      </li>
    </c:forEach>
  </ul>
</div>
