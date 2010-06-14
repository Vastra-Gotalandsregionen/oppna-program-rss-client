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

<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<fmt:setBundle basename="se.vgregion.portal.rss.client.rssClient"/>

<portlet:actionURL var="save" escapeXml="false"/>
<style>
<!--
    @import url("${pageContext.request.contextPath}/style/style.css");
-->
</style>

<form:form method="POST" action="${save}" commandName="preferences">
  <fieldset>
    <legend><fmt:message key="settings"/></legend>
    <ol>
      <li>
        <label for="maximum-items"><fmt:message key="numberofitems" />:</label>
        <form:input id="maxium-items" path="numberOfItems"/>
      </li>
      <li>
        <label for="rss-feed-links"><fmt:message key="feeds" />:</label> 
        <form:textarea id="rss-feed-links" path="rssFeedLinks" cols="100" rows="5"/>
      </li>
    </ol>
  </fieldset>
  <fieldset class="submit">
    <input type="submit" value=<fmt:message key="save"/> />
  </fieldset>
</form:form>