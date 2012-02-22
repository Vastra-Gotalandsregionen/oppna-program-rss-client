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

<portlet:actionURL escapeXml="false" var="sortByDate" name="sortByDate"/>
<portlet:actionURL escapeXml="false" var="groupBySource" name="groupBySource"/>
<portlet:resourceURL id="sortByDate" escapeXml="false" var="sortByDateResource"/>
<portlet:resourceURL id="groupBySource" escapeXml="false" var="groupBySourceResource"/>

<fmt:setBundle basename="se.vgregion.portal.rss.client.rssClient"/>

<div class="aui-tabview-content-item">
	<ul id="list-news" class="list-news">
		<c:forEach items="${rssEntries}" var="item" varStatus="status">
		
			<c:set var="hasDateCssClass" value="" scope="page" />
			<c:if test="${empty item.publishedDate}">
				<c:set var="hasDateCssClass" value="news-item-no-date" scope="page" />
			</c:if>
		
			<li class="news-item clearfix ${hasDateCssClass}" id="${item.link}">
				<span class="news-date">
					<c:choose>
						<c:when test="${not empty item.publishedDate}">
							<fmt:formatDate value="${item.publishedDate}" type="both" pattern="yyyy-MM-dd"/>
						</c:when>
						<c:otherwise>
							&nbsp;
						</c:otherwise>
					</c:choose>
				</span>
				<span class="news-block">
					<a class="news-title" href="${item.link}" target="_BLANK">${item.title}</a>
					<div class="news-content aui-helper-hidden">
						<c:out value="${item.contentsString}" escapeXml="false"/>
						<div class="news-actions">
							<a class="source-link" href="${item.link}" target="_BLANK"><fmt:message key="goToSource"/></a>
						</div>
					</div>
				</span>
			</li>
		</c:forEach>
	</ul>
	<a href="${rssFeedLink}" target="_blank"><fmt:message key="readmore"/>: ${rssFeedTitle}</a>
</div>

<%-- 
<div id="blockMe">
    <div id="rss-item-container" style="min-height: 100px;">
        <c:if test="${!empty rssEntries}">
            <div class="sort-box">
                <fmt:message key="sorton"/>:
                <c:choose>
                    <c:when test="${sort_order == 'SORT_BY_NAME'}">
                        <span id="sort-by-date-selected"><a id="group-by-source" href="#"><fmt:message
                                key="source"/></a> | <strong><fmt:message key="date"/></strong></span>
                    </c:when>
                    <c:when test="${sort_order == 'GROUP_BY_SOURCE'}">
                        <span id="group-by-source-selected"><strong><fmt:message key="source"/></strong> | <a
                                id="sort-by-date" href="#"><fmt:message key="date"/></a></span>
                    </c:when>
                    <c:otherwise>
                        <span id="sort-by-date-selected"><a id="group-by-source" href="#"><fmt:message
                                key="source"/></a> | <strong><fmt:message key="date"/></strong></span>
                    </c:otherwise>
                </c:choose>
            </div>
        </c:if>

        <ul id="list-news" class="list-news">
            <c:forEach items="${rssEntries}" var="item" varStatus="status">
                <li class="news-item" id="${item.link}">
                    <span class="news-source">
                      <c:out value="${item.feedTitle}" escapeXml="true"/>
                    </span>
                    <span class="news-date">
                      <c:if test="${!empty item.publishedDate}">
                          [<fmt:formatDate value="${item.publishedDate}" type="both" pattern="yyyy-MM-dd"/>
                          <span class="news-time">&nbsp;<fmt:formatDate value="${item.publishedDate}" type="both"
                                                                        pattern="HH:mm"/></span>]
                      </c:if>
                    </span>
                    <a class="news-title" href="${item.link}">${item.title}</a>&nbsp;
                    <a class="source-link" href="${item.link}"></a>

                    <div class="news-excerpt">
                            <p class="news-excerpt">
                            <c:out value="${item.excerpt}" escapeXml="false"/>
                            </p>
                    </div>
                    <div class="news-content" style="display: none;">
                        <c:out value="${item.contentsString}" escapeXml="false"/>
                        <div class="news-actions">
                            <a class="source-link" href="${item.link}"><fmt:message key="goToSource"/></a>
                            <a href="#" class="read-less"><fmt:message key="close"/></a>
                        </div>
                    </div>
                </li>
            </c:forEach>
        </ul>
    </div>
    <a href="${rssFeedLink}" target="_blank"><fmt:message key="readmore"/>: ${rssFeedTitle}</a>
</div>
--%>

<%-- 
<div id="blockDisplayMessage" style="display:none"> 
  <h1>&nbsp;Laddar källa...&nbsp;</h1> 
</div> 
--%>

<%-- 

<c:if test="${empty sort_order and empty selectedRssItemTitle}">
    <script>
        //No sort order and no pre-selection, sort by date to fetch content (no fetch on default load, this to avoid "page lock")
        //updateSorting('${sortByDateResource}', '<portlet:namespace/>');
    </script>
</c:if>


<script type="text/javascript">

    AUI().ready(
            'vgr-rss-client',
            function(A) {
                var rssClient = new A.VgrRssClient({
                    portletNamespace: '<portlet:namespace/>',
                    selectedRssItemTitle: '${selectedRssItemTitle}',
                    urlGroupBySource: '${groupBySourceResource}',
                    urlSortByDate: '${sortByDateResource}'
                }).render();
            }
    );
</script>

--%>