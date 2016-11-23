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

<%@page import="javax.portlet.PortletURL"%>
<%@page import="org.opencps.util.ActionKeys"%>
<%@page import="com.liferay.portal.service.permission.PortletPermissionUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="org.opencps.processmgt.permissions.ProcessOrderPermission"%>
<%@page import="org.opencps.processmgt.util.ProcessUtils"%>

<%@ include file="../init.jsp"%>

<%

	String[] names = new String[]{ProcessUtils.TOP_TABS_PROCESS_ORDER_WAITING_PROCESS, ProcessUtils.TOP_TABS_PROCESS_ORDER_FINISHED_PROCESSING};

	String value = ParamUtil.getString(request, "tabs1", ProcessUtils.TOP_TABS_PROCESS_ORDER_WAITING_PROCESS);

	List<String> urls = new ArrayList<String>();
	
	if (PortletPermissionUtil.contains(permissionChecker, plid, portletDisplay.getId(), ActionKeys.CONFIGURATION) && 
			ProcessOrderPermission.contains(permissionChecker, scopeGroupId, ActionKeys.CONFIGURATION)) {
		PortletURL configToDoListURL = renderResponse.createRenderURL();
		configToDoListURL.setParameter("mvcPath", templatePath + "/configuration/todolist.jsp");
		configToDoListURL.setParameter("tabs1", "processorder-todolist");
		urls.add(configToDoListURL.toString());
	}
	
	if (PortletPermissionUtil.contains(permissionChecker, plid, portletDisplay.getId(), ActionKeys.CONFIGURATION) && 
			ProcessOrderPermission.contains(permissionChecker, scopeGroupId, ActionKeys.CONFIGURATION)) {
		PortletURL configJustFinishedListURL = renderResponse.createRenderURL();
		configJustFinishedListURL.setParameter("mvcPath", templatePath + "/configuration/justfinishedlist.jsp");
		configJustFinishedListURL.setParameter("tabs1", "processorder-justfinishedlist");
		urls.add(configJustFinishedListURL.toString());
	}
	
	if (PortletPermissionUtil.contains(permissionChecker, plid, portletDisplay.getId(), ActionKeys.CONFIGURATION) && 
			ProcessOrderPermission.contains(permissionChecker, scopeGroupId, ActionKeys.CONFIGURATION)) {
		PortletURL configSignatureURL = renderResponse.createRenderURL();
		configSignatureURL.setParameter("mvcPath", templatePath + "/configuration/signature.jsp");
		configSignatureURL.setParameter("tabs1", "signature");
		urls.add(configSignatureURL.toString());
	}
	
%>
<div class="opencps-toptabs">
	<div class="container">
		<liferay-ui:tabs
			names="<%= StringUtil.merge(names) %>"
			param="tabs1"
			url0="<%=urls != null && urls.size() > 0 ? urls.get(0): StringPool.BLANK %>"
			url1="<%=urls != null && urls.size() > 1 ? urls.get(1): StringPool.BLANK %>"
			url2="<%=urls != null && urls.size() > 2 ? urls.get(2): StringPool.BLANK %>"
		/>
	</div>
</div>