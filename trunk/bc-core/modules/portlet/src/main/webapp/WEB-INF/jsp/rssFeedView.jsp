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
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<portlet:renderURL var="feedURL1">
  <portlet:param name="feed" value="1" />
</portlet:renderURL>
<portlet:renderURL var="feedURL2">
  <portlet:param name="feed" value="2" />
</portlet:renderURL>
<portlet:renderURL var="feedURL3">
  <portlet:param name="feed" value="3" />
</portlet:renderURL>
<portlet:renderURL var="feedURL4">
  <portlet:param name="feed" value="4" />
</portlet:renderURL>

<div class="rss-tabs-wrap">
    <ul class="rss-tabs-nav">
        <c:if test="${isTab1Active}">        
            <li>
              <a href="${feedURL1}">${rssFeedTitle1}</a>
            </li>
        </c:if>
        <c:if test="${isTab2Active}">        
            <li>
              <a href="${feedURL2}">${rssFeedTitle2}</a>
            </li>
        </c:if>
        <c:if test="${isTab3Active}">        
            <li>
              <a href="${feedURL3}">${rssFeedTitle3}</a>
            </li>
        </c:if>
        <c:if test="${isTab4Active}">        
            <li>
              <a href="${feedURL4}">${rssFeedTitle4}</a>
            </li>
        </c:if>
    </<ul>
    <div class="rss-tabs-content-wrap">
        <jsp:directive.include file="rssItems.jsp" />
    </div>
</div>