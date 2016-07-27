<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="org.opencps.dossiermgt.permissions.DossierTemplatePermission"%>
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
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@page import="org.opencps.util.MessageKeys"%>
<%@page import="org.opencps.dossiermgt.search.DossierTemplateSearch"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.opencps.dossiermgt.model.DossierTemplate"%>
<%@page import="java.util.List"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="com.liferay.portal.kernel.dao.search.SearchEntry"%>
<%@page import="org.opencps.util.ActionKeys"%>
<%@page import="org.opencps.dossiermgt.service.DossierTemplateLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.search.DossierTemplateSearchTerms"%>

<%@ include file="../init.jsp"%>

<style>
<!--
/* Table Thu tuc hanh chinh */
.opencps-theme .ocps-serviceinfo-list table {
	border: 0;
}
.opencps-theme .ocps-serviceinfo-list table thead {
	display: none;
}
.opencps-theme .ocps-serviceinfo-list table td {
	background: none #fff;
	border: 0;
	border-bottom: 1px solid #e1e1e1;
	position: relative;
}
.opencps-theme .ocps-serviceinfo-list table tr:first-child td {
	border-top: 1px solid #e1e1e1;
}
.opencps-theme .ocps-serviceinfo-list table td:after {
	content: "";
	border-right: 1px solid #e1e1e1;
	width: 1px;
	height: calc(100% - 30px);
	position: absolute;
	right: 0;
	top: 15px;
}
.opencps-theme .ocps-serviceinfo-list table td:first-child:after, 
.opencps-theme .ocps-serviceinfo-list table td:last-child:after {
	border: 0;
}
.opencps-theme .ocps-serviceinfo-list table td.table-cell {
	padding: 20px 30px;
}
.opencps-theme .ocps-serviceinfo-list table td.table-cell.first {
	text-align: right;
	font-family: 'Roboto-Bold';
	padding: 20px 0;
	width: 40px;
}
.opencps-theme .ocps-serviceinfo-list table td.table-cell .ocps-searh-bound-data p span {
	font-family: 'Roboto-Bold';
	display: inline-block;
	width: 125px;
}
.opencps-theme .ocps-serviceinfo-list table td.table-cell .ocps-searh-bound-data p a {
	display: flex;
}
.opencps-theme .ocps-serviceinfo-list table td:nth-child(3) {
	width: 280px;
}
.opencps-theme .ocps-serviceinfo-list table td:last-child {
	width: 150px;
}
.opencps-theme .ocps-serviceinfo-list table td:last-child a img {
	display: none;
}
.opencps-theme .ocps-serviceinfo-list table td:last-child a {
	width: 100%;
    display: block;
    height: 30px;
    border-radius: 20px;
}
.opencps-theme .ocps-serviceinfo-list table td:last-child .edit a {
	background-color: #0090ff;
	margin: 25% 0;
}
.opencps-theme .ocps-serviceinfo-list table td:last-child .delete a {
	background-color: #ff5558;
}

-->
</style>

<%-- <liferay-util:include page='<%=templatePath + "edit_dossier.jsp" %>' servletContext="<%=application %>" /> --%>

<%
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "dossiertemplatelist.jsp");
	List<DossierTemplate> dossierTemplates = new ArrayList<DossierTemplate>();
	int totalCount = 0;
	
	boolean isPermission =
				    DossierTemplatePermission.contains(
				        themeDisplay.getPermissionChecker(),
				        themeDisplay.getScopeGroupId(), ActionKeys.ADD_DOSSIER_TEMPLATE);
%>
<div class="ocps-serviceinfo-list">
<liferay-util:include page='<%= templatePath + "toptabs.jsp" %>' servletContext="<%=application %>" />


<liferay-ui:error 
	key="<%=MessageKeys.DOSSIER_TEMPLATE_DELETE_ERROR %>" 
	message="<%=MessageKeys.DOSSIER_TEMPLATE_DELETE_ERROR %>"
/>

<c:if test="<%=DossierTemplatePermission.contains(permissionChecker, scopeGroupId, ActionKeys.ADD_DOSSIER_TEMPLATE) %>">
	<liferay-util:include page='<%= templatePath + "toolbar.jsp" %>' servletContext="<%=application %>" />
</c:if>

<liferay-ui:search-container searchContainer="<%= new DossierTemplateSearch(
	renderRequest ,SearchContainer.DEFAULT_DELTA, iteratorURL) %>">
	<liferay-ui:search-container-results>
		<%
			DossierTemplateSearchTerms searchTerms = (DossierTemplateSearchTerms) searchContainer.getSearchTerms();
				dossierTemplates = DossierTemplateLocalServiceUtil
								.getDossierTemplates(searchTerms.getKeywords(),
									searchContainer.getStart(), searchContainer.getEnd(), 
									searchContainer.getOrderByComparator());
				if(Validator.isNotNull(searchTerms.getKeywords())) {
					totalCount = DossierTemplateLocalServiceUtil
									.countDossierTemplatesByName(searchTerms.getKeywords());
				} else {
					totalCount = DossierTemplateLocalServiceUtil.countAll();
				}
			total = totalCount;
			results = dossierTemplates;
			pageContext.setAttribute("results", results);
			pageContext.setAttribute("total", total);
		%>
	</liferay-ui:search-container-results>
	
	
	
	
	
	<liferay-ui:search-container-row 
		className="org.opencps.dossiermgt.model.DossierTemplate" 
		modelVar="dossierTemplate" 
		keyProperty="dossierTemplateId"
	>
	
	
	
		
		<%
			String templateNumberVal =  LanguageUtil.get(portletConfig, themeDisplay.getLocale(), "template-number");
			String template_name =  LanguageUtil.get(portletConfig, themeDisplay.getLocale(), "template_name");
			String templateDescription =  LanguageUtil.get(portletConfig, themeDisplay.getLocale(), "template-description");
			
		
			String templateNo = dossierTemplate.getTemplateNo();
			String templateName = dossierTemplate.getTemplateName();
			String s = "<div class=\"ocps-searh-bound-data\"><p class=\"ocps-searh-bound-data-chirld-p\"><span class=\"ocps-searh-bound-data-chirld-span\">" + templateNumberVal + "</span>"+ templateNo +"</p>";
			s= s + "<p class=\"ocps-searh-bound-data-chirld-p\"><span class=\"ocps-searh-bound-data-chirld-span\">"+ template_name +"</span><a class=\"ocps-searh-bound-data-chirld-label\" href=\"#\"> "+ templateName + "</a></p></div>";
			row.addText(String.valueOf(row.getPos() + 1));
			row.addText(s);
			
			String des = "<div class=\"ocps-searh-bound-data\"><p class=\"ocps-searh-bound-data-chirld-p\"><span class=\"ocps-searh-bound-data-chirld-span\">" + templateDescription + "</span><a class=\"ocps-searh-bound-data-chirld-label\" href=\"#\">" + dossierTemplate.getDescription() + "</p></div>";
				
			
			row.addText(des);
			if(isPermission) {
				row.addJSP("center", SearchEntry.DEFAULT_VALIGN, templatePath + "dossier_template_action.jsp", config.getServletContext(), request, response);
			}
		%>
	</liferay-ui:search-container-row>
	
	<liferay-ui:search-iterator/>
</liferay-ui:search-container>
</div>

