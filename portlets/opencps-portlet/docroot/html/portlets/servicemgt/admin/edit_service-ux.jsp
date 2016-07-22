<%@page import="com.liferay.portal.kernel.util.UnicodeFormatter"%>
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
		
		<liferay-ui:message key='<%=Validator.isNotNull(serviceInfo) ? "add-service" : "update-service" %>' />
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
		<div class="ocps-update-service-bound-top">
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
		
		<div class="ocps-update-service-bound-bot">
			<aui:row cssClass="opcps-rows">
				<aui:col width="100">
					<div class="label">
						<liferay-ui:message key="service-process"/>
					</div>
					<liferay-ui:input-editor name="<%= ServiceDisplayTerms.SERVICE_PROCESS %>" 
						toolbarSet="simple" initMethod="initProcess"/>
				</aui:col>
			</aui:row>
			
			<aui:row cssClass="opcps-rows">
				<aui:col width="100">
				<div class="label">
					<liferay-ui:message key="service-method"/>
				</div>
					<liferay-ui:input-editor name="<%= ServiceDisplayTerms.SERVICE_METHOD %>" 
						toolbarSet="simple" initMethod="initMethod"/>
				</aui:col>
			</aui:row>
			
			<aui:row >
				<aui:col width="100">
					<%-- <aui:input name="<%= ServiceDisplayTerms.SERVICE_DOSSIER %>" type="textarea" cssClass="txtarea-medium">
					</aui:input> --%>
					<liferay-ui:input-editor name="<%= ServiceDisplayTerms.SERVICE_DOSSIER %>" initMethod="service_dossier"/>
				</aui:col>
			</aui:row>
			
			<aui:row >
				<aui:col width="100">
					<%-- <aui:input name="<%= ServiceDisplayTerms.SERVICE_CONDITION %>" type="textarea" cssClass="txtarea-medium">
					</aui:input> --%>
					<liferay-ui:input-editor name="<%= ServiceDisplayTerms.SERVICE_CONDITION %>" initMethod="service_condition"/>
				</aui:col>
			</aui:row>
			
			<aui:row >
				<aui:col width="100">
					<%-- <aui:input name="<%= ServiceDisplayTerms.SERVICE_DURATION %>" type="textarea" cssClass="txtarea-medium">
					</aui:input>
					 --%>
					<liferay-ui:input-editor name="<%= ServiceDisplayTerms.SERVICE_DURATION %>" initMethod="service_duration"/>
				</aui:col>
			</aui:row>
			
			<aui:row >
				<aui:col width="100">
					<%-- <aui:input name="<%= ServiceDisplayTerms.SERVICE_ACTORS %>" type="textarea" cssClass="txtarea-medium">
					</aui:input> --%>
					
					<liferay-ui:input-editor name="<%= ServiceDisplayTerms.SERVICE_ACTORS %>" initMethod="service_actors"/>
				</aui:col>
			</aui:row>
			
			<aui:row >
				<aui:col width="100">
					<%-- <aui:input name="<%= ServiceDisplayTerms.SERVICE_FEE %>" type="textarea" cssClass="txtarea-medium">
					</aui:input>
					 --%>
					<liferay-ui:input-editor name="<%= ServiceDisplayTerms.SERVICE_FEE %>" initMethod="service_fee"/>
				</aui:col>
			</aui:row>
			
			<aui:row >
				<aui:col width="100">
					<%-- <aui:input name="<%= ServiceDisplayTerms.SERVICE_RESULTS %>" type="textarea" cssClass="txtarea-medium">
					</aui:input> --%>
					
					<liferay-ui:input-editor name="<%= ServiceDisplayTerms.SERVICE_RESULTS %>" initMethod="service_results"/>
				</aui:col>
			</aui:row>
			
			<aui:row >
				<aui:col width="100">
					<%-- <aui:input name="<%= ServiceDisplayTerms.SERVICE_RECORDS %>" type="textarea" cssClass="txtarea-medium">
					</aui:input> --%>
					
					<liferay-ui:input-editor name="<%= ServiceDisplayTerms.SERVICE_RECORDS %>" initMethod="service_records"/>
					
				</aui:col>
			</aui:row>
			
			<aui:row >
				<aui:col width="100">
					<div class="label">
						<liferay-ui:message key="service-instructions"/>
					</div>
					<liferay-ui:input-editor name="<%= ServiceDisplayTerms.SERVICE_INSTRUCTIONS %>" 
						toolbarSet="simple" initMethod="initInstructions"/>
				</aui:col>
			</aui:row>
		</div>
		
		<div>
			<aui:input name="addTemplateIds" type="hidden" value="<%= templateIdsString %>" />
				<aui:input name="deleteTemplateIds" type="hidden" />
				
				<liferay-util:buffer var="removeTemplateIcon">
					<liferay-ui:icon
						image="unlink"
						label="<%= true %>"
						message="remove"
					/>
				</liferay-util:buffer>
				<%
					revIcon = removeTemplateIcon.toString();
				%>
				<liferay-ui:error-marker key="errorSection" value="roles" />
				
				<liferay-ui:membership-policy-error />
				
				<liferay-ui:search-container
					headerNames="fileno,filename,null"
					id="templateSearchContainer"
				>
					<liferay-ui:search-container-results
						results="<%= templates %>"
						total="<%= count %>"
					/>
				
					<liferay-ui:search-container-row
						className="org.opencps.servicemgt.model.TemplateFile"
						keyProperty="templatefileId"
						modelVar="template"
					>
				
						<liferay-ui:search-container-column-text
							name="fileNo"
							value="<%= HtmlUtil.escape(template.getFileNo()) %>"
						/>
						<liferay-ui:search-container-column-text
							name="fileName"
			value="<%= HtmlUtil.escape(template.getFileName()) %>"
		/>
		
		<liferay-ui:search-container-column-text>
			<a class="modify-link" data-rowId="<%= template.getTemplatefileId() %>" href="javascript:;"><%= removeTemplateIcon %></a>
		</liferay-ui:search-container-column-text>
		</liferay-ui:search-container-row>
	
		<liferay-ui:search-iterator paginate="<%= false %>" />
	</liferay-ui:search-container>
	
	
	<liferay-ui:icon
		cssClass="modify-link"
		iconCssClass="icon-search"
		id="selectTemplateLink"
		label="<%= true %>"
		linkCssClass="btn"
		message="select"
		method="get"
		url="javascript:;"
	/>
		</div>
	</div>
	
	<aui:button type="submit" value="submit"/>
