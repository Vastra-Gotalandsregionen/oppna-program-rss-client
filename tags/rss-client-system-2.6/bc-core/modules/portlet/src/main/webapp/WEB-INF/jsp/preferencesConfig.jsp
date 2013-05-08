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

<fmt:setBundle basename="se.vgregion.portal.rss.client.rssClient" />

<portlet:actionURL var="save" name="save" escapeXml="false" />
<portlet:actionURL var="clearFeedBlackList" name="clearFeedBlackList" />
<portlet:actionURL var="removeFromFeedBlackList" name="removeFromFeedBlackList" />

<style>
<!--
@import url("${pageContext.request.contextPath}/style/style.css");
-->
</style>

<form:form id="preferencesForm" method="POST" action="${save}" modelAttribute="portletPreferencesWrapperBean"
  htmlEscape="false"
>
  <fieldset>
    <legend>
      <fmt:message key="settings" />
    </legend>
    <ol>
      <li>
        <label for="maximum-items"><fmt:message key="numberofitems1" />:</label> 
        <form:input id="maximum-items" path="numberOfItems1" cssErrorClass="validation-error"/> 
        <form:errors cssClass="validation-error-message" path="numberOfItems1" />
      </li>
      <li>
        <label for="rss-feed-title"><fmt:message key="rssfeedtitle1" />:</label> 
        <form:input id="rss-feed-title" path="rssFeedTitle1" cssErrorClass="validation-error"/> 
        <form:errors cssClass="validation-error-message" path="rssFeedTitle1" />
      </li>
      <li>
        <label for="rss-feed-links1"><fmt:message key="feed1" />:</label>        
        <form:textarea id="rss-feed-link1" path="rssFeedLink1" cols="100" rows="5" cssErrorClass="validation-error"/>
        <form:errors path="rssFeedLink1" cssClass="validation-error-message validation-error-message-feed-urls"/>
      </li>
    </ol>
    <ol>
      <li>
        <label for="maximum-items"><fmt:message key="numberofitems2" />:</label>
        <form:input id="maximum-items" path="numberOfItems2" cssErrorClass="validation-error"/>
        <form:errors cssClass="validation-error-message" path="numberOfItems2" />
      </li>
      <li>
        <label for="rss-feed-title"><fmt:message key="rssfeedtitle2" />:</label> 
        <form:input id="rss-feed-title" path="rssFeedTitle2" cssErrorClass="validation-error"/> 
        <form:errors cssClass="validation-error-message" path="rssFeedTitle2" />
      </li>
      <li>
        <label for="rss-feed-links2"><fmt:message key="feed2" />:</label>
        <form:textarea id="rss-feed-link2" path="rssFeedLink2" cols="100" rows="5" cssErrorClass="validation-error"/>
        <form:errors path="rssFeedLink2" cssClass="validation-error-message validation-error-message-feed-urls"
        />
      </li>
    </ol>
    <ol>
      <li>
        <label for="maximum-items"><fmt:message key="numberofitems3" />:</label>
        <form:input id="maximum-items" path="numberOfItems3" cssErrorClass="validation-error" />
        <form:errors cssClass="validation-error-message" path="numberOfItems3" />
      </li>
      <li>
        <label for="rss-feed-title"><fmt:message key="rssfeedtitle3" />:</label> 
        <form:input id="rss-feed-title" path="rssFeedTitle3" cssErrorClass="validation-error"/> 
        <form:errors cssClass="validation-error-message" path="rssFeedTitle3" />
      </li>
      <li>
        <label for="rss-feed-links3"><fmt:message key="feed3" />:</label>
        <form:textarea id="rss-feed-link3" path="rssFeedLink3" cols="100" rows="5" cssErrorClass="validation-error"/>
        <form:errors path="rssFeedLink3" cssClass="validation-error-message validation-error-message-feed-urls"
        />
      </li>
    </ol>
    <ol>
      <li>
        <label for="maximum-items"><fmt:message key="numberofitems4" />:</label>
        <form:input id="maximum-items" path="numberOfItems4" cssErrorClass="validation-error" />
        <form:errors cssClass="validation-error-message" path="numberOfItems4" />
      </li>
      <li>
        <label for="rss-feed-title"><fmt:message key="rssfeedtitle4" />:</label> 
        <form:input id="rss-feed-title" path="rssFeedTitle4" cssErrorClass="validation-error"/> 
        <form:errors cssClass="validation-error-message" path="rssFeedTitle4" />
      </li>
      <li>
        <label for="rss-feed-links4"><fmt:message key="feed4" />:</label>
        <form:textarea id="rss-feed-link4" path="rssFeedLink4" cols="100" rows="5" cssErrorClass="validation-error"/>
        <form:errors path="rssFeedLink4" cssClass="validation-error-message validation-error-message-feed-urls"/>
      </li>
    </ol>
  </fieldset>
  <fieldset class="submit">
    <input type="submit" value=<fmt:message key="save"/> />
  </fieldset>

  <c:if test="${not empty portletPreferencesWrapperBean.feedBlackList}">
    <fieldset>
      <legend>
        <fmt:message key="blacklistedfeeds" />
      </legend>
      <ol>
        <c:forEach items="${portletPreferencesWrapperBean.feedBlackList}" var="feedLink">
          <li>
            <c:out value="${feedLink}" />
            &nbsp;
            &nbsp;
            <input type="button" 
                 value="<fmt:message key="removefromblacklist"/>" 
                 onclick="location.href='${removeFromFeedBlackList}&amp;feedLink=<c:out value="${feedLink}"/>';" />
          </li>
        </c:forEach>
      </ol>
    </fieldset>
    <fieldset class="submit">
      <input type="button" 
             value="<fmt:message 
             key="clearblacklist"/>"
             onclick="location.href='${clearFeedBlackList}';"
      />
    </fieldset>
  </c:if>

</form:form>

