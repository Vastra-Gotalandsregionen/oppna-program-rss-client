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

<fmt:setBundle basename="se.vgregion.portal.rss.client.rssClient"/>

<div class="blogs-listing content-box">

	<h2>${feedTitle}</h2>
	
	<div class="content-box-bd">
	
		<div class="news-items">
			<c:forEach items="${rssEntries}" var="item" varStatus="status">
			    <div class="entry-item">
			        <c:set var="link" scope="page">
			            <c:choose>
			                <c:when test="${not empty portletPreferencesValues.rssStandardClientPortletLink[0]}">
			                    ${portletPreferencesValues.rssStandardClientPortletLink[0]}&
			                    selectedRssItemTitle=${item.link}#${item.link}
			                </c:when>
			                <c:otherwise>
			                    ${item.link}
			                </c:otherwise>
			            </c:choose>
			        </c:set>
			        <a href="${item.link}" target="_blank">
			            <div class="entry-date">
			                <div class="entry-date-inner">
			                    <div class="entry-date-month">
			                        <fmt:formatDate value="${item.publishedDate}" type="date" dateStyle="medium" pattern="MMM"/>
			                    </div>
			                    <div class="entry-date-day">
			                      <fmt:formatDate value="${item.publishedDate}" type="date" pattern="d"/>
			                    </div>
			                </div>
			            </div>
			            <div class="entry-content news-title-minimal">
			                <c:out value="${item.title}" escapeXml="false"/>
			            </div>
			            <div class="time-interval">
							<!--
							<span class="entry-content news-title-minimal start-date">
								<c:out value="${item.startDate}" escapeXml="false"/>
							</span>
							-->
							<span class="news-title-minimal start-time">
								<c:out value="${item.startTime}" escapeXml="false"/>
							</span>
							<span> - </span>
							<!--
							<div class="entry-content news-title-minimal end-date">
								<c:out value="${item.endDate}" escapeXml="false"/>
							</div>
							-->
							<span class="news-title-minimal end-time">
								<c:out value="${item.endTime}" escapeXml="false"/>
							</span>
			            </div>
                        <div class="entry-content news-title-minimal location">
			                <c:out value="${item.location}" escapeXml="false"/>
			            </div>
			        </a>
			    </div>
			</c:forEach>
		</div>
	
	</div>

</div>