</aui:form>	

<aui:script use="liferay-search-container">

	function <portlet:namespace />initProcess() {
		return "<%= Validator.isNotNull(serviceInfo) ? UnicodeFormatter.toString(serviceInfo.getServiceProcess()) : StringPool.BLANK %>";
	}
	
	function <portlet:namespace />initMethod() {
		return "<%= Validator.isNotNull(serviceInfo) ? UnicodeFormatter.toString(serviceInfo.getServiceMethod()) : StringPool.BLANK %>";
	}
	
	function <portlet:namespace />initInstructions() {
		return "<%= Validator.isNotNull(serviceInfo) ? UnicodeFormatter.toString(serviceInfo.getServiceInstructions()) : StringPool.BLANK %>";
	}
	
	function <portlet:namespace />service_dossier() {
		return "<%= Validator.isNotNull(serviceInfo) ? UnicodeFormatter.toString(serviceInfo.getServiceDossier()) : StringPool.BLANK %>";
	}
	
	function <portlet:namespace />service_condition() {
		return "<%= Validator.isNotNull(serviceInfo) ? UnicodeFormatter.toString(serviceInfo.getServiceCondition()) : StringPool.BLANK %>";
	}
	
	function <portlet:namespace />service_duration() {
		return "<%= Validator.isNotNull(serviceInfo) ? UnicodeFormatter.toString(serviceInfo.getServiceDuration()) : StringPool.BLANK %>";
	}
	
	function <portlet:namespace />service_actors() {
		return "<%= Validator.isNotNull(serviceInfo) ? UnicodeFormatter.toString(serviceInfo.getServiceActors()) : StringPool.BLANK %>";
	}
	
	function <portlet:namespace />service_fee() {
		return "<%= Validator.isNotNull(serviceInfo) ? UnicodeFormatter.toString(serviceInfo.getServiceFee()) : StringPool.BLANK %>";
	}
	
	function <portlet:namespace />service_results() {
		return "<%= Validator.isNotNull(serviceInfo) ? UnicodeFormatter.toString(serviceInfo.getServiceResults()) : StringPool.BLANK %>";
	}
	
	function <portlet:namespace />service_records() {
		return "<%= Validator.isNotNull(serviceInfo) ? UnicodeFormatter.toString(serviceInfo.getServiceResults()) : StringPool.BLANK %>";
	}

	var AArray = A.Array;
	var Util = Liferay.Util;

	var addTemplateIds = [];

	var templateValues = document.<portlet:namespace />fm.<portlet:namespace />addTemplateIds.value;

	if (addTemplateIds) {
		addTemplateIds.push(templateValues);
	}

	var deleteTemplateIds = [];

	var searchContainer = Liferay.SearchContainer.get('<portlet:namespace />templateSearchContainer');

	var searchContainerContentBox = searchContainer.get('contentBox');

	searchContainerContentBox.delegate(
		'click',
		function(event) {
			var link = event.currentTarget;

			var rowId = link.attr('data-rowId');

			var tr = link.ancestor('tr');

			var selectTemplate = Util.getWindow('<portlet:namespace />selectTemplate');

			if (selectTemplate) {
				var selectButton = selectTemplate.iframe.node.get('contentWindow.document').one('.selector-button[data-templatefileid="' + rowId + '"]');

				Util.toggleDisabled(selectButton, false);
			}

			searchContainer.deleteRow(tr, rowId);

			AArray.removeItem(addTemplateIds, rowId);

			deleteTemplateIds.push(rowId);

			document.<portlet:namespace />fm.<portlet:namespace />addTemplateIds.value = addTemplateIds.join(',');
			document.<portlet:namespace />fm.<portlet:namespace />deleteTemplateIds.value = deleteTemplateIds.join(',');
		},
		'.modify-link'
	);

	Liferay.on(
		'<portlet:namespace />enableRemovedTemplates',
		function(event) {
			event.selectors.each(
				function(item, index, collection) {
					var templatefileid = item.attr('data-templatefileid');

					if (AArray.indexOf(deleteTemplateIds, templatefileid) != -1) {
						Util.toggleDisabled(item, false);
					}
				}
			);
		}
	);

	var selectTemplateLink = A.one('#<portlet:namespace />selectTemplateLink');

	if (selectTemplateLink) {
		selectTemplateLink.on(
			'click',
			function(event) {
				Util.selectEntity(
					{
						dialog: {
							constrain: true,
							modal: true,
							width: 600
						},
						id: '<portlet:namespace />selectTemplate',
						title: '<liferay-ui:message arguments="template-file" key="select-x" />',
						uri: '<portlet:renderURL windowState="<%= LiferayWindowState.POP_UP.toString() %>"><portlet:param name="mvcPath" value="/html/portlets/servicemgt/admin/select_template_file.jsp" /></portlet:renderURL>'
					},

					function(event) {
						var rowColumns = [];
						rowColumns.push(event.fileno);
						rowColumns.push(event.filename);
						rowColumns.push('<a class="modify-link" data-rowId="' + event.templatefileid + '" href="javascript:;"><%= UnicodeFormatter.toString(revIcon) %></a>');

						searchContainer.addRow(rowColumns, event.templatefileid);

						searchContainer.updateDataStore();

						AArray.removeItem(deleteTemplateIds, event.templatefileid);

						addTemplateIds.push(event.templatefileid);

						document.<portlet:namespace />fm.<portlet:namespace />addTemplateIds.value = addTemplateIds.join(',');
						document.<portlet:namespace />fm.<portlet:namespace />deleteTemplateIds.value = deleteTemplateIds.join(',');
					}
				);
			}
		);
	}
</aui:script>