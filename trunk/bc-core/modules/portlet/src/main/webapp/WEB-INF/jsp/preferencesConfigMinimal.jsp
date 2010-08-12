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

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<fmt:setBundle basename="se.vgregion.portal.rss.client.rssClient"/>

<portlet:actionURL var="save" escapeXml="false"/>
<portlet:actionURL var="clearFeedBlackList" name="clearFeedBlackList"/>
<portlet:actionURL var="removeFromFeedBlackList" name="removeFromFeedBlackList"/>

<style>
<!--
    @import url("${pageContext.request.contextPath}/style/style.css");
-->
</style>
  
<form:form id="preferencesForm" method="POST" action="${save}" modelAttribute="portletPreferencesWrapperBean" htmlEscape="false">
  <fieldset>
    <legend><fmt:message key="settings"/></legend>
    <ol>
      <li>
        <label for="maximum-items"><fmt:message key="numberofitems" />:</label>
        <form:input id="maximum-items" path="numberOfItems" cssErrorClass="validation-error"/> <form:errors cssClass="validation-error-message" path="numberOfItems"/>
      </li>
      <li>
        <label for="excerpt-rows"><fmt:message key="numberofexcerptrows" />:</label>
        <form:input id="excerpt-rows" path="numberOfExcerptRows" cssErrorClass="validation-error"/> <form:errors cssClass="validation-error-message" path="numberOfExcerptRows"/>
      </li>
      <li>
        <label for="portlet-link"><fmt:message key="rssstandardslientportletlink" />:</label>
        <form:input id="portlet-link" size="100" path="rssStandardClientPortletLink" cssErrorClass="validation-error"/> <form:errors cssClass="validation-error-message" path="rssStandardClientPortletLink"/>
      </li>
      <li>
        <label for="rss-feed-links"><fmt:message key="feeds" />:</label> 
        <form:textarea id="rss-feed-links" path="rssFeedLinks" cols="100" rows="5" cssErrorClass="validation-error"/>
        <form:errors path="rssFeedLinks" cssClass="validation-error-message validation-error-message-feed-urls"/>
      </li>
    </ol>
  </fieldset>
  <fieldset class="submit">
    <input type="submit" value=<fmt:message key="save"/> />
  </fieldset>
  
  <c:if test="${not empty portletPreferencesWrapperBean.feedBlackList}">
    <fieldset>
      <legend><fmt:message key="blacklistedfeeds"/></legend>
      <ol>
        <c:forEach items="${portletPreferencesWrapperBean.feedBlackList}" var="feedLink">
          <li>
            <c:out value="${feedLink}"/>&nbsp;&nbsp;<input type="button" value="<fmt:message key="removefromblacklist"/>" onclick="location.href='${removeFromFeedBlackList}&amp;feedLink=<c:out value="${feedLink}"/>';"/>
          </li>
        </c:forEach>
      </ol>
    </fieldset>
    <fieldset class="submit">
      <input type="button" value="<fmt:message key="clearblacklist"/>" onclick="location.href='${clearFeedBlackList}';"/>
    </fieldset>
  </c:if>
</form:form>

