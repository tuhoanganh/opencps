<%@page import="org.opencps.processmgt.util.ProcessUtils"%>
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
<%@ include file="../init.jsp"%>
<%
	ServiceProcess serviceProcess = (ServiceProcess) request.getAttribute(WebKeys.SERVICE_PROCESS_ENTRY);
	long serviceProcessId = Validator.isNotNull(serviceProcess) ? serviceProcess.getServiceProcessId() : 0L;
	String [] chooseServiceInf = new String[] {"serviceinfolist"};
	String[][] categorySections = {chooseServiceInf};
	
	String backURL = ParamUtil.getString(request, "backURL");
%>

<liferay-ui:header
	backURL="<%= backURL %>"
	title="choose-serviceinfos"
	backLabel="back"
/>

<portlet:actionURL name="chooseServiceInfoFromProcess" var="chooseServiceInfoFromProcessURL" >
	<portlet:param name="backURL" value="<%= backURL %>"/>
	<portlet:param name="serviceProcessId" value="<%=String.valueOf(serviceProcessId) %>"/>
</portlet:actionURL>

<liferay-util:buffer var="htmlTop">
	<c:if test="<%=Validator.isNotNull(serviceProcess) %>">
		<h5><%=serviceProcess.getProcessName() %></h5>
	</c:if>
</liferay-util:buffer>

<liferay-util:buffer var="htmlBot" >
</liferay-util:buffer>

<aui:form name="fm" 
	method="post" 
	action="<%=chooseServiceInfoFromProcessURL.toString() %>">
	<liferay-ui:form-navigator 
		backURL="<%= currentURL %>"
		categoryNames= '<%=new String [] {"choose-service"} %>'	
		categorySections="<%=categorySections %>" 
		htmlBottom="<%= htmlBot %>"
		htmlTop="<%= htmlTop %>"
		jspPath='<%=templatePath + "serviceinfolist_choose/" %>'
		>	
	</liferay-ui:form-navigator>
</aui:form>