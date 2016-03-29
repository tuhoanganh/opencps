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

<%@ include file="../init.jsp" %>

<%
	String redirectURL = ParamUtil.getString(request, "redirectURL");

	ServiceInfo servieInfo = (ServiceInfo) request.getAttribute(WebKeys.SERVICE_ENTRY);
	
	String backURL = ParamUtil.getString(request, "backURL");
%>

<liferay-ui:header
	backURL="<%= backURL %>"
	title='<%= (Validator.isNull(servieInfo)) ? "add-service" : "update-service" %>'
/>

<portlet:actionURL name="updateService" var="updateServiceURL"/>

<aui:form name="fm" action="<%=updateServiceURL %>" method="post">

	<aui:model-context bean="<%=servieInfo %>" model="<%= ServiceInfo.class %>" />
	
	<aui:input name="redirectURL" type="hidden" value="<%= backURL%>"/>
	<aui:input name="returnURL" type="hidden" value="<%= currentURL%>"/>
	
	<aui:input name="<%= ServiceDisplyTerms.GROUP_ID %>" type="hidden" value="<%= scopeGroupId%>"/>
	<aui:input name="<%= ServiceDisplyTerms.COMPANY_ID %>" type="hidden" value="<%= company.getCompanyId()%>"/>

	<liferay-ui:form-navigator
		backURL="<%= backURL %>"
		categoryNames="<%= UserMgtUtil._EMPLOYESS_CATEGORY_NAMES %>"
		categorySections="<%= categorySections %>"
		htmlBottom="<%= htmlBottom %>"
		htmlTop="<%= htmlTop %>"
		jspPath='<%= templatePath + "employees/" %>'
	/>
</aui:form>


