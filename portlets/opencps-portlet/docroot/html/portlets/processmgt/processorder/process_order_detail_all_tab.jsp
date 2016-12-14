
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

<%@ include file="../init.jsp"%>

<%
	ProcessOrder processOrder = (ProcessOrder)request.getAttribute(WebKeys.PROCESS_ORDER_ENTRY);
	ProcessStep processStep = (ProcessStep)request.getAttribute(WebKeys.PROCESS_STEP_ENTRY);
	Dossier dossier = (Dossier)request.getAttribute(WebKeys.DOSSIER_ENTRY);
	ServiceProcess serviceProcess = (ServiceProcess)request.getAttribute(WebKeys.SERVICE_PROCESS_ENTRY);
	ServiceInfo serviceInfo = (ServiceInfo)request.getAttribute(WebKeys.SERVICE_INFO_ENTRY);
	ServiceConfig serviceConfig = (ServiceConfig)request.getAttribute(WebKeys.SERVICE_CONFIG_ENTRY);
	
	String backURL = ParamUtil.getString(request, "backURL");
%>

<liferay-ui:header
	backURL="<%= backURL %>"
	title="process-order"
/>

<div class="ocps-history-process-bound-navigator">
	<liferay-util:buffer var="htmlTop">
		<c:if test="<%= processOrder != null %>">
			<div class="form-navigator-topper dossier-info">
				<div class="form-navigator-container">
					<i aria-hidden="true" class="fa fa-suitcase"></i>
					<span class="form-navigator-topper-name"><%= Validator.isNotNull(dossier.getReceptionNo()) ? dossier.getReceptionNo() : StringPool.BLANK %></span>
				</div>
			</div>
		</c:if> 
	</liferay-util:buffer>
	
	<liferay-util:buffer var="htmlBottom">
	
	</liferay-util:buffer>
	
	<aui:row>
		<aui:col width="25">
			<div style="margin-bottom: 25px;" class="opencps-searchcontainer-wrapper default-box-shadow radius8">
				<p><aui:a href="#dossier-content"><%=LanguageUtil.get(pageContext, "dossier_content") %></aui:a></p>
				<p><aui:a href="#dossier-info"><%=LanguageUtil.get(pageContext, "dossier-info") %></aui:a></p>
				<p><aui:a href="#process"><%=LanguageUtil.get(pageContext, "process") %></aui:a></p>
				<p><aui:a href="#history"><%=LanguageUtil.get(pageContext, "history") %></aui:a></p>
			</div>
		</aui:col>
		
		<aui:col width="75">
			<div class="opencps-searchcontainer-wrapper default-box-shadow radius8">
				<aui:form name="pofm" action="" method="post">
				
					<aui:model-context bean="<%= processOrder %>" model="<%= ProcessOrder.class %>" />
					
					<aui:input 
						name="redirectURL" 
						type="hidden" 
						value="<%= backURL%>"
					/>
					<aui:input 
						name="returnURL" 
						type="hidden" 
						value="<%= currentURL%>"
					/>
					
					<div id="dossier-content">
						<liferay-ui:panel title="dossier_content" collapsible="false" >
							<liferay-util:include page='<%=templatePath + "/dossier/dossier_content.jsp" %>' servletContext="<%=application %>" />
						</liferay-ui:panel>
					</div>
					<div id="dossier-info">
						<liferay-ui:panel title="dossier-info" collapsible="false">
							<liferay-util:include page='<%=templatePath + "/dossier/dossier_info.jsp" %>' servletContext="<%=application %>" />
						</liferay-ui:panel>
					</div>
					<div id="process">
						<liferay-ui:panel title="process" collapsible="false">
							<liferay-util:include page='<%=templatePath + "/dossier/process.jsp" %>' servletContext="<%=application %>" />
						</liferay-ui:panel>
					</div>
					<div id="history">
						<liferay-ui:panel title="history" collapsible="false">
							<liferay-util:include page='<%=templatePath + "/dossier/history.jsp" %>' servletContext="<%=application %>" />
						</liferay-ui:panel>
					</div>
					
				</aui:form>
			</div>
		</aui:col>
	</aui:row>
	
	
</div>


<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.processmgt.processorder.process_order_detail.jsp");
%>