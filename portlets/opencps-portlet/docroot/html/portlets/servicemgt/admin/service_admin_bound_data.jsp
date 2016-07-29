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
<%@page import="org.opencps.util.DictItemUtil"%>
<%@page import="com.liferay.portal.kernel.dao.search.ResultRow"%>
<%
	ResultRow row = (ResultRow) request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);
 	ServiceInfo info = (ServiceInfo) row.getObject();
 	
 	int status = info.getActiveStatus();
	String statusMess = StringPool.BLANK;
	if(status == 0) {
		statusMess = LanguageUtil.get(portletConfig, themeDisplay.getLocale(), "service-private");
	} else if(status == 1) {
		statusMess = LanguageUtil.get(portletConfig, themeDisplay.getLocale(), "service-public");
	} else if(status == 2) {
		statusMess = LanguageUtil.get(portletConfig, themeDisplay.getLocale(), "service-outdate");
	}
	
	PortletURL editURL = renderResponse.createRenderURL();
	editURL.setParameter("mvcPath", templatePath + "edit_service-ux.jsp");
	editURL.setParameter("serviceinfoId", String.valueOf(info.getServiceinfoId()));
	editURL.setParameter("backURL", currentURL);
%>

<div class="ocps-searh-bound-data">
	<p class="ocps-searh-bound-data-chirld-p">
		<span class="ocps-searh-bound-data-chirld-span">
			<liferay-ui:message key="service-domain" />
		</span>
		<a class="ocps-searh-bound-data-chirld-a" href="<%=editURL.toString()%>">
			<%= DictItemUtil.getNameDictItem(info.getDomainCode()) %>
		</a>
	</p>
	
	<p class="ocps-searh-bound-data-chirld-p">
		<span class="ocps-searh-bound-data-chirld-span">
			<liferay-ui:message key="service-administrator" />
		</span>
		<a class="ocps-searh-bound-data-chirld-a" href="<%=editURL.toString()%>">
			<%=DictItemUtil.getNameDictItem(info.getAdministrationCode()) %>
		</a>
	</p>
	
	<p class="ocps-searh-bound-data-chirld-p">
		<span class="ocps-searh-bound-data-chirld-span">
			<liferay-ui:message key="status" />
		</span>
		<a class="ocps-searh-bound-data-chirld-a" href="<%=editURL.toString()%>">
			<%=statusMess %>
		</a>
	</p>

</div>
