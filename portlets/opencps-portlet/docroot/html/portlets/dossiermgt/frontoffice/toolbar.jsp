
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
<%@page import="org.opencps.util.PortletConstants"%>
<%@page import="org.opencps.servicemgt.util.ServiceUtil"%>
<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@page import="org.opencps.dossiermgt.util.DossierMgtUtil"%>
<%@page import="org.opencps.util.ActionKeys"%>
<%@page import="org.opencps.dossiermgt.permissions.DossierPermission"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="org.opencps.util.PortletUtil"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>

<%@ include file="../init.jsp"%>

<%
	long serviceDomainId = ParamUtil.getLong(request, "serviceDomainId");
	
	long govAgencyId = ParamUtil.getLong(request, "govAgencyId");
	
	String dossierStatus = ParamUtil.getString(request, "dossierStatus", PortletConstants.DOSSIER_STATUS_NEW);

	String tabs1 = ParamUtil.getString(request, "tabs1", DossierMgtUtil.TOP_TABS_DOSSIER);
	
	PortletURL searchURL = renderResponse.createRenderURL();
	
	boolean isListServiceConfig = ParamUtil.getBoolean(request, "isListServiceConfig", false);
	
	if(isListServiceConfig && tabs1.equals(DossierMgtUtil.TOP_TABS_DOSSIER)){
		searchURL.setParameter("mvcPath", templatePath + "frontofficeservicelist.jsp");
		searchURL.setParameter("tabs1", DossierMgtUtil.TOP_TABS_DOSSIER);
		searchURL.setParameter("isListServiceConfig", String.valueOf(true));
	}else if(!isListServiceConfig && tabs1.equals(DossierMgtUtil.TOP_TABS_DOSSIER)){
		searchURL.setParameter("mvcPath", templatePath + "frontofficedossierlist.jsp");
		searchURL.setParameter("tabs1", DossierMgtUtil.TOP_TABS_DOSSIER);
	}
	
	
%>

