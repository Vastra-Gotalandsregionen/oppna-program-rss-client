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

<portlet:actionURL escapeXml="false" var="sortByDate" name="sortByDate" />
<portlet:actionURL escapeXml="false" var="groupBySource" name="groupBySource" />
<portlet:resourceURL id="sortByDate" escapeXml="false" var="sortByDateResource" />
<portlet:resourceURL id="groupBySource" escapeXml="false" var="groupBySourceResource" />
<script>
  jQuery(document).ready(function() {
    jQuery('#group-by-source').click(function() {
        updateSorting("${groupBySourceResource}");
        return false;
    });
  });
  jQuery(document).ready(function() {
    jQuery('#sort-by-date').click(function() {
        updateSorting("${sortByDateResource}");
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
  <div class="sort-box">
    <c:if test="${sort_order != 'GROUP_BY_SOURCE'}">
      <span id="sort-by-date-selected"><a id="group-by-source" href="${groupBySource}">Källa</a> | <strong>Datum</strong></span>
    </c:if>
    <c:if test="${sort_order == 'GROUP_BY_SOURCE'}">   
      <span id="group-by-source-selected"><strong>Källa</strong> | <a id="sort-by-date" href="${sortByDate}">Datum</a></span>
    </c:if>
  </div>
  <ul class="list-news">
    <c:forEach items="${rssEntries}" var="item" varStatus="status">
      <li class="news-item"><span class="news-source"><c:out value="${item.feedTitle}" escapeXml="true" /></span>
      <span class="news-date">[<fmt:formatDate value="${item.publishedDate}" type="both"
        pattern="yyyy-MM-dd hh:mm" />]</span> <a class="news-title" href="${item.link}">${item.title}</a>
      <p class="news-excerpt" id="${item.id}-excerpt">${item.shortExcerpt} <a class="read-more" href="#"
        onclick='jQuery("#${item.id}-content").show("medium"); jQuery("#${item.id}-excerpt").hide("medium"); return false;'>Läs mer</a></p>
      <div class="news-content" id="${item.id}-content">${item.contentsString} <a href="#"
        onclick='jQuery("#${item.id}-content").hide("medium"); jQuery("#${item.id}-excerpt").show("medium"); return false;'>Stäng</a></div>
      </li>
    </c:forEach>
  </ul>
</div>
