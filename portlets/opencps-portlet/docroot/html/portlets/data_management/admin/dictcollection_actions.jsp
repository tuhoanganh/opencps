<%@page import="org.opencps.datamgt.model.DictCollection"%>
<%@page import="com.liferay.portal.kernel.util.WebKeys"%>
<%@page import="com.liferay.portal.kernel.dao.search.ResultRow"%>
<%@page import="com.liferay.portal.kernel.dao.search.SearchContainer"%>
<%
/**
 * OpenCPS is the open source Core Public Services software
 * Copyright (C) 2016-present OpenCPS community
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
%>
 <%@ include file="../init.jsp"%>
 
<%
	ResultRow row = (ResultRow)request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);
	DictCollection dictCollection = (DictCollection) row.getObject();
%> 
<portlet:actionURL var="delDictCollectionURL" name="delDictCollection" >
	<portlet:param name="idDictCol" value="<%=String.valueOf(dictCollection.getDictCollectionId()) %>"/>
</portlet:actionURL> 
			
<portlet:renderURL var="updateDictCollectionURL">
	<portlet:param name="mvcPath" value="/html/portlets/data_management/admin/view.jsp"/>
	<portlet:param name="dictCollectionId" value="<%=String.valueOf(dictCollection.getDictCollectionId()) %>"/>
</portlet:renderURL> 
 <liferay-ui:icon-menu>
	<liferay-ui:icon image="delete" message="Delete" url="<%=delDictCollectionURL.toString() %>" />  
	<liferay-ui:icon image="edit" message="Edit" url="<%=updateDictCollectionURL.toString() %>" /> 
</liferay-ui:icon-menu> 