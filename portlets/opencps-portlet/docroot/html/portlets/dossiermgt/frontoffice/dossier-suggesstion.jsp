
<%@page import="org.opencps.util.PortletConstants"%>
<%@page import="org.opencps.dossiermgt.service.DossierPartLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.model.DossierPart"%>
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

<%@page import="org.opencps.dossiermgt.util.DossierMgtUtil"%>
<%@page import="org.opencps.servicemgt.model.TemplateFile"%>
<%@page import="com.liferay.portal.kernel.language.UnicodeLanguageUtil"%>
<%@page import="com.liferay.portal.kernel.servlet.SessionErrors"%>
<%@page import="com.liferay.portal.kernel.servlet.SessionMessages"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@page import="com.liferay.portal.kernel.util.Constants"%>
<%@page import="org.opencps.dossiermgt.search.DossierDisplayTerms"%>
<%@page import="org.opencps.util.DateTimeUtil"%>
<%@page import="org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil"%>
<%@page import="org.opencps.servicemgt.model.ServiceInfo"%>
<%@page import="org.opencps.dossiermgt.service.DossierLocalServiceUtil"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="org.opencps.dossiermgt.service.ServiceConfigLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.model.ServiceConfig"%>
<%@page import="org.opencps.dossiermgt.service.DossierTemplateLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.model.DossierTemplate"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.opencps.dossiermgt.model.Dossier"%>
<%@page import="java.util.List"%>
<%@ include file="../init.jsp"%>

<%
	long dossierId = ParamUtil.getLong(request, "dossierId");
	long serviceConfigId = ParamUtil.getLong(request, "serviceConfigId");
	long dossierPartId = ParamUtil.getLong(request, "dossierPartId");
	int totalCount = 0;
	long dossierTemplateId = 0;
	ServiceConfig serviceConfig = null;
	List<Dossier> dossiers = new ArrayList<Dossier>();
	Dossier dossierNeedAdd = null;
	try {
		dossierNeedAdd = DossierLocalServiceUtil.getDossier(dossierId);
		dossierTemplateId = dossierNeedAdd.getDossierTemplateId();
	} catch (Exception e) {}
	
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "dossier-suggesstion.jsp");
	iteratorURL.setParameter("dossierId", String.valueOf(dossierId));
	
	String dossierTemplateSuggesstion = DossierMgtUtil.getDossierTemplateSuggesstion(dossierId, scopeGroupId);
	
	boolean success = false;
	
	try{
		success = !SessionMessages.isEmpty(renderRequest) && SessionErrors.isEmpty(renderRequest);
	}catch(Exception e){
		
	}
	
%>

