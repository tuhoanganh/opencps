
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
<%@page import="org.opencps.util.PortletUtil"%>
<%@page import="org.opencps.accountmgt.search.CitizenDisplayTerms"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="org.opencps.accountmgt.model.Citizen"%>
<%@page import="com.liferay.portal.kernel.dao.search.ResultRow"%>
<%@ include file="../init.jsp" %>

<%
	ResultRow row =
	(ResultRow) request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);
	Citizen citizen = (Citizen)row.getObject();
	String redirectURL = currentURL;
	
	String accountActionName = StringPool.BLANK;
	int changeAccountStatus = PortletUtil.changeAccountStatus(citizen.getAccountStatus());
	
	accountActionName = PortletUtil.getAccountActionName(changeAccountStatus, themeDisplay.getLocale());
%>

<liferay-ui:icon-menu>
	<portlet:renderURL var="updateCitizen"> 
		<portlet:param name="<%=CitizenDisplayTerms.CITIZEN_ID %>" value="<%=String.valueOf(citizen.getCitizenId()) %>"/>
		<portlet:param name="mvcPath" value="/html/portlets/accountmgt/admin/update_profile.jsp"/>
	</portlet:renderURL>
	
	<liferay-ui:icon image="edit" message="edit"
		url="<%=updateCitizen.toString()%>" />
	
	
	<portlet:actionURL var="changeAccountStatusURL" name="changeAccountStatusURL">
		<portlet:param name="<%=CitizenDisplayTerms.CITIZEN_ACCOUNTSTATUS %>" value="<%=String.valueOf(citizen.getAccountStatus()) %>"/>
		<portlet:param name="<%=CitizenDisplayTerms.CITIZEN_ID %>" value="<%=String.valueOf(citizen.getCitizenId()) %>"/>
	</portlet:actionURL>
	
	<liferay-ui:icon image="status_online" message="<%=accountActionName %>"
		url="<%= changeAccountStatusURL.toString()%>" />
</liferay-ui:icon-menu>