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

<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="org.opencps.util.DateTimeUtil"%>
<%@page import="org.opencps.dossiermgt.service.DossierFileLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.util.DossierMgtUtil"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="org.opencps.dossiermgt.model.DossierFile"%>
<%@page import="org.opencps.dossiermgt.search.DossierFileSearchTerms"%>
<%@page import="org.opencps.dossiermgt.search.DossierFileSearch"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@ include file="../init.jsp"%>

<liferay-util:include page='<%=templatePath + "toptabs.jsp" %>' servletContext="<%=application %>" />
<liferay-util:include page='<%=templatePath + "toolbar.jsp" %>' servletContext="<%=application %>" />

<%
	List<String> headerNames = new ArrayList<String>();
	
	headerNames.add("#");
	headerNames.add("create-date");
	headerNames.add("dossier-file-no");
	headerNames.add("dossier-file-date");
	headerNames.add("display-name");
	headerNames.add("dossier-name");
	headerNames.add("dossier-type");
	headerNames.add("original");
	
	String headers = StringUtil.merge(headerNames);
	
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "frontofficeexternaldossierlist.jsp");
	iteratorURL.setParameter("tab1", DossierMgtUtil.TOP_TABS_DOSSIER_FILE);

%>


<liferay-ui:search-container 
	searchContainer="<%= new DossierFileSearch(renderRequest, SearchContainer.DEFAULT_DELTA, iteratorURL) %>" 
	headerNames="<%= headers %>"
>

	<liferay-ui:search-container-results>
		<%
			DossierFileSearchTerms searchTerms = (DossierFileSearchTerms)searchContainer.getSearchTerms();
						
			List<DossierFile> dossierFiles = new ArrayList<DossierFile>();
			int totalCount = 0;
			try {
				//dossierFiles = DossierFileLocalServiceUtil.searchDossierFile(scopeGroupId, searchTerms.getKeywords(), templateFileNo, 0, searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator());
				//totalCount = DossierFileLocalServiceUtil.countDossierFile(scopeGroupId, searchTerms.getKeywords(), templateFileNo, 0);
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
				row.addText(String.valueOf(row.getPos() + 1 + searchContainer.getStart()));
			
				row.addText(DateTimeUtil.convertDateToString(dossierFile.getCreateDate(), DateTimeUtil._VN_DATE_TIME_FORMAT));
				
				// dossier file no column
				row.addText(dossierFile.getDossierFileNo());
								
				// dossier display name column
				row.addText(dossierFile.getDisplayName());
				
				headerNames.add("-");
				headerNames.add("-");
				
				
				row.addButton(LanguageUtil.get(locale, "select"), "javascript:" + renderResponse.getNamespace() + "selectDossierFile(" + dossierFile.getDossierFileId() +")");
				
			%>	
		</liferay-ui:search-container-row> 
	
	<liferay-ui:search-iterator/>
</liferay-ui:search-container>