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
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix='fn' uri='http://java.sun.com/jsp/jstl/functions'%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!--CSS file (default YUI Sam Skin) -->
<link type="text/css" rel="stylesheet" href="/vgr-theme/javascript/yui-2.8.0r4/assets/skins/sam/datatable.css" />
 
<!-- Dependencies -->
<script type="text/javascript" src="/vgr-theme/javascript/yui-2.8.0r4/yahoo-dom-event.js"></script>
<script type="text/javascript" src="/vgr-theme/javascript/yui-2.8.0r4/element-min.js"></script>
<script type="text/javascript" src="/vgr-theme/javascript/yui-2.8.0r4/datasource-min.js"></script>
<!-- OPTIONAL: Drag Drop (enables resizeable or reorderable columns) -->
<script type="text/javascript" src="/vgr-theme/javascript/yui-2.8.0r4/dragdrop-min.js"></script>
<script type="text/javascript" src="/vgr-theme/javascript/yui-2.8.0r4/datatable-min.js"></script>

<portlet:actionURL escapeXml="false" var="formAction" />


<div id="module-news" class="module">
  <div id="module-content">

    <script type="text/javascript">
    <!--
    YAHOO.example.Data = {
        rssItems: [
         <c:forEach items="${rssEntries}" var="item" varStatus="status">{Flöde:'<c:out value="${item.feedTitle}" escapeXml="true" />',Datum:new Date(<fmt:formatDate value="${item.publishedDate}" type="both" pattern="yyyy, MM, dd" />),Titel:'<a href="${item.link}"><c:out value="${item.title}" escapeXml="true"/></a>', Beskrivning:'<pre style="font-family:arial;font-size:100%; white-space: pre-wrap; white-space: -moz-pre-wrap; white-space: -pre-wrap; white-space: -o-pre-wrap; word-wrap: break-word;"><c:out value="${item.excerpt}" /></pre>'}<c:if test="${not status.last}">,</c:if></c:forEach>
        ]
    };
    
    YAHOO.util.Event.addListener(window, "load", function() {
        YAHOO.example.Basic = function() {
            var myColumnDefs = [
              {key:"Flöde", sortable:true, resizeable:true},
              {key:"Datum", formatter:YAHOO.widget.DataTable.formatDate, sortable:true, sortOptions:{defaultDir:YAHOO.widget.DataTable.CLASS_DESC},resizeable:true},
              {key:"Titel", sortable:true, resizeable:true},
              {key:"Beskrivning", sortable:true, resizeable:true}
            ];
    
            var myDataSource = new YAHOO.util.DataSource(YAHOO.example.Data.rssItems);
            myDataSource.responseType = YAHOO.util.DataSource.TYPE_JSARRAY;
            myDataSource.responseSchema = {
                fields: ["Flöde","Datum","Titel","Beskrivning"]
            };
    
            var myDataTable = new YAHOO.widget.DataTable("basic",
                    myColumnDefs, myDataSource, {});
                    
            return {
                oDS: myDataSource,
                oDT: myDataTable
            };
        }();
    });
    //--></script>
    <div id="basic"></div> 
  </div>
</div>
