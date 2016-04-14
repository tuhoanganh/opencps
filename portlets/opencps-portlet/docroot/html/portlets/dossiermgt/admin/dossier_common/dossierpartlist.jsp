<%@page import="com.liferay.portal.kernel.dao.search.SearchEntry"%>
<%@page import="org.opencps.dossiermgt.service.DossierPartLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.search.DossierPartSearchTerms"%>
<%@page import="org.opencps.dossiermgt.model.DossierPart"%>
<%@page import="org.opencps.dossiermgt.search.DossierPartSearch"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="org.opencps.dossiermgt.permission.DossierPartPermission"%>
<%@page import="org.opencps.util.ActionKeys"%>
<%@page import="org.opencps.dossiermgt.permission.DossierTemplatePermission"%>
<%@page import="org.opencps.dossiermgt.util.DossierMgtUtil"%>
<%
/**
 * OpenCPS is the open source Core Public Services software
 * Copyright (C) 2016-present OpenCPS community
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 fds * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
%>
<%@ include file="../../init.jsp"%>
<%
	request.setAttribute("tabs2", DossierMgtUtil.TOP_TABS_DOSSIER_PART);

	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "dossier_common/dossierpartlist.jsp");
	iteratorURL.setParameter("tabs2", DossierMgtUtil.TOP_TABS_DOSSIER_PART);
	
	List<String> headerNames = new ArrayList<String>();
	
	headerNames.add("row-no");
	headerNames.add("part-no");
	headerNames.add("part-name");
	headerNames.add("part-type");
	headerNames.add("part-tip");
	
	boolean isPermission =
					DossierPartPermission.contains(
				        themeDisplay.getPermissionChecker(),
				        themeDisplay.getScopeGroupId(), ActionKeys.ADD_DOSSIER_PART);

	if (isPermission) {
		headerNames.add("action");
	}
	
	String headers = StringUtil.merge(headerNames, StringPool.COMMA);
	
	int totalCount = 0;
	List<DossierPart> dossierParts = new ArrayList<DossierPart>();
					
%>
<c:if test="<%=DossierPartPermission.contains(permissionChecker, scopeGroupId, ActionKeys.ADD_DOSSIER_PART) %>">
	<liferay-util:include page='<%= templatePath + "toolbar.jsp" %>' servletContext="<%=application %>" />
</c:if>

<liferay-ui:search-container searchContainer="<%= new DossierPartSearch(renderRequest, SearchContainer.DEFAULT_DELTA, iteratorURL) %>" 
	headerNames="<%= headers %>">
	<liferay-ui:search-container-results>
		<%
			DossierPartSearchTerms searchTerms = (DossierPartSearchTerms) searchContainer.getSearchTerms();
			if(Validator.isNotNull(searchTerms.getKeywords())) {
				dossierParts = DossierPartLocalServiceUtil.getDossierParts(searchTerms.getKeywords());
				totalCount = dossierParts.size();
			} else {
				dossierParts = DossierPartLocalServiceUtil.getDossierParts(
					searchContainer.getStart(),
					searchContainer.getEnd(),
					searchContainer.getOrderByComparator()
								);	
				totalCount = DossierPartLocalServiceUtil.countAll();
			}
			
			total = totalCount;
			results = dossierParts;
			pageContext.setAttribute("results", results);
			pageContext.setAttribute("total", total);
		%>
	</liferay-ui:search-container-results>
	
	<liferay-ui:search-container-row 
		className="org.opencps.dossiermgt.model.DossierPart" 
		modelVar="dossierPart" 
		keyProperty="dossierpartId"
	>
		<%
			String partTypeName = DossierMgtUtil.getNameOfPartType(dossierPart.getPartType(), themeDisplay.getLocale());
			row.addText(String.valueOf(row.getPos() + 1));
			row.addText(dossierPart.getPartNo());
			row.addText(dossierPart.getPartName());
			row.addText(partTypeName);
			row.addText(dossierPart.getPartTip());
			if(isPermission) {
				row.addJSP("center", SearchEntry.DEFAULT_VALIGN, templatePath + "dossier_part_actions.jsp", config.getServletContext(), request, response);
			}
		%>
		
	</liferay-ui:search-container-row>
	<liferay-ui:search-iterator/>
</liferay-ui:search-container>