
<%@page import="org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil"%>
<%@page import="org.opencps.servicemgt.model.ServiceInfo"%>
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
<%@page import="org.opencps.util.PortletUtil"%>
<%@page import="com.liferay.portal.service.UserLocalServiceUtil"%>
<%@page import="com.liferay.portal.model.User"%>
<%@page import="org.opencps.util.DateTimeUtil"%>
<%@page import="org.opencps.dossiermgt.service.DossierLogLocalServiceUtil"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="org.opencps.dossiermgt.model.DossierLog"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="org.opencps.dossiermgt.model.Dossier"%>
<%@ include file="../../init.jsp"%>

<%
	Dossier dossier = (Dossier) request.getAttribute(WebKeys.DOSSIER_ENTRY);
	long dossierId = dossier != null ? dossier.getDossierId() : 0L;
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", "/html/portlets/dossiermgt/frontoffice/dossier/history.jsp");
	List<DossierLog> dossierLogs = new ArrayList<DossierLog>();
	
	String serviceName = StringPool.BLANK;
	String receptionNo = StringPool.BLANK;
	ServiceInfo serviceInfo = null;
	if(Validator.isNotNull(dossier)) {
		receptionNo = dossier.getReceptionNo();
		try {
			serviceInfo = ServiceInfoLocalServiceUtil.getServiceInfo(dossier.getServiceInfoId());
			serviceName = serviceInfo.getServiceName();
		} catch (Exception e) {
			
		}
	}
	
%>
<div class="ocps-title-detail">
	<div class="ocps-title-detail-top">
		<label class="service-reception-label">
			<liferay-ui:message key="reception-no"/> 
		</label>
		<p class="service-reception-no"><%=receptionNo %></p>
	</div>
	<div class="ocps-title-detail-bot">
		<label class="service-name-label">
			<liferay-ui:message key="dossier-name"/> 
		</label>
		<p class="service-service-name"><%=serviceName%></p>
	</div>
</div>

<div class="bound-search-container-history">
	<liferay-ui:search-container 
		emptyResultsMessage="no-history-were-found"
		iteratorURL="<%=iteratorURL %>"
		delta="<%=20 %>"
		deltaConfigurable="true"
		>
		<liferay-ui:search-container-results>
			<%
				dossierLogs = DossierLogLocalServiceUtil.getDossierLogByDossierId(dossierId, searchContainer.getStart(), searchContainer.getEnd());
				
				results = dossierLogs;
				total = DossierLogLocalServiceUtil.countDossierLogByDossierId(dossierId);
				
				pageContext.setAttribute("results", results);
				pageContext.setAttribute("total", total);
			%>
		</liferay-ui:search-container-results>
		<liferay-ui:search-container-row 
					className="org.opencps.dossiermgt.model.DossierLog" 
					modelVar="dossierLog" 
					keyProperty="dossierLogId"
				>
				
				<%-- <liferay-ui:search-container-column-text 
					name="row" 
					value="<%=String.valueOf(row.getPos() +1) %>" 
				/> --%>
				
				<%-- <liferay-ui:search-container-column-text 
					name="time" 
					value="<%=DateTimeUtil.convertDateToString(dossierLog.getUpdateDatetime(), DateTimeUtil._VN_DATE_TIME_FORMAT) %>" 
				/> --%>
				
				<%-- <liferay-ui:search-container-column-text 
					name="	" 
					value="<%=dossierLog.getSyncStatus() %>" 
				/> --%>
				 
				<%-- <liferay-ui:search-container-column-text 
					name="action" 
					value="<%= dossierLog.getActionInfo() %>" 
				/> --%>
				
				<%-- <liferay-ui:search-container-column-text 
					name="note" 
					value="<%=dossierLog.getMessageInfo() %>" 
				/> --%>
				
				<liferay-ui:search-container-column-jsp path="/html/portlets/dossiermgt/frontoffice/dossier/history-bound-data-second.jsp" />
				<liferay-ui:search-container-column-jsp path="/html/portlets/dossiermgt/frontoffice/dossier/history-bound-data.jsp" />
				
		</liferay-ui:search-container-row>
	<liferay-ui:search-iterator paginate="false"/>
	</liferay-ui:search-container>
</div>
