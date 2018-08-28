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
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@ taglib uri="http://liferay.com/tld/util" prefix="liferay-util" %>

<%@ page import="com.liferay.portal.kernel.util.ParamUtil" %>
<%@ page import="com.liferay.portlet.display.template.PortletDisplayTemplateUtil" %>
<%@ page import="com.liferay.dynamic.data.mapping.model.DDMTemplate" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="se.vgregion.portal.rss.client.beans.FeedEntryBean" %>

<portlet:defineObjects/>
<liferay-theme:defineObjects />

<%
	long portletDisplayDDMTemplateId = (Long)request.getAttribute("portletDisplayDDMTemplateId");

	Map<String, Object> displayTemplateContextObjects = (HashMap<String, Object>)request.getAttribute("displayTemplateContextObjects");

	List<FeedEntryBean> rssEntries = (List<FeedEntryBean>)request.getAttribute("rssEntries");
%>

<c:choose>
    <c:when test="${portletDisplayDDMTemplateId > 0 }">
		<%= PortletDisplayTemplateUtil.renderDDMTemplate(request, response, portletDisplayDDMTemplateId, rssEntries, displayTemplateContextObjects) %>
    </c:when>
    <c:otherwise>
		<liferay-util:include page="/WEB-INF/jsp/intra/view_default.jsp" servletContext="<%= application %>" />
    </c:otherwise>
</c:choose>
