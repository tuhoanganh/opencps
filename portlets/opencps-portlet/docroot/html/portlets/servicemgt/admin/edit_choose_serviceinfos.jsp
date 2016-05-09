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
<%@ include file="../init.jsp" %>

<%
	TemplateFile templateFile = (TemplateFile) request.getAttribute(WebKeys.SERVICE_TEMPLATE_ENTRY);
	long templateFileId = Validator.isNotNull(templateFile) ? templateFile.getTemplatefileId() : 0L;
	String [] chooseServiceInf = new String[] {"check_serviceinfos"};
	String[][] categorySections = {chooseServiceInf};
	
	String backURL = ParamUtil.getString(request, "backURL");
%>

<liferay-ui:header
	backURL="<%= backURL %>"
	title="choose-serviceinfos"
	backLabel="back"
/>

<portlet:actionURL name="chooseServiceInfo" var="chooseServiceInfoURL" >
	<portlet:param name="backURL" value="<%= backURL %>"/>
	<portlet:param name="templateFileId" value="<%=String.valueOf(templateFileId) %>"/>
</portlet:actionURL>

<liferay-util:buffer var="htmlTop">
	<c:if test="<%=templateFile!=null %>">
		<h5><%=templateFile.getFileName() %></h5>
	</c:if>
</liferay-util:buffer>

<liferay-util:buffer var="htmlBot" >
</liferay-util:buffer>

<aui:form name="fm" 
	method="post" 
	action="<%=chooseServiceInfoURL.toString() %>">
	<liferay-ui:form-navigator 
		backURL="<%= currentURL %>"
		categoryNames= "<%=ServiceUtil.CHOOSE_SERVICE_INFO_NAMES %>"	
		categorySections="<%=categorySections %>" 
		htmlBottom="<%= htmlBot %>"
		htmlTop="<%= htmlTop %>"
		jspPath='<%=templatePath + "choose_serviceinfos/" %>'
		>	
	</liferay-ui:form-navigator>
</aui:form>