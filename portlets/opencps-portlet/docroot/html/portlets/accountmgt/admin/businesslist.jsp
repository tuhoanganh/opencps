
<%@page import="com.liferay.portal.kernel.upgrade.RenameUpgradePortletPreferences"%>
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

<%@page import="org.opencps.accountmgt.util.AccountMgtUtil"%>
<%@page import="org.opencps.util.PortletUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="org.opencps.accountmgt.search.BusinessDisplayTerms"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="org.opencps.accountmgt.model.Business"%>
<%@page import="com.liferay.portal.kernel.dao.search.SearchEntry"%>
<%@page import="org.opencps.util.DateTimeUtil"%>
<%@page import="org.opencps.accountmgt.service.BusinessLocalServiceUtil"%>
<%@page import="org.opencps.accountmgt.search.BusinessSearchTerm"%>
<%@page import="com.liferay.portal.kernel.dao.search.SearchContainer"%>
<%@page import="org.opencps.accountmgt.search.BusinessSearch"%>
<%@ include file="../init.jsp" %>


<liferay-util:include page="/html/portlets/accountmgt/admin/toptabs.jsp" servletContext="<%=application %>" />
<%
	Business business = (Business) request.getAttribute(WebKeys.BUSINESS_ENTRY);
	long businessId = business != null ? business.getBusinessId() : 0L;
	
	String fullName = ParamUtil.getString(request, BusinessDisplayTerms.BUSINESS_NAME);
	int accountStatus = ParamUtil.getInteger(request, BusinessDisplayTerms.BUSINESS_ACCOUNTSTATUS);
	
	int [] accoutStatusArr = {0,1,2,3}; 
	
	PortletURL searchURL = renderResponse.createRenderURL();
	searchURL.setParameter("tabs1", AccountMgtUtil.TOP_TABS_BUSINESS);
	searchURL.setParameter("mvcPath", "/html/portlets/accountmgt/admin/businesslist.jsp");
	
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", "/html/portlets/accountmgt/admin/businesslist.jsp");
	iteratorURL.setParameter(BusinessDisplayTerms.BUSINESS_NAME, fullName);
	iteratorURL.setParameter(BusinessDisplayTerms.BUSINESS_ACCOUNTSTATUS, String.valueOf(accountStatus));
	
	List<Business> businesses = new ArrayList<Business>();
	int totalCount = 0;
	
%>

<aui:row>
	<aui:col width="20">
	</aui:col>
</aui:row>

<aui:form action="<%=searchURL.toString() %>" method="post" name="fm">
	<aui:row>
		<aui:col width="30">
			<aui:input name="<%=BusinessDisplayTerms.BUSINESS_NAME %>" label="<%=StringPool.BLANK %>"/>
		</aui:col>
		
		<aui:col width="30">
			<aui:select name="<%=BusinessDisplayTerms.BUSINESS_ACCOUNTSTATUS %>" label="<%=StringPool.BLANK %>">
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

<liferay-ui:search-container searchContainer="<%= new BusinessSearch(
	renderRequest ,SearchContainer	.DEFAULT_DELTA, iteratorURL) %>">
	
	<liferay-ui:search-container-results>
		<%
			BusinessSearchTerm searchTerms = (BusinessSearchTerm) searchContainer.getSearchTerms();
			
			if(!fullName.equals(StringPool.BLANK)) {
				businesses = BusinessLocalServiceUtil.getBusinesses(themeDisplay.getScopeGroupId(), fullName);
			} else if(accountStatus!=0) {
				businesses = BusinessLocalServiceUtil.getBusinesses(themeDisplay.getScopeGroupId(), accountStatus);
			} else if(!fullName.equals(StringPool.BLANK) && accountStatus!=0)  {
				businesses = BusinessLocalServiceUtil.getBusinesses(themeDisplay.getScopeGroupId(), fullName, accountStatus);
			} else {
				businesses = BusinessLocalServiceUtil.getBusinesses(searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator());
			}
			
			totalCount = BusinessLocalServiceUtil.countAll();
			total = totalCount;
			results = businesses;
			pageContext.setAttribute("results", results);
			pageContext.setAttribute("total", total);
		%>
	
	</liferay-ui:search-container-results>
	<liferay-ui:search-container-row 
		className="org.opencps.accountmgt.model.Business" 
		modelVar="businesS" 
		keyProperty="businessId"
	>
		<%
			String accoutStatus = StringPool.BLANK;
			
			accoutStatus = PortletUtil.getAccountStatus(businesS.getAccountStatus(), themeDisplay.getLocale());
			row.addText(businesS.getIdNumber());
			row.addText(businesS.getName());
			row.addText(businesS.getBusinessType());
			row.addText(businesS.getEmail());
			row.addText(accoutStatus);
			row.addJSP("center", SearchEntry.DEFAULT_VALIGN,  "/html/portlets/accountmgt/admin/business_action.jsp", config.getServletContext(), request, response);
			
		%>
		
	</liferay-ui:search-container-row>
	<liferay-ui:search-iterator/>
</liferay-ui:search-container>	
