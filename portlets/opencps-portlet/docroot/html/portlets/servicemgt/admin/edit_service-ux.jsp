
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
<%@page import="com.liferay.portal.kernel.util.UnicodeFormatter"%>
<%@page import="org.opencps.dossiermgt.model.ServiceConfig"%>
<%@page import="org.opencps.dossiermgt.service.ServiceConfigLocalServiceUtil"%>
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
	
	String backURL = ParamUtil.getString(request, "backURL");
	
	List<TemplateFile> templates = new ArrayList<TemplateFile>();
	
	int count = 0;

	if (Validator.isNotNull(serviceInfo)) {
		templates = TemplateFileLocalServiceUtil.getServiceTemplateFiles(serviceInfo.getServiceinfoId());
		count = TemplateFileLocalServiceUtil.countServiceTemplateFile(serviceInfo.getServiceinfoId());
	}
	String revIcon = StringPool.BLANK;
	String templateIdsString = ParamUtil.getString(request, "templateSearchContainerPrimaryKeys");
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

<div class="ocps-custom-header">
	<label class="opcps-label">
		<liferay-ui:message key='<%=Validator.isNotNull(serviceInfo) ? "update-service" : "add-service" %>' />
	</label>
	<span class="ocps-span">
		<a href="<%=backURL %>"><liferay-ui:message key="back"/></a>
	</span>
</div>

<portlet:actionURL name="updateService" var="updateServiceURL"/>
<aui:form name="fm" action="<%=updateServiceURL %>" method="post">

	<aui:model-context bean="<%= serviceInfo %>" model="<%= ServiceInfo.class %>"/>

	<aui:model-context bean="<%= serviceInfo %>" model="<%= ServiceInfo.class %>" />
	
	<aui:input name="redirectURL" type="hidden" value="<%= backURL%>"/>
	<aui:input name="returnURL" type="hidden" value="<%= currentURL%>"/>
	
	<aui:input name="<%= ServiceDisplayTerms.GROUP_ID %>" type="hidden" 
		value="<%= scopeGroupId%>"/>
	<aui:input name="<%= ServiceDisplayTerms.COMPANY_ID %>" type="hidden" 
		value="<%= company.getCompanyId()%>"/>
	<aui:input name="<%= ServiceDisplayTerms.SERVICE_ID %>" type="hidden" 
		value="<%= Validator.isNotNull(serviceInfo) ? serviceInfo.getServiceinfoId() : StringPool.BLANK %>"/>
	<div class="ocps-update-service-bound-all">	
		<div class="ocps-update-service-bound-general">
		<a href="<%=currentURL.toString() %>">
			<liferay-ui:message key="general-service"/>
		</a>
			<aui:row>
				<aui:col width="100" cssClass="ocps-edit-serviceinfo-col-only">
					<aui:input name="<%= ServiceDisplayTerms.SERVICE_NAME %>" ></aui:input>
				</aui:col>
			</aui:row>
			
			<aui:row>
				<aui:col width="100" cssClass="ocps-edit-serviceinfo-col-only">
					<aui:input name="<%= ServiceDisplayTerms.SERVICE_FULLNAME %>"></aui:input>
				</aui:col>
			</aui:row>
			
			<aui:row>
				<aui:col cssClass="ocps-edit-serviceinfo-col">
					<datamgt:ddr 
						cssClass="input100"
						depthLevel="1" 
						dictCollectionCode="SERVICE_ADMINISTRATION"
						itemNames="<%= ServiceDisplayTerms.SERVICE_ADMINISTRATION %>"
						itemsEmptyOption="true"
						selectedItems="<%= Validator.isNotNull(serviceInfo) ? serviceInfo.getAdministrationCode() : StringPool.BLANK %>"
					>
					</datamgt:ddr>
				</aui:col>
				
				<aui:col width="30" cssClass="ocps-edit-serviceinfo-col">
					<datamgt:ddr 
						cssClass="input100"
						depthLevel="1" 
						dictCollectionCode="SERVICE_DOMAIN"
						itemNames="<%= ServiceDisplayTerms.SERVICE_DOMAINCODE %>"
						itemsEmptyOption="true"
						selectedItems="<%= Validator.isNotNull(serviceInfo) ? serviceInfo.getDomainCode() : StringPool.BLANK %>"
					>
					</datamgt:ddr>
				</aui:col>
				
				<aui:col width="30" cssClass="ocps-edit-serviceinfo-col">
					<aui:select name="<%= ServiceDisplayTerms.SERVICE_ACTIVESTATUS %>" showEmptyOption="true">
						<aui:option value="0">
							<liferay-ui:message key="service-private"/>
						</aui:option>
						<aui:option value="1">
							<liferay-ui:message key="service-public"/>
						</aui:option>
						<aui:option value="2">
							<liferay-ui:message key="service-outdate"/>
						</aui:option>
					</aui:select>
				</aui:col>
			</aui:row>
			
			
			<aui:row>
				<aui:col width="30" cssClass="ocps-edit-serviceinfo-col">
					<aui:input name="<%= ServiceDisplayTerms.SERVICE_NO %>"></aui:input>
				</aui:col>
				
				<aui:col width="30" cssClass="ocps-edit-serviceinfo-col">
					<c:choose>
						<c:when test="<%=Validator.isNotNull(serviceInfo) && Validator.isNull(serviceInfo.getOnlineUrl())%>">
							<aui:input cssClass="input100" name="urlOnline" type="text" value="<%=servieOnlinePopURL.toString() %>"/>
						</c:when>
						<c:otherwise>
							<aui:input cssClass="input100" name="<%= ServiceDisplayTerms.SERVICE_ONLINEURL %>" />
						</c:otherwise>
					</c:choose>	
				</aui:col>
			</aui:row>
		</div>
		
		<div class="ocps-update-service-bound-detail">
		
		<a href="<%=currentURL.toString() %>">
			<liferay-ui:message key="detail-service"/>
		</a>
		
			<liferay-util:include 
				page="/html/portlets/servicemgt/admin/service/detail_info.jsp" 
				servletContext="<%=application %>" 
			/>
			
		</div >
		
		<div class = "ocps-update-service-bound-template">
			<liferay-util:include 
				page="/html/portlets/servicemgt/admin/service/template_info.jsp" 
				servletContext="<%=application %>" 
			/>
		</div>
	</div>
	
	<aui:button type="submit" value="submit"/>
</aui:form>