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

<div class="rss-tabs-wrap vgr-tabs" id="<portlet:namespace />rssTabsWrap">
    <ul class="rss-tabs-nav aui-tabview-list aui-widget-hd" id="<portlet:namespace />rssTabsList">
        <c:forEach items="${rssFeedTitles}" varStatus="index" var="rssFeedTitle">
            <li class="aui-tab">
                <a class="aui-tab-label" href="#tab-${index}">${rssFeedTitle}</a>
            </li>
        </c:forEach>
    </ul>

    <div class="vgr-tabs-content-wrap vgr-list-view-wrap aui-tabview-content-item rss-wrap" id="<portlet:namespace />rssTabsContent">
    	<jsp:directive.include file="rssItems.jsp" />
    </div>
</div>

<script type="text/javascript">

AUI().use(
        'aui-tabview',
        function(A) {
            new A.TabView(
                    {
                        cssClass: 'vgr-tabs',
                        srcNode: '#<portlet:namespace />rssTabsWrap'
                    }
            ).render();
        }
);


</script>