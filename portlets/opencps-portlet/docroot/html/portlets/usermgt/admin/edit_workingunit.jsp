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
<%@page import="org.opencps.usermgt.search.WorkingUnitDisplayTerms"%>
<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@page import="org.opencps.usermgt.util.UserMgtUtil"%>
<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>

<%
	String backURL = ParamUtil.getString(request, "backURL");
	long workingUnitId = ParamUtil.getLong(request, WorkingUnitDisplayTerms.WORKINGUNIT_ID);
	String [] workingunitSections = null;
	if(workingUnitId == 0) {
		workingunitSections = new String[2];
		workingunitSections[0] = "general_workingunit";
		workingunitSections[1] = "contact_workingunit";
		
	} else {
		workingunitSections = new String[3];
		workingunitSections[0] = "general_workingunit";
		workingunitSections[1] = "contact_workingunit";
		workingunitSections[2] = "jobpos";
	}
	
				
	String[][] categorySections = {workingunitSections};
%>
<liferay-ui:error key="UPDATE_JOBPOS_ERROR" message="UPDATE JOBPOS ERROR!" />
<portlet:actionURL var="updateWorkingUnitURL" name="updateWorkingUnit"/>

<portlet:renderURL	var="dialogURL"	windowState="<%=LiferayWindowState.POP_UP.toString()%>">
	<portlet:param name="mvcPath" value='<%= templatePath + "jobpos.jsp" %>' />
	<portlet:param name="workingUnitId" value="<%=String.valueOf(workingUnitId) %>"/>
</portlet:renderURL>

<liferay-util:buffer var="htmlTop">
	<liferay-ui:icon iconCssClass="icon-home" />
</liferay-util:buffer>

<liferay-util:buffer var="htmlBot">

</liferay-util:buffer>

<aui:form name="fm" 
	method="post" 
	action="<%=updateWorkingUnitURL.toString() %>">
	<liferay-ui:form-navigator 
		backURL="<%= backURL %>"
		categoryNames= "<%= UserMgtUtil._WORKING_UNIT_CATEGORY_NAMES %>"	
		categorySections="<%=categorySections %>" 
		htmlBottom="<%= htmlBot %>"
		htmlTop="<%= htmlTop %>"
		jspPath='<%=templatePath + "workingunit/" %>'
		>	
	</liferay-ui:form-navigator>
	<aui:input 
		name="<%=WorkingUnitDisplayTerms.WORKINGUNIT_ID %>" 
		value="<%=String.valueOf(workingUnitId) %>"
		type="hidden"
	></aui:input>
</aui:form>

<aui:script use="liferay-util-window">
	AUI().ready(function(A){
		var onclickJobPos = A.one('#<portlet:namespace/>jobposLink');
		if(onclickJobPos) {
			onclickJobPos.on('click', function(event) {
				Liferay.Util.openWindow({
					dialog : {
						centered : true,
						
						modal : true
						
					},
					id : '<portlet:namespace/>dialog',
					title : 'Edit-JobPos',
					uri : '<%=dialogURL %>'
				});
			});
		}
	});
</aui:script>

<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.usermgt.admin.edit_workingunit.jsp");
%>