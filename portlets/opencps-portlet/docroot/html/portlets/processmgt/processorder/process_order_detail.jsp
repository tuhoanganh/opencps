
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

<%@page import="org.opencps.processmgt.model.ProcessOrder"%>
<%@page import="org.opencps.dossiermgt.model.Dossier"%>
<%@page import="org.opencps.dossiermgt.model.DossierPart"%>
<%@page import="org.opencps.dossiermgt.model.ServiceConfig"%>
<%@page import="org.opencps.processmgt.util.ProcessUtils"%>

<%@ include file="init.jsp"%>

<c:choose>
	<c:when test="<%=processOrderDetailPage.equals("default") %>">
		<liferay-util:include page="<%=templatePath + "/display/processorder/detail/default.jsp" %>" />
	</c:when>
	<c:when test="<%=processOrderDetailPage.equals("process_order_detail") %>">
		<liferay-util:include page="<%=templatePath + "/display/processorder/detail/process_order_detail.jsp" %>" />
	</c:when>
</c:choose>

<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.processmgt.processorder.process_order_detail.jsp");
%>