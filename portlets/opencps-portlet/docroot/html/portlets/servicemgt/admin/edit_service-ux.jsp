<%@page import="org.opencps.dossiermgt.model.ServiceConfig"%>
<%@page import="org.opencps.dossiermgt.service.ServiceConfigLocalServiceUtil"%>
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
	ServiceInfo serviceInfo = (ServiceInfo) request.getAttribute(WebKeys.SERVICE_ENTRY);
	long serviceInfoId = (serviceInfo != null) ? serviceInfo.getServiceinfoId() : 0L;
	long submitOnlinePlid = PortalUtil.getPlidFromPortletId(scopeGroupId, true,  WebKeys.P26_SUBMIT_ONLINE);
	
	long plidResLong = 0;
	
	if(Long.valueOf(plidRes) ==0) {
		plidResLong = submitOnlinePlid;
	} else {
		plidResLong = Long.valueOf(plidRes);
	}
	
	ServiceConfig serviceConfig = null;

	try {
		serviceConfig = ServiceConfigLocalServiceUtil.getServiceConfigByG_S(scopeGroupId, serviceInfoId);
	} catch (Exception e) {
		
	}
%>
<liferay-portlet:renderURL 
		var="servieOnlinePopURL" 
		portletName="<%=WebKeys.P26_SUBMIT_ONLINE %>"
		plid="<%=plidResLong %>"
		portletMode="VIEW"
	>
		<portlet:param name="mvcPath" value="/html/portlets/dossiermgt/submit/dossier_submit_online.jsp"/>
		<portlet:param name="serviceinfoId" value="<%=String.valueOf(serviceInfoId) %>"/>
</liferay-portlet:renderURL>

<aui:model-context bean="<%= serviceInfo %>" model="<%= ServiceInfo.class %>"/>
<div>
</div>