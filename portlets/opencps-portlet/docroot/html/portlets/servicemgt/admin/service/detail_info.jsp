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
	ServiceInfo serviceInfo = (ServiceInfo) request.getAttribute(WebKeys.SERVICE_ENTRY);
%>

<aui:model-context bean="<%= serviceInfo %>" model="<%= ServiceInfo.class %>"/>

<aui:row >
	<aui:col width="100">
		<div class="label">
			<liferay-ui:message key="service-process"/>
		</div>
		<liferay-ui:input-editor name="<%= ServiceDisplyTerms.SERVICE_PROCESS %>" 
			toolbarSet="simple" />
	</aui:col>
</aui:row>

<aui:row >
	<div class="label">
		<liferay-ui:message key="service-method"/>
	</div>
	<aui:col width="100">
		<liferay-ui:input-editor name="<%= ServiceDisplyTerms.SERVICE_METHOD %>" 
			toolbarSet="simple" />
	</aui:col>
</aui:row>

<aui:row >
	<div class="label">
		<liferay-ui:message key="service-dossier"/>
	</div>
	<aui:col width="100">
		<liferay-ui:input-editor name="<%= ServiceDisplyTerms.SERVICE_DOSSIER %>" 
			toolbarSet="simple" height="100"/>
	</aui:col>
</aui:row>

<aui:row >
	<div class="label">
		<liferay-ui:message key="service-condition"/>
	</div>
	<aui:col width="100">
		<liferay-ui:input-editor name="<%= ServiceDisplyTerms.SERVICE_CONDITION %>" 
			toolbarSet="simple" height="100"/>
	</aui:col>
</aui:row>

<aui:row >
	<div class="label">
		<liferay-ui:message key="service-duration"/>
	</div>
	<aui:col width="100">
		<liferay-ui:input-editor name="<%= ServiceDisplyTerms.SERVICE_DURATION %>" 
			toolbarSet="simple" height="100"/>
	</aui:col>
</aui:row>

<aui:row >
	<div class="label">
		<liferay-ui:message key="service-actors"/>
	</div>
	<aui:col width="100">
		<liferay-ui:input-editor name="<%= ServiceDisplyTerms.SERVICE_ACTORS %>" 
			toolbarSet="simple" height="100"/>
	</aui:col>
</aui:row>

<aui:row >
	<div class="label">
		<liferay-ui:message key="service-fee"/>
	</div>
	<aui:col width="100">
		<liferay-ui:input-editor name="<%= ServiceDisplyTerms.SERVICE_FEE %>" 
			toolbarSet="simple" height="100"/>
	</aui:col>
</aui:row>

<aui:row >
	<div class="label">
		<liferay-ui:message key="service-results"/>
	</div>
	<aui:col width="100">
		<liferay-ui:input-editor name="<%= ServiceDisplyTerms.SERVICE_RESULTS %>" 
			toolbarSet="simple" height="100"/>
	</aui:col>
</aui:row>

<aui:row >
	<div class="label">
		<liferay-ui:message key="service-records"/>
	</div>
	<aui:col width="100">
		<liferay-ui:input-editor name="<%= ServiceDisplyTerms.SERVICE_RECORDS %>" 
			toolbarSet="simple" height="100"/>
	</aui:col>
</aui:row>

<aui:row >
	<div class="label">
		<liferay-ui:message key="service-instructions"/>
	</div>
	<aui:col width="100">
		<liferay-ui:input-editor name="<%= ServiceDisplyTerms.SERVICE_INSTRUCTIONS %>" 
			toolbarSet="simple" height="100"/>
	</aui:col>
</aui:row>

