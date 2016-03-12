<%@page import="com.liferay.portal.kernel.dao.search.SearchEntry"%>
<%@page import="org.opencps.datamgt.search.DictCollectionSearch"%>
<%@page import="com.liferay.portal.kernel.util.ListUtil"%>
<%@page import="javax.swing.plaf.ListUI"%>
<%@page import="org.opencps.datamgt.service.DictCollectionLocalServiceUtil"%>
<%@page import="org.opencps.datamgt.model.DictCollection"%>
<%@page import="java.util.List"%>
<%@page import="javax.portlet.PortletURL"%>
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
	String dictCollName = ParamUtil.getString(request, "dictCollNameReq");
	String dictCollCode = ParamUtil.getString(request, "dictCollCodeReq");
	String dictCollDes = ParamUtil.getString(request, "dictCollDesReq");
	
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", "/html/portlets/data_management/admin/view.jsp");
	/* iteratorURL.setParameter("dictCollNameReq", dictCollName);
	iteratorURL.setParameter("dictCollCodeReq", dictCollCode);
	iteratorURL.setParameter("dictCollDesReq", dictCollDes); */
	
%>


<liferay-ui:search-container emptyResultsMessage="No data found"  iteratorURL="<%=iteratorURL %>" 
searchContainer="<%= new DictCollectionSearch(renderRequest,5, iteratorURL) %>">
	<liferay-ui:search-container-results>
		<%
			List<DictCollection> lstCollection = DictCollectionLocalServiceUtil.getDictCollections(searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator());
			total = DictCollectionLocalServiceUtil.countAll();
			results = lstCollection;
			pageContext.setAttribute("results", results);
			pageContext.setAttribute("total", total);
		%>
	</liferay-ui:search-container-results>	
		<liferay-ui:search-container-row className="org.opencps.datamgt.model.DictCollection" 
		modelVar="dictCollection" keyProperty="dictCollectionId">
			
			<%
		
			row.addText(String.valueOf(dictCollection.getDictCollectionId()), "");
			row.addText(dictCollection.getCollectionCode(), "");
			row.addText(dictCollection.getCollectionName(), "");
			row.addText(dictCollection.getDescription(), "");
			row.addText(String.valueOf(dictCollection.getCreateDate()), "");
			row.addText(dictCollection.getCollectionName(), "");
			row.addText(String.valueOf(dictCollection.getUserId()), "");
			
			row.addJSP("right",SearchEntry.DEFAULT_VALIGN,"/html/portlets/data_management/admin/dictcollection_actions.jsp", config.getServletContext(), request, response);
			%>
			<%-- <liferay-ui:search-container-column-text
			 value="<%=dictCollection.getCollectionName() %>" 
			 name="name1" orderable="<%=true %>"/>
			<liferay-ui:search-container-column-text name="Collection Code" value="<%=dictCollection.getCollectionCode() %>" />
			<liferay-ui:search-container-column-text name="Collection Description" value="<%=dictCollection.getDescription()%>" />
			<liferay-ui:search-container-column-text name="Collection UserID" value="<%=String.valueOf(dictCollection.getUserId())%>" />
			<liferay-ui:search-container-column-text name="Collection GroupID" value="<%=String.valueOf(dictCollection.getGroupId())%>" />
			<liferay-ui:search-container-column-text name="Collection CompanyID" value="<%=String.valueOf(dictCollection.getCompanyId())%>" />
			<liferay-ui:search-container-column-text name="Collection Create Date" value="<%=dictCollection.getCreateDate().toString()%>" />
			<liferay-ui:search-container-column-text name="Collection Modified Date" value="<%=dictCollection.getModifiedDate().toString()%>" />
	
			<liferay-ui:search-container-column-jsp  name ="Action" path="/html/portlets/data_management/admin/dictcollection_actions.jsp"></liferay-ui:search-container-column-jsp>
 --%>			
		
		</liferay-ui:search-container-row> 
	
	<liferay-ui:search-iterator/>
</liferay-ui:search-container>


