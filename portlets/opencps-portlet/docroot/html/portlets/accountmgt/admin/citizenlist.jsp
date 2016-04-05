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
 * along with this program. If not, see <http://www.gnu.org/licenses/>
 */
%>
<%@page import="org.opencps.util.DateTimeUtil"%>
<%@page import="org.opencps.util.PortletUtil"%>
<%@page import="org.opencps.accountmgt.search.CitizenSearch"%>
<%@page import="org.opencps.accountmgt.service.CitizenLocalServiceUtil"%>
<%@page import="org.opencps.accountmgt.search.CitizenSearchTerm"%>
<%@page import="com.liferay.portal.kernel.dao.search.SearchContainer"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="javax.persistence.metamodel.ListAttribute"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="org.opencps.accountmgt.model.Citizen"%>
<%@page import="com.liferay.portal.kernel.dao.search.SearchEntry"%>
<%@page import="org.opencps.accountmgt.search.CitizenDisplayTerms"%>
<%@ include file="../init.jsp" %>

<%
	Citizen citizen = (Citizen) request.getAttribute(WebKeys.CITIZEN_ENTRY);
	long citizenId = citizen != null ? citizen.getCitizenId() : 0L;
	
	String fullName = ParamUtil.getString(request, CitizenDisplayTerms.CITIZEN_FULLNAME );
	int accountStatus = ParamUtil.getInteger(request, CitizenDisplayTerms.CITIZEN_ACCOUNTSTATUS);
	
	int [] accoutStatusArr = {0,1,2,3}; 
	
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "citizenlist.jsp");
	iteratorURL.setParameter(CitizenDisplayTerms.CITIZEN_FULLNAME, fullName);
	iteratorURL.setParameter(CitizenDisplayTerms.CITIZEN_ACCOUNTSTATUS, String.valueOf(accountStatus));
	List<Citizen> citizens = new ArrayList<Citizen>();
	
	
	int totalCount = 0;
	
	
%>

<liferay-util:include page="/html/portlets/accountmgt/admin/toptabs.jsp" servletContext="<%=application %>" />

<portlet:actionURL var="searchCitizenURL" name="searchCitizen" />

<aui:form action="<%=searchCitizenURL.toString() %>" method="post" name="fm">
	<aui:row>
		<aui:col width="30">
			<aui:input name="<%=CitizenDisplayTerms.CITIZEN_FULLNAME %>" label="<%=StringPool.BLANK %>"/>
		</aui:col>
		
		<aui:col width="30">
			<aui:select name="<%=CitizenDisplayTerms.CITIZEN_ACCOUNTSTATUS %>" label="<%=StringPool.BLANK %>">
				<%
					for(int i=0; i<accoutStatusArr.length; i++) {
						%>
							<aui:option value="<%=accoutStatusArr[i] %>">
							<%=PortletUtil.getAccountStatus(accoutStatusArr[i], themeDisplay.getLocale()) %>
							</aui:option>
						<%
						
					}
				%>
			</aui:select>
		</aui:col>

		<aui:col width="30">
			<aui:input label="<%=StringPool.BLANK %>" name="search" type="submit" value="search"/> 		
		</aui:col>
	</aui:row>
</aui:form>


<liferay-ui:search-container searchContainer="<%= new CitizenSearch(
	renderRequest ,SearchContainer.DEFAULT_DELTA, iteratorURL) %>">
	
	<liferay-ui:search-container-results>
		<%
			CitizenSearchTerm searchTerms = (CitizenSearchTerm) searchContainer.getSearchTerms();
			
			if(!fullName.equals(StringPool.BLANK)) {
				citizens = CitizenLocalServiceUtil.getCitizens(themeDisplay.getScopeGroupId(), fullName);
			} else if(accountStatus!=0) {
				citizens = CitizenLocalServiceUtil.getCitizens(themeDisplay.getScopeGroupId(), accountStatus);
			} else if(!fullName.equals(StringPool.BLANK) && accountStatus!=0)  {
				citizens = CitizenLocalServiceUtil.getCitizens(themeDisplay.getScopeGroupId(), fullName, accountStatus);
			} else {
				citizens = CitizenLocalServiceUtil.getCitizens(searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator());
			}
			
			totalCount = CitizenLocalServiceUtil.countAll();
			total = totalCount;
			results = citizens;
			pageContext.setAttribute("results", results);
			pageContext.setAttribute("total", total);
		%>
	
	</liferay-ui:search-container-results>
	<liferay-ui:search-container-row 
		className="org.opencps.accountmgt.model.Citizen" 
		modelVar="citiZen" 
		keyProperty="citizenId"
	>
		<%
			String gender = StringPool.BLANK;
			gender = PortletUtil.getGender(citiZen.getGender(), themeDisplay.getLocale());
			
			String accoutStatus = StringPool.BLANK;
			
			accoutStatus = PortletUtil.getAccountStatus(citiZen.getAccountStatus(), themeDisplay.getLocale());
			
			
			row.addText(citiZen.getPersonalId());
			row.addText(citiZen.getFullName());
			row.addText(gender);
			row.addText(DateTimeUtil.convertDateToString(citiZen.getBirthdate(), DateTimeUtil._VN_DATE_FORMAT));
			row.addText(citiZen.getEmail());
			row.addText(accoutStatus);
			row.addJSP("center", SearchEntry.DEFAULT_VALIGN,  "/html/portlets/accountmgt/admin/citizen_action.jsp", config.getServletContext(), request, response);
			
		%>
		
	</liferay-ui:search-container-row>
	<liferay-ui:search-iterator/>
</liferay-ui:search-container>	