
<%@page import="org.opencps.util.ActionKeys"%>
<%@page import="org.opencps.accountmgt.permissions.BusinessPermission"%>
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

<%@page import="org.opencps.accountmgt.search.BusinessDisplayTerms"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="org.opencps.util.PortletUtil"%>
<%@page import="org.opencps.accountmgt.model.Business"%>
<%@page import="com.liferay.portal.kernel.dao.search.ResultRow"%>
<%@ include file="../init.jsp" %>


<%
	ResultRow row =
	(ResultRow) request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);
	Business business = (Business)row.getObject();
	String redirectURL = currentURL;
	
	String accountActionName = StringPool.BLANK;
	int changeAccountStatus = PortletUtil.changeAccountStatus(business.getAccountStatus());
	
	accountActionName = PortletUtil.getAccountActionName(changeAccountStatus, themeDisplay.getLocale());
%>

<liferay-ui:icon-menu>
	<portlet:renderURL var="updateBusiness"> 
		<portlet:param name="<%=BusinessDisplayTerms.BUSINESS_BUSINESSID %>" value="<%=String.valueOf(business.getBusinessId()) %>"/>
		<portlet:param name="mvcPath" value="/html/portlets/accountmgt/admin/update_profile.jsp"/>
	</portlet:renderURL>
	<c:if test="<%=BusinessPermission.contains(permissionChecker, scopeGroupId, ActionKeys.UPDATE) %>">
		<liferay-ui:icon 
			image="edit" 
			message="edit"
			url="<%=updateBusiness.toString()%>" />
	</c:if>
	
	
	
	<portlet:actionURL var="changeAccountStatusBusinessURL" name="changeAccountStatusBusiness">
		<portlet:param name="<%=BusinessDisplayTerms.BUSINESS_ACCOUNTSTATUS %>" value="<%=String.valueOf(business.getAccountStatus()) %>"/>
		<portlet:param name="<%=BusinessDisplayTerms.BUSINESS_BUSINESSID %>" value="<%=String.valueOf(business.getBusinessId()) %>"/>
	</portlet:actionURL>
	<c:if test="<%=BusinessPermission.contains(permissionChecker, scopeGroupId, ActionKeys.UPDATE) %>">
		<liferay-ui:icon 
			image="status_online" 
			message="<%=accountActionName %>"
			url="<%= changeAccountStatusBusinessURL.toString()%>" />
	</c:if>
	
</liferay-ui:icon-menu>