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
<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@page import="com.liferay.portal.kernel.language.UnicodeLanguageUtil"%>
<%@page import="javax.portlet.PortletRequest"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="com.liferay.portlet.PortletURLFactoryUtil"%>
<%@page import="org.opencps.dossiermgt.search.DossierFileDisplayTerms"%>
<%@page import="org.opencps.dossiermgt.model.impl.DossierPartImpl"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="java.util.LinkedList"%>
<%@page import="java.util.Map"%>
<%@page import="org.opencps.dossiermgt.model.DossierPart"%>
<%@page import="java.util.List"%>

<%@ include file="../../init.jsp"%>

<portlet:renderURL var="updateDossierFileURL" windowState="<%=LiferayWindowState.POP_UP.toString() %>">
	<portlet:param name="mvcPath" value='<%=templatePath + "upload_dossier_file.jsp" %>'/>
</portlet:renderURL>

<%
	List<List<DossierPart>> maps = new ArrayList<List<DossierPart>>();

	List<DossierPart> dossierParts1 = new ArrayList<DossierPart>();
	DossierPart dossierPart1 = new DossierPartImpl();
	dossierPart1.setDossierpartId(1);
	dossierPart1.setPartType(1);
	dossierPart1.setPartName("dossierPart1");
	
	dossierParts1.add(dossierPart1);
	
	List<DossierPart> dossierParts2 = new ArrayList<DossierPart>();
	DossierPart dossierPart2 = new DossierPartImpl();
	dossierPart2.setDossierpartId(2);
	dossierPart2.setPartType(1);
	dossierPart2.setPartName("dossierPart2");
	
	dossierParts2.add(dossierPart2);
	
	List<DossierPart> dossierParts3 = new ArrayList<DossierPart>();
	DossierPart dossierPart3 = new DossierPartImpl();
	dossierPart3.setDossierpartId(3);
	dossierPart3.setPartType(2);
	dossierPart3.setPartName("dossierPart3");
	
	dossierParts3.add(dossierPart3);
	
	maps.add(dossierParts1);
	maps.add(dossierParts2);
	maps.add(dossierParts3);

	for(List<DossierPart> dossierParts : maps){
		%>
			<div class="opencps dossiermgt dossier-part-tree">
			<%
				if(dossierParts != null){
					for(DossierPart dossierPart : dossierParts){
						%>
						<aui:row>
							<aui:col width="30">
								<span class="opencps dossiermgt dossier-part">
									<i class="fa fa-square-o" aria-hidden="true"></i>
									<span class="opencps dossiermgt dossier-part-name">
										<%=dossierPart.getPartName() %>
									</span>
								</span>
							</aui:col>
							
							<aui:col width="70">
								<span class="opencps dossiermgt dossier-part-control">
									<liferay-util:include page="/html/portlets/dossiermgt/frontoffice/dossier_file_controls.jsp"  servletContext="<%=application %>">
										<portlet:param name="<%=DossierFileDisplayTerms.DOSSIER_PART_ID %>" value="<%=String.valueOf(dossierPart.getDossierpartId()) %>"/>
									</liferay-util:include>
								</span>
							</aui:col>
						</aui:row>
						<%
					}	
				}
			%>
			</div>
		<%
	}
%>
<aui:script>
	
	AUI().ready('aui-base','liferay-portlet-url','aui-io', function(A){
		
		var uploadFileCtrs = A.all('.upload-file');
		
		if(uploadFileCtrs){
			uploadFileCtrs.each(function(elm){
				elm.on('click', function(){
					var dossierPartId = A.one(elm).attr('dossier-part');
					var index = A.one(elm).attr('index');
					var portletURL = Liferay.PortletURL.createURL('<%= PortletURLFactoryUtil.create(request, WebKeys.DOSSIER_MGT_PORTLET, themeDisplay.getPlid(), PortletRequest.RENDER_PHASE) %>');
					portletURL.setParameter("mvcPath", "/html/portlets/dossiermgt/frontoffice/upload_dossier_file.jsp");
					portletURL.setWindowState("<%=LiferayWindowState.POP_UP.toString()%>"); 
					portletURL.setPortletMode("normal");
					portletURL.setParameter("dossierPartId", dossierPartId);
					portletURL.setParameter("index", index);
					
					<portlet:namespace/>openDossierFileDialog(portletURL.toString());
				});
			});
		}
		
	});

	Liferay.provide(window, '<portlet:namespace/>openDossierFileDialog', function(uri) {
		var dossierFileDialog = Liferay.Util.openWindow(
			{
				dialog: {
					cssClass: 'opencps-dossiermgt-upload-dossier-file',
					modal: true,
					height: 480,
					width: 800
				},
				cache: false,
				id: '<portlet:namespace />dossierFileId',
				title: '<%= UnicodeLanguageUtil.get(pageContext, "upload-dossier-file") %>',
				uri: uri
				
			},function(evt){

			}
		);
		
	});
	
	Liferay.on('getUploadDossierFileData',function(event) {
		var A = AUI();
		 
		var data = event.responseData;
		 
		var json = [];
		 
		if(data.length > 0){
			var index = data[0].index;
			var dossierPartId = data[0].dossierPartId;
	
			var input = A.one('#<portlet:namespace/>' + dossierPartId + '-' + index);
				 
			if(input){
				if(input.val() != ''){
					json = JSON.parse(input.val());
				}
					
				json.push(data[0]);
				input.val(JSON.stringify(json));
			}
				 
			var counterLabel = A.one('.alias-' + dossierPartId + '-' + index);
				 
			if(counterLabel){
				counterLabel.text(json.length);
			}
		}
	});

</aui:script>
