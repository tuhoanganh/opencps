
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

<%@page import="org.opencps.dossiermgt.search.DossierDisplayTerms"%>
<%@page import="org.opencps.dossiermgt.search.DossierFileDisplayTerms"%>
<%@page import="org.opencps.dossiermgt.service.DossierFileLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.model.DossierFile"%>
<%@page import="org.opencps.dossiermgt.search.DossierFileSearchTerms"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="org.opencps.dossiermgt.search.DossierFileSearch"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@ include file="/init.jsp"%>

<%
	List<String> headerNames = new ArrayList<String>();
	
	headerNames.add("#");
	headerNames.add("dossierFileNo");
	headerNames.add("displayName");
	headerNames.add("select");
	
	String headers = StringUtil.merge(headerNames);
	
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", "/html/common/portlet/dossier_select_file.jsp");
	iteratorURL.setParameter("tab1", "select-file");
	
	String templateFileNo = ParamUtil.getString(request, DossierDisplayTerms.TEMPLATE_FILE_NO);
%>

<liferay-ui:search-container 
	searchContainer="<%= new DossierFileSearch(renderRequest, SearchContainer.DEFAULT_DELTA, iteratorURL) %>" 
	headerNames="<%= headers %>"
>

	<liferay-ui:search-container-results>
		<%
			DossierFileSearchTerms searchTerms = (DossierFileSearchTerms)searchContainer.getSearchTerms();
						
				List<DossierFile> dossierFiles = null;
				int totalCount = 0;
				try {
					dossierFiles = DossierFileLocalServiceUtil.searchDossierFile(scopeGroupId, searchTerms.getKeywords(), templateFileNo, 0, searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator());
					totalCount = DossierFileLocalServiceUtil.countDossierFile(scopeGroupId, searchTerms.getKeywords(), templateFileNo, 0);
				} catch(Exception e){
					
				}
			
				total = totalCount;
				results = dossierFiles;
				
				pageContext.setAttribute("results", results);
				pageContext.setAttribute("total", total);				
		%>
	</liferay-ui:search-container-results>	
		<liferay-ui:search-container-row 
			className="org.opencps.dossiermgt.model.DossierFile" 
			modelVar="dossierFile" 
			keyProperty="dossierFileId"
		>
			<%				
			    // no column
				row.addText(String.valueOf(row.getPos() + 1));
				
				// dossier file no column
				row.addText(dossierFile.getDossierFileNo());
								
				// dossier display name column
				row.addText(dossierFile.getDisplayName());
				
				row.addButton("select", "javascript:void(0)");
				
			%>	
		</liferay-ui:search-container-row> 
	
	<liferay-ui:search-iterator/>
</liferay-ui:search-container>