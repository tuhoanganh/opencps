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
<%@page import="org.opencps.usermgt.util.UserMgtUtil"%>
<%@page import="org.opencps.usermgt.search.JobPosDisplayTerms"%>
<%
	long workingUnitId = ParamUtil.getLong(request, "workingUnitId");
	long jobPosId = ParamUtil.getLong(request, JobPosDisplayTerms.ID_JOBPOS);
	String backURL = ParamUtil.getString(request, "backURL");
	String [] jobPosSections = {"general_jobpos","role_jobpos"};
	String [][] categorySections = {jobPosSections};
%>

<portlet:actionURL var="updateJobPosURL" name="updateJobPos"/>

<liferay-util:buffer var="htmlTop">

</liferay-util:buffer>

<liferay-util:buffer var="htmlBot">

</liferay-util:buffer>

<aui:form name="fm" 
	method="post" 
	action="<%=updateJobPosURL.toString() %>">
	<liferay-ui:form-navigator 
		backURL="<%= backURL %>"
		categoryNames= "<%= UserMgtUtil._JOBPOS_CATEGORY_NAMES %>"	
		categorySections="<%=categorySections %>" 
		htmlBottom="<%= htmlBot %>"
		htmlTop="<%= htmlTop %>"
		jspPath='<%=templatePath + "jobpos/" %>'
		>	
	</liferay-ui:form-navigator>
	<aui:input name="<%=JobPosDisplayTerms.ID_JOBPOS %>" 
		type="hidden" />
</aui:form>