<div class="opencps-searchcontainer-wrapper" id = "<portlet:namespace/>opencps-searchcontainer-wrapper">
	<liferay-ui:search-container 
			emptyResultsMessage="no-serviceinfo-were-found"
			iteratorURL="<%=iteratorURL %>"
			delta="<%=5 %>"
			deltaConfigurable="true"
		>
		
		<liferay-ui:search-container-results>
			<%
				dossiers = DossierLocalServiceUtil.getDossierSuggesstion(dossierStatusConfig, dossierTemplateSuggesstion, searchContainer.getStart(), searchContainer.getEnd());
				totalCount = DossierLocalServiceUtil.countDossierSuggesstion(dossierStatusConfig, dossierTemplateSuggesstion);
				
				results = dossiers;
				total = totalCount;
				pageContext.setAttribute("results", results);
				pageContext.setAttribute("total", total);
			%>
		</liferay-ui:search-container-results>
		
		<liferay-ui:search-container-row 
			className="org.opencps.dossiermgt.model.Dossier" 
			modelVar="dossier" 
			keyProperty="dossierId"
		>
			<%
				if(Validator.isNotNull(dossierNeedAdd)) {
					try {
						List<DossierPart> dossierParts = DossierPartLocalServiceUtil
								.getDossierParts(dossierNeedAdd.getDossierTemplateId());
						
						List<DossierPart> dossierPartsCurr = DossierPartLocalServiceUtil
								.getDossierParts(dossier.getDossierTemplateId());
						
						for(DossierPart dossierPartCurr : dossierPartsCurr) {
							for(DossierPart dossierPart : dossierParts) {
								if (dossierPartCurr.getPartType() == dossierPart
										.getPartType()
										&& dossierPartCurr.getPartNo()
												.equalsIgnoreCase(
														dossierPart.getPartNo())
										&& dossierPart.getPartType() != PortletConstants.DOSSIER_PART_TYPE_MULTIPLE_RESULT
										&& dossierPart.getPartType() != PortletConstants.DOSSIER_PART_TYPE_RESULT) {
									%>
										<portlet:renderURL var="viewDossierURL">
											<portlet:param name="mvcPath"
												value='<%=templatePath + "edit_dossier.jsp"%>' />
											<portlet:param name="<%=DossierDisplayTerms.DOSSIER_ID%>"
												value="<%=String.valueOf(dossier.getDossierId())%>" />
											<portlet:param name="<%=Constants.CMD%>" value="<%=Constants.VIEW%>" />
											<portlet:param name="isEditDossier" value="<%=String.valueOf(false)%>" />
										</portlet:renderURL>
										
										<portlet:actionURL var="updateDossierSuggestionURL" name="updateDossierSuggestion">
											<portlet:param name="currentDossierId" value="<%=String.valueOf(dossier.getDossierId()) %>"/>
											<portlet:param name="dossierId" value="<%=String.valueOf(dossierId) %>"/>
										</portlet:actionURL>
										
										<% 
											String serviceName = StringPool.BLANK;
											ServiceInfo serviceInfo = null;
											try {
												serviceInfo = ServiceInfoLocalServiceUtil.
														getServiceInfo(ServiceConfigLocalServiceUtil.getServiceConfig(dossier.getServiceConfigId()).getServiceInfoId());
												serviceName = serviceInfo.getServiceName();
											} catch(Exception e) {
												
											}
											
											String viewDossierUrlNomal = viewDossierURL.toString();
											if(viewDossierUrlNomal.contains("p_p_state=pop_up"))  {
												viewDossierUrlNomal = StringUtil.replace(viewDossierUrlNomal, "p_p_state=pop_up", "p_p_state=nomal");
											}				
										%>
										<liferay-util:buffer var="boundcol1">
											<div class="row-fluid">
												
												<div class="span6 bold-label">
													<liferay-ui:message key="dossier-no"/>
												</div>
												<div class="span6"><%=String.valueOf(dossier.getDossierId())%></div>
											</div>
											
											<div class="row-fluid">
												
												<div class="span6 bold-label">
													<liferay-ui:message key="reception-no"/>
												</div>
												<div class="span6">
													<%=Validator.isNotNull(dossier.getReceptionNo()) ? dossier.getReceptionNo() : StringPool.DASH%>
												</div>
											</div>
											
										</liferay-util:buffer>
										
										<liferay-util:buffer var="boundcol2">
											<div class="row-fluid">
												<div class="span5 bold-label">
													<liferay-ui:message key="service-name"/>
												</div>
												<div class="span7"><%= Validator.isNotNull(serviceName) ? serviceName : StringPool.DASH %> </div>
											</div>
										</liferay-util:buffer>
										
										<liferay-util:buffer var="boundcol3">
											<div class="row-fluid">
												<div class="span5 bold-label">
													<liferay-ui:message key="submit-date"/>
												</div>
												
												<div class="span7">
													<%=
														Validator.isNotNull(dossier.getSubmitDatetime()) ?
														DateTimeUtil.convertDateToString(dossier.getSubmitDatetime(), DateTimeUtil._VN_DATE_TIME_FORMAT) :
															DateTimeUtil._EMPTY_DATE_TIME 
													%> 
												</div>
											</div>
											
											<div class="row-fluid">
												<div class="span5 bold-label">
													<liferay-ui:message key="finish-date"/>
												</div>
												
												<div class="span7">
													<%= 
														Validator.isNotNull(dossier.getFinishDatetime()) ?
														DateTimeUtil.convertDateToString(dossier.getFinishDatetime(), DateTimeUtil._VN_DATE_TIME_FORMAT) :
															DateTimeUtil._EMPTY_DATE_TIME
													%> 
												</div>
											</div>
											
										</liferay-util:buffer>
										
										<liferay-util:buffer var="boundcol4">
											<aui:row>
												<a href="<%=viewDossierUrlNomal.toString()%>" target="_blank" class="btn">
													<i class="fa fa-eye"><liferay-ui:message key="view"/></i>
												<a>
											</aui:row>
											
											<aui:row>
												 <aui:a cssClass="btn"
													href="<%=\"javascript:\" + renderResponse.getNamespace()+ \"loadingMark('\" + updateDossierSuggestionURL.toString() +\"')\" %>" 
												>
													<i class = "fa fa-check"><liferay-ui:message key="choose"/></i>
												</aui:a> 
											</aui:row>
											
											
											 <%-- <aui:button  
												name="dossierSuggestion" 
												value="choose" 
												onClick="<%= renderResponse.getNamespace()+ \"loadingMark('\" + updateDossierSuggestionURL.toString() +\"')\" %>"
											/> --%>
										</liferay-util:buffer>
										
										<%
											row.setClassName("opencps-searchcontainer-row");
											row.addText(String.valueOf(row.getPos() + 1));
											row.addText(boundcol1);
											row.addText(boundcol2);
											row.addText(boundcol3);
											row.addText(boundcol4);
										%>
									
									<%
								}
							}
						}
					} catch(Exception e) {}
				}
			%>
				
		</liferay-ui:search-container-row>
			
		<liferay-ui:search-iterator type="opencs_page_iterator"/>
	
	</liferay-ui:search-container>
</div>

<aui:script>
	AUI().ready(function(A) {
		var success = <%=success%>;
		
		if(success) {
			closeDialog('submit-dossier-suggesstion', '<%=WebKeys.DOSSIER_MGT_PORTLET%>_');
		}
	});
	
	Liferay.provide(window, '<portlet:namespace/>loadingMark', function(url) {
		var A = AUI();
		var loadingMask = new A.LoadingMask(
			{
				'strings.loading': '<%= UnicodeLanguageUtil.get(pageContext, "rending...") %>',
				target: A.one('#<portlet:namespace/>opencps-searchcontainer-wrapper')
			}
		);
		
		loadingMask.show();
		
		A.io.request(
			url,
			{
				on: {
					success: function(event, id, obj) {
						var response = this.get('responseData');
						loadingMask.hide();
						if(response){
							response = JSON.parse(response);
							if(response.msg == "success"){
								closeDialog('submit-dossier-suggesstion', '<%=WebKeys.DOSSIER_MGT_PORTLET%>_');
							}else{
								alert('<%= UnicodeLanguageUtil.get(pageContext, "error-while-add-dossier-file") %>');
								
							}
						}
					}
				}
			}
		);

	});
</aui:script>