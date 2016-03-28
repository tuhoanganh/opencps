<%@page import="org.opencps.servicemgt.util.ServiceUtil"%>
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

	String[] names = new String[]{ServiceUtil.TOP_TABS_SERVICE, 
		ServiceUtil.TOP_TABS_DOCUMENT, ServiceUtil.TOP_TABS_CATEGORY,
		ServiceUtil.TOP_TABS_LEVEL};

	String value = 
		ParamUtil.getString(request, "tabs1", ServiceUtil.TOP_TABS_SERVICE);

	List<String> urls = new ArrayList<String>();
	
	if (PortletPermissionUtil.contains(permissionChecker, plid, portletDisplay.getId(),
		ActionKeys.VIEW) && 
			ServicePermission.contains(permissionChecker, scopeGroupId, ActionKeys.VIEW)) {
		PortletURL viewServiceURL = renderResponse.createRenderURL();
		viewServiceURL.setParameter("mvcPath", templatePath + "serviceinfolist.jsp");
		viewServiceURL.setParameter("tabs1", ServiceUtil.TOP_TABS_SERVICE);
		urls.add(viewServiceURL.toString());
	}
	
	if (PortletPermissionUtil.contains(permissionChecker, plid, portletDisplay.getId(), 
		ActionKeys.VIEW) && 
					DocumentPermission.contains(permissionChecker, scopeGroupId, ActionKeys.VIEW)) {
				PortletURL viewServiceURL = renderResponse.createRenderURL();
				viewServiceURL.setParameter("mvcPath", templatePath + "templatefilelist.jsp");
				viewServiceURL.setParameter("tabs1", ServiceUtil.TOP_TABS_DOCUMENT);
				urls.add(viewServiceURL.toString());
	}

	if (PortletPermissionUtil.contains(permissionChecker, plid, portletDisplay.getId(), 
		ActionKeys.VIEW) && 
					CategoryPermission.contains(permissionChecker, scopeGroupId, ActionKeys.VIEW)) {
				PortletURL viewServiceURL = renderResponse.createRenderURL();
				viewServiceURL.setParameter("mvcPath", templatePath + "categorylist.jsp");
				viewServiceURL.setParameter("tabs1", ServiceUtil.TOP_TABS_CATEGORY);
				urls.add(viewServiceURL.toString());
	}

	if (PortletPermissionUtil.contains(permissionChecker, plid, portletDisplay.getId(), 
		ActionKeys.VIEW) && 
					LevelPermission.contains(permissionChecker, scopeGroupId, ActionKeys.VIEW)) {
				PortletURL viewServiceURL = renderResponse.createRenderURL();
				viewServiceURL.setParameter("mvcPath", templatePath + "levellist.jsp");
				viewServiceURL.setParameter("tabs1", ServiceUtil.TOP_TABS_LEVEL);
				urls.add(viewServiceURL.toString());
	}
	
%>
<liferay-ui:tabs
	names="<%= StringUtil.merge(names) %>"
	param="tabs1"
	url0="<%=urls != null && urls.size() > 0 ? urls.get(0): StringPool.BLANK %>"
	url1="<%=urls != null && urls.size() > 1 ? urls.get(1): StringPool.BLANK %>"
	url2="<%=urls != null && urls.size() > 2 ? urls.get(2): StringPool.BLANK %>"
	url3="<%=urls != null && urls.size() > 3 ? urls.get(3): StringPool.BLANK %>"
/>
