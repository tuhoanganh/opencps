<%@page import="org.opencps.util.ActionKeys"%>
<%@page import="org.opencps.dossiermgt.permission.DossierPartPermission"%>
<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
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
<%@ include file="../init.jsp"%>
<%@page import="com.liferay.portal.kernel.util.ParamUtil"%>
<%@page import="org.opencps.dossiermgt.search.DossierTemplateDisplayTerms"%>
<%@page import="org.opencps.dossiermgt.util.DossierMgtUtil"%>

<%
	long dossierTemplateId = ParamUtil.getLong(request, DossierTemplateDisplayTerms.DOSSIERTEMPLATE_DOSSIERTEMPLATEID);
	
	String backURL = ParamUtil.getString(request, "backURL");
	
	String [] dossierTemplateSections = null;
	
	if(dossierTemplateId == 0) {
		dossierTemplateSections = new String [1];
		dossierTemplateSections[0] = "edit_dossier_template";
	} else {
		dossierTemplateSections = new String[3];
		dossierTemplateSections[0] = "edit_dossier_template";
		dossierTemplateSections[1] = "dossierpartlist";
		dossierTemplateSections[2] = "dossierservicelist";
	}
	
	String[][] categorySections = {dossierTemplateSections};
%>
<portlet:renderURL var="renderToToolBar" windowState="<%=LiferayWindowState.EXCLUSIVE.toString()%>">
	<portlet:param name="mvcPath" value='<%= templatePath + "toolbar.jsp" %>'/>
</portlet:renderURL>
<liferay-ui:header
	backURL="<%= backURL %>"
	title="update-dossier"
	backLabel="back"
/>

<portlet:actionURL name="updateDossier" var="updateDossierURL" >
</portlet:actionURL>


<liferay-util:buffer var="htmlTop">
	<liferay-ui:icon iconCssClass="icon-home" />
</liferay-util:buffer>

<liferay-util:buffer var="htmlBot">
	<div class="button-holder ">
			<aui:button name="submit" type="submit" value="submit"/>
			<aui:button name="cancel" value="cancel" href="<%=backURL %>"/>	
	</div>
</liferay-util:buffer>

<aui:form name="fm" 
	method="post" 
	action="<%=updateDossierURL.toString() %>">
	<liferay-ui:form-navigator 
		backURL="<%= currentURL %>"
		categoryNames= "<%= DossierMgtUtil._DOSSIER_CATEGORY_NAMES %>"	
		categorySections="<%=categorySections %>" 
		htmlBottom="<%= htmlBot %>"
		htmlTop="<%= htmlTop %>"
		jspPath='<%=templatePath + "dossier_common/" %>'
		showButtons="false"
		
		>	
	</liferay-ui:form-navigator>
	<%-- <aui:input 
		name="<%=WorkingUnitDisplayTerms.WORKINGUNIT_ID %>" 
		value="<%=String.valueOf(workingUnitId) %>"
		type="hidden"
	></aui:input> --%>
</aui:form>

<aui:script>
	
	
	AUI().ready('liferay-portlet-url',function(A) {	
		var dossierPartLink = A.one('#<portlet:namespace />dossierpartlistLink');
		var dossierServiceLink = A.one('#<portlet:namespace />dossierservicelistLink'); 
		var dossierTemplateLink = A.one('#<portlet:namespace />edit_dossier_templateLink');
		
		if(dossierTemplateLink) {
			var submitBtn = A.one('#<portlet:namespace />submit');
			dossierTemplateLink.on('click',function(){
				submitBtn.show();
			});
		} 
		
		if(dossierPartLink) {
			var submitBtn = A.one('#<portlet:namespace />submit');
			dossierPartLink.on('click',function(){
				submitBtn.hide();
				<portlet:namespace />sentToolBarSignal('dossierPartToolBar');
			});
		} 
		
		if(dossierServiceLink) {
			var submitBtn = A.one('#<portlet:namespace />submit');
			dossierServiceLink.on('click',function(){
				submitBtn.hide();
				<portlet:namespace />sentToolBarSignal('serviceConfigToolBar');
			});
		} 
		
	});
	
Liferay.provide(window, '<portlet:namespace/>sentToolBarSignal', function(tbSignal) {
		
		var A = AUI();
		
		A.io.request(
			'<%= renderToToolBar.toString() %>',
			{
				dataType : 'text/html',
				method : 'GET',
			    data:{    	
			    	"<portlet:namespace />tabs1" : tbSignal
			    },   
			    on: {
			    	success: function(event, id, obj) {
						var instance = this;
						var res = instance.get('responseData');
						
						var toolbarResponse = A.one("#<portlet:namespace/>toolbarResponse");
						
						if(toolbarResponse){
							toolbarResponse.empty();
							toolbarResponse.html(res);
						}
							
					},
			    	error: function(){}
				}
			}
		);
	},['aui-base','aui-io']);
</aui:script>