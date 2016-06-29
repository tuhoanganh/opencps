
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
<%@page import="org.opencps.dossiermgt.bean.DossierFileBean"%>
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
	headerNames.add("dossier-file-type");
	headerNames.add("original-file");
	
	String headers = StringUtil.merge(headerNames);
	
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "frontofficeexternaldossierlist.jsp");
	iteratorURL.setParameter("tab1", DossierMgtUtil.TOP_TABS_DOSSIER_FILE);

	List<DossierFileBean> dossierFileBeans = new ArrayList<DossierFileBean>();
	int totalCount = 0;
%>


<liferay-ui:search-container 
	searchContainer="<%= new DossierFileSearch(renderRequest, SearchContainer.DEFAULT_DELTA, iteratorURL) %>" 
	headerNames="<%= headers %>"
>

	<liferay-ui:search-container-results>
		<%
			DossierFileSearchTerms searchTerms = (DossierFileSearchTerms)searchContainer.getSearchTerms();
						
			String 	templateFileNo = StringPool.BLANK;
			
			int partType = -1;
			
			int original = 0;
		
			try {
				dossierFileBeans = DossierFileLocalServiceUtil.searchDossierFileAdvance(scopeGroupId, citizen != null ? citizen.getMappingUserId() : 0, business != null ? business.getMappingOrganizationId() : 0, searchTerms.getKeywords(), templateFileNo, 0, partType, original, searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator());
				totalCount = DossierFileLocalServiceUtil.countDossierFileAdvance(scopeGroupId, citizen != null ? citizen.getMappingUserId() : 0, business != null ? business.getMappingOrganizationId() : 0, searchTerms.getKeywords(), templateFileNo, 0, partType, original);
			} catch(Exception e){
				
			}
		
			total = totalCount;
			results = dossierFileBeans;
			
			pageContext.setAttribute("results", results);
			pageContext.setAttribute("total", total);				
		%>
	</liferay-ui:search-container-results>	
		<liferay-ui:search-container-row 
			className="org.opencps.dossiermgt.bean.DossierFileBean" 
			modelVar="dossierFileBean" 
			keyProperty="dossierFileId"
		>
			<%
			
				DossierFile dossierFile = dossierFileBean.getDossierFile();
			    // no column
				row.addText(String.valueOf(row.getPos() + 1 + searchContainer.getStart()));
			
				row.addText(DateTimeUtil.convertDateToString(dossierFile.getCreateDate(), DateTimeUtil._VN_DATE_TIME_FORMAT));
				
				// dossier file no column
				row.addText(dossierFile.getDossierFileNo());
								
				row.addText(DateTimeUtil.convertDateToString(dossierFile.getDossierFileDate(), DateTimeUtil._VN_DATE_FORMAT));
				// dossier display name column
				row.addText(dossierFile.getDisplayName());
				
				row.addText(dossierFileBean.getReceptionNo());
				
				row.addText(String.valueOf(dossierFileBean.getPartType()));
				
				StringBuffer sb = new StringBuffer();
				sb.append("<input type=\"checkbox\""+ (dossierFile.getOriginal() == 0 ? "checked" : StringPool.BLANK) + ">");
				row.addText(sb.toString());
				
			%>	
		</liferay-ui:search-container-row> 
	
	<liferay-ui:search-iterator/>
</liferay-ui:search-container>