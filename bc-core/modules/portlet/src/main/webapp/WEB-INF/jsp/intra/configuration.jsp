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

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %>
<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@ taglib uri="http://liferay.com/tld/ddm" prefix="liferay-ddm" %>

<%@ page import="com.liferay.portal.kernel.util.Constants" %>

<portlet:defineObjects />
<liferay-theme:defineObjects />

<liferay-portlet:actionURL portletConfiguration="true" var="configurationURL" />

<aui:form action="${configurationURL}" method="post" name="fm">
	<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.UPDATE %>" />

    <div class="portlet-configuration-body-content">
        <div class="container-fluid-1280">
		    <aui:fieldset-group markupView="lexicon">
				<aui:fieldset collapsible="<%= true %>" label="display-settings">
					<div class="display-template">
			
						<liferay-ddm:template-selector
							className="${templateHandlerClassName}"
							displayStyle="${displayStyle}"
							displayStyleGroupId="${displayStyleGroupId}"
							refreshURL="${refreshUrl}"
							showEmptyOption="true"
						/>
						
						
					</div>
				</aui:fieldset>
				
				<aui:fieldset collapsible="<%= true %>" label="Feed">
				
					<aui:input type="text" name="preferences--rssFeedtitle1--" id="Titel f&ouml;r feed" label="feedTitle" value="${rssFeedtitle1}" />
					<aui:input type="text" name="preferences--rssFeedLink1--" id="URL f&ouml;r feed" label="feedUrl" value="${rssFeedLink1}" />
					
					<aui:select name="preferences--numberOfItems1--" label="Visa max antal poster">
						<c:forEach begin="0" end="100" var="index">
							<c:set var="optionSelected" value="false" scope="page" />
							<c:if test="${index == numberOfItems1}">
								<c:set var="optionSelected" value="true" scope="page" />
							</c:if>
							<aui:option label="${index} st" value="${index}" selected="${optionSelected}" />
						</c:forEach>
					</aui:select>
					
					<%--		
					<aui:input type="text" name="preferences--numberOfItems1--" id="numberOfItems1" label="feedUrl" value="${numberOfItems1}" />
				 	--%>
				
				</aui:fieldset>
			</aui:fieldset-group>
	   </div>
	</div>

	<aui:button-row>
		<aui:button type="submit" />
	</aui:button-row>
</aui:form>