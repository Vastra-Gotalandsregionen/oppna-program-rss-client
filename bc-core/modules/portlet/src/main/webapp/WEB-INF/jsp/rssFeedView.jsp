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
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix='fn' uri='http://java.sun.com/jsp/jstl/functions'%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!--CSS file (default YUI Sam Skin) -->
<link type="text/css" rel="stylesheet" href="/vgr-theme/javascript/yui-2.8.0r4/assets/skins/sam/datatable.css" />

<!-- Dependencies -->
<script type="text/javascript" src="/vgr-theme/javascript/yui-2.8.0r4/yahoo-dom-event.js"></script>
<script type="text/javascript" src="/vgr-theme/javascript/yui-2.8.0r4/element-min.js"></script>
<script type="text/javascript" src="/vgr-theme/javascript/yui-2.8.0r4/datasource-min.js"></script>
<!-- OPTIONAL: Drag Drop (enables resizeable or reorderable columns) -->
<script type="text/javascript" src="/vgr-theme/javascript/yui-2.8.0r4/dragdrop-min.js"></script>
<script type="text/javascript" src="/vgr-theme/javascript/yui-2.8.0r4/datatable-min.js"></script>

<portlet:actionURL escapeXml="false" var="sortByDate" name="sortByDate" />
<portlet:actionURL escapeXml="false" var="groupBySource" name="groupBySource" />
<portlet:resourceURL id="sortByDate" escapeXml="false" var="sortByDateResource" />
<portlet:resourceURL id="sortByDate" escapeXml="false" var="groupBySourceResource" />

<style>
.list-news .news-item {
  list-style: none;
  padding: 1em;
  margin: 1em;
  border: 1px dotted #CCC;
}

.news-source {
  display: block;
  font-style: italic;
}

.news-date {
  color: grey;
}

.news-title {
  text-decoration: none;
  font-weight: bold;
  font-size: 1.2em;
}

.news-excerpt {
  color: #33332A;
}

.news-excerpt .read-more {
  display: block;
}

.news-content {
  display: none;
}
</style>

<script>

jQuery(document).ready(function() {
  jQuery('#sort-by-date').click(function() {
    //alert("Hej med Jquery");
    alert("url: ${groupBySourceResource}");
    jQuery.ajax({
            method: "get",
            url: "${groupBySourceResource}",
            data: "",
            beforeSend: function() { alert("before"); },
            complete: function() {  alert("complete"); },
            success: function(result) { alert("Update dom with new fragment: " + result ); }
        });
    return false;
  });
});

function sortByDate() {

}

function groupBySource() {

}

</script>

<div id="module-news" class="module">
<div id="module-content">

<div class="sort-box"><span id="sort-by">Sortera efter: </span> <a id="sort-by-date" href="${sortByDate}">Datum</a>
| <a id="group-by-source" href="${groupBySource}" onclick="groupBySource(); return false;">Källa</a></div>

<ul class="list-news">
  <c:forEach items="${rssEntries}" var="item" varStatus="status">
    <li class="news-item"><span class="news-source"><c:out value="${item.feedTitle}" escapeXml="true" /></span>
    <span class="news-date">[<fmt:formatDate value="${item.publishedDate}" type="both"
      pattern="yyyy-MM-dd hh:mm" />]</span> <a class="news-title" href="${item.link}">${item.title}</a>
    <p class="news-excerpt" id="${item.id}-excerpt">${item.shortExcerpt} <a class="read-more" href="#"
      onclick='jQuery("#${item.id}-content").show("slow"); jQuery("#${item.id}-excerpt").hide("slow");'>Läs
    mer</a></p>
    <div class="news-content" id="${item.id}-content">${item.contentsString} <a href="#"
      onclick='jQuery("#${item.id}-content").hide("slow"); jQuery("#${item.id}-excerpt").show("slow");'>Stäng</a></div>
    </li>
  </c:forEach>
</ul>
</div>
</div>