<aui:nav-bar cssClass="custom-toolbar">

	<c:if test="<%=!isListServiceConfig %>">
		<aui:nav id="toolbarContainer" cssClass="nav-display-style-buttons pull-left font-pull" >
			<c:if test="<%=DossierPermission.contains(permissionChecker, scopeGroupId, ActionKeys.ADD_DOSSIER) 
				&& tabs1.equals(DossierMgtUtil.TOP_TABS_DOSSIER)%>">
				
				<portlet:renderURL var="addDossierURL" windowState="<%=LiferayWindowState.NORMAL.toString() %>">
					<portlet:param name="mvcPath" value="/html/portlets/dossiermgt/frontoffice/frontofficeservicelist.jsp"/>
					<portlet:param name="isListServiceConfig" value="<%=String.valueOf(true) %>"/>
					<portlet:param name="backURL" value="<%=currentURL %>"/>
				</portlet:renderURL>
				<aui:nav-item 
					id="addDictItem" 
					label="add-dossier" 
					iconCssClass="icon-plus"  
					href="<%=addDossierURL %>"
				/>
			</c:if>
		</aui:nav>
	</c:if>
	
	<aui:nav-bar-search cssClass="pull-right front-custom-select-search">
		<div class="form-search">
			<aui:form action="<%= searchURL %>" method="post" name="fmSearch" onSubmit='<%= "event.preventDefault(); " + renderResponse.getNamespace() + "doSearch();" %>'>

				<c:choose>
					<c:when test="<%=isListServiceConfig %>">
						
						<aui:row>
							<aui:col width="30">
								<datamgt:ddr 
									depthLevel="1" 
									dictCollectionCode="<%=ServiceUtil.SERVICE_DOMAIN %>" 
									name="serviceDomain"
									inlineField="<%=true%>"
									inlineLabel="left"
									showLabel="<%=false%>"
									emptyOptionLabels="filter-by-service-domain"
									itemsEmptyOption="true"
									itemNames="serviceDomainId"
									selectedItems="<%=String.valueOf(serviceDomainId)%>"
								/>
							</aui:col>
							<aui:col width="30">
								<datamgt:ddr 
									depthLevel="1" 
									dictCollectionCode="<%=ServiceUtil.SERVICE_ADMINISTRATION %>" 
									name="govAgency"
									inlineField="<%=true%>"
									inlineLabel="left"
									showLabel="<%=false%>"
									emptyOptionLabels="filter-by-gov-agency"
									itemsEmptyOption="true"
									itemNames="govAgencyId"
									selectedItems="<%=String.valueOf(govAgencyId)%>"
								/>
							</aui:col>
							<aui:col width="30">
								<liferay-ui:input-search 
									id="keywords1"
									name="keywords"
									title="keywords"
									placeholder='<%= LanguageUtil.get(locale, "keywords") %>'
								/>
							</aui:col>
						</aui:row>
					</c:when>
					<c:otherwise>
						<c:if test="<%=tabs1.equals(DossierMgtUtil.TOP_TABS_DOSSIER) %>">
							<aui:row>
								<aui:col width="30">
									<datamgt:ddr 
										depthLevel="1" 
										dictCollectionCode="<%=ServiceUtil.SERVICE_DOMAIN %>" 
										name="serviceDomain"
										inlineField="<%=true%>"
										inlineLabel="left"
										showLabel="<%=false%>"
										emptyOptionLabels="filter-by-service-domain"
										itemsEmptyOption="true"
										itemNames="serviceDomainId"
										selectedItems="<%=String.valueOf(serviceDomainId)%>"
									/>
								</aui:col>
								<aui:col width="30">
									<aui:select name="dossierStatus" label="<%=StringPool.BLANK %>" inlineField="<%=true %>" inlineLabel="left">
										<aui:option><liferay-ui:message key="dossier-status"/></aui:option>
										<aui:option value="<%=StringPool.BLANK %>" selected="<%=dossierStatus.equals(StringPool.BLANK)%>"><liferay-ui:message key="all"/></aui:option>
											<%
												for(String status : PortletUtil.getDossierStatus()){
													%>
														<aui:option 
															value="<%= status%>"
															selected="<%=dossierStatus.equals(status)%>"
														>
															<%=PortletUtil.getDossierStatusLabel(status, locale) %>
														</aui:option>
													<%
												}
											%>
									</aui:select>
								</aui:col>
								<aui:col width="30">
									<liferay-ui:input-search 
										id="keywords1"
										name="keywords"
										title="keywords"
										placeholder='<%=LanguageUtil.get(locale, "keywords") %>' 
									/>
								</aui:col>
							</aui:row>
						</c:if>
					</c:otherwise>
				</c:choose>
			</aui:form>
		</div>
	</aui:nav-bar-search>
</aui:nav-bar>

<aui:script use="liferay-util-list-fields,liferay-portlet-url">

	Liferay.provide(window, '<portlet:namespace/>doSearch', function() {
		
		var A = AUI();
		
		var isListServiceConfig = '<%=isListServiceConfig%>'
		
		var serviceDomainId = A.one('#<portlet:namespace/>serviceDomainId').val();
		
		var govAgencyId;
		
		var dossierStatus;
		
		if(isListServiceConfig == 'true'){
			govAgencyId = A.one('#<portlet:namespace/>govAgencyId').val();
		}else{
			dossierStatus = A.one('#<portlet:namespace/>dossierStatus').val();
		} 

		var fmSearch = A.one('#<portlet:namespace/>fmSearch');
		
		var action = fmSearch.attr('action');
		
		var portletURL = Liferay.PortletURL.createURL(action);
		portletURL.setParameter("serviceDomainId", serviceDomainId);
		
		if(isListServiceConfig == 'true'){
			portletURL.setParameter("govAgencyId", govAgencyId);
		}else{
			portletURL.setParameter("dossierStatus", dossierStatus);
		} 
		
		fmSearch.setAttribute('action', portletURL.toString());
		
		submitForm(document.<portlet:namespace />fmSearch);
	},['liferay-portlet-url']);
</aui:script>


<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.dossiermgt.frontoffice.toolbar.jsp");
%>