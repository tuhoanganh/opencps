<%@page import="org.opencps.processmgt.model.ServiceProcess"%>
<%@page import="org.opencps.processmgt.model.ProcessStep"%>
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

<%@ include file="../../init.jsp" %>

<%
	ProcessStep step = (ProcessStep) request.getAttribute(WebKeys.PROCESS_STEP_ENTRY);

	ServiceProcess serviceProcess  = (ServiceProcess) request.getAttribute(WebKeys.SERVICE_PROCESS_ENTRY);
%>

<liferay-portlet:renderURL var="editStepURL" windowState="<%= LiferayWindowState.POP_UP.toString() %>">
	<portlet:param name="mvcPath" value='<%= templatePath + "edit_step.jsp" %>'/>
</liferay-portlet:renderURL>

<liferay-portlet:renderURL var="editStepURL2" windowState="<%= LiferayWindowState.POP_UP.toString() %>">
	<portlet:param name="mvcPath" value='<%= templatePath + "edit_step.jsp" %>'/>
</liferay-portlet:renderURL>

<aui:button-row>
	<aui:button name="addStep" onClick="showDialog()" value="add-step" ></aui:button>
</aui:button-row>

<aui:script use="liferay-util-window">
	Liferay.provide(window, 'showDialog', function(action) {
		page = '<%=editStepURL%>'
		Liferay.Util.openWindow({
			dialog: {
				cache: false,
				centered: true,
				modal: true,
				resizable: false,
				width: 1000
			},
			id: 'addstep',
			title: 'adding-process-step',
			uri: page
		});
	});
</aui:script>

<aui:script>
	Liferay.provide(window, 'refreshPortlet', function() 
		{	
// 			alert("refreshPortlet");
// 			var curPortlet = '#p_p_id<portlet:namespace/>';
// 			Liferay.Portlet.refresh(curPortlet);
			window.location.reload();
			//window.opener.location.href = window.opener.location;
		},
		['aui-dialog','aui-dialog-iframe']
	);
</aui:script>

<aui:script>
	Liferay.provide(window, 'closePopup', function(dialogId) 
		{
			var A = AUI();
			var dialog = Liferay.Util.Window.getById(dialogId);
			dialog.destroy();
		},
		['liferay-util-window']
	);
</aui:script>
