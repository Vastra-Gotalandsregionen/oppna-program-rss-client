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

<portlet:defineObjects />
<portlet:actionURL var="save" />

<style>
  fieldset {  
    margin: 1.5em 0 0 0;  
    padding: 0;  
  }  
  legend {  
    margin-left: 1em;  
    color: #000000;  
    font-weight: bold;  
  }  
  fieldset ol {  
    padding: 1em 1em 0 1em;  
    list-style: none;  
  }  
  fieldset li {  
    padding-bottom: 1em;  
  }  
  fieldset.submit {  
    border-style: none;  
  }
</style>

<form:form method="POST" action="${save}">
  <fieldset>
    <legend><fmt:message key="settings"/></legend>
    <ol>
      <li>
        <label for="maximum-items"><fmt:message key="numberofitems" />:</label>
        <form:input id="maxium-items" path="numberOfItems"/>
      </li>
      <li>
        <label for="rss-feed-link"><fmt:message key="feeds" />:</label> 
        <form:textarea id="rss-feed-link" path="rssFeedLink" cols="100" rows="5"/>
      </li>
    </ol>
  </fieldset>
  <fieldset class="submit">
    <input value=<fmt:message key="save"/> />
  </fieldset>
</form:form>
