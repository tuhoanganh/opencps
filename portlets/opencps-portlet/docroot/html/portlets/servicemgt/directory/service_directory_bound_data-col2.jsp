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
 	
 	PortletURL viewURL = renderResponse.createRenderURL();
	viewURL.setParameter("mvcPath", templatePath + "service_detail.jsp");
	viewURL.setParameter("serviceinfoId", String.valueOf(info.getServiceinfoId()));
	viewURL.setParameter("backURL", currentURL);
%>

<div class="ocps-searh-bound-data">
	<p class="ocps-searh-bound-data-chirld-p">
		<span class="ocps-searh-bound-data-chirld-span">
			<liferay-ui:message key="service-name" />
		</span>
		<a class="ocps-searh-bound-data-chirld-label" href="<%=viewURL.toString()%>">
			<%=info.getServiceName() %>
		</a>
	</p>

</div>
