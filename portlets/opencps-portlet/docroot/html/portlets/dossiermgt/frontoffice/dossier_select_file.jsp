
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

<%@page import="org.opencps.util.MessageKeys"%>
<%@page import="com.liferay.portlet.documentlibrary.DuplicateFileException"%>
<%@page import="org.opencps.accountmgt.NoSuchAccountOwnUserIdException"%>
<%@page import="com.liferay.portlet.documentlibrary.NoSuchFileEntryException"%>
<%@page import="org.opencps.dossiermgt.PermissionDossierException"%>
<%@page import="org.opencps.accountmgt.NoSuchAccountOwnOrgIdException"%>
<%@page import="org.opencps.accountmgt.NoSuchAccountFolderException"%>
<%@page import="org.opencps.accountmgt.NoSuchAccountTypeException"%>
<%@page import="org.opencps.accountmgt.NoSuchAccountException"%>
<%@page import="org.opencps.dossiermgt.NoSuchDossierPartException"%>
<%@page import="org.opencps.dossiermgt.NoSuchDossierException"%>
<%@page import="org.opencps.util.PortletConstants"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="org.opencps.dossiermgt.search.DossierDisplayTerms"%>
<%@page import="org.opencps.dossiermgt.search.DossierFileDisplayTerms"%>
<%@page import="org.opencps.dossiermgt.service.DossierFileLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.model.DossierFile"%>
<%@page import="org.opencps.dossiermgt.search.DossierFileSearchTerms"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="org.opencps.dossiermgt.search.DossierFileSearch"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@ include file="/init.jsp"%>

<%
	long dossierId = ParamUtil.getLong(request, DossierDisplayTerms.DOSSIER_ID);
	
	long dossierPartId = ParamUtil.getLong(request, DossierFileDisplayTerms.DOSSIER_PART_ID);
	
	long dossierFileId = ParamUtil.getLong(request, DossierFileDisplayTerms.DOSSIER_FILE_ID);
	
	long fileGroupId = ParamUtil.getLong(request, DossierDisplayTerms.FILE_GROUP_ID);
	
	long groupDossierPartId = ParamUtil.getLong(request, "groupDossierPartId");
	
	String groupName = ParamUtil.getString(request, DossierFileDisplayTerms.GROUP_NAME);
	
	String modalDialogId = ParamUtil.getString(request, "modalDialogId");
	
	String redirectURL = ParamUtil.getString(request, "redirectURL");

	List<String> headerNames = new ArrayList<String>();
	
	headerNames.add("#");
	headerNames.add("dossier-file-no");
	headerNames.add("display-name");
	headerNames.add("select");
	
	String headers = StringUtil.merge(headerNames);
	
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "dossier_file.jsp");
	iteratorURL.setParameter("tab1", "select-file");
	

	PortletURL searchURL = renderResponse.createRenderURL();
	searchURL.setParameter("mvcPath", templatePath + "dossier_file.jsp");
	searchURL.setParameter("tab1", "select-file");
	
	String templateFileNo = ParamUtil.getString(request, DossierDisplayTerms.TEMPLATE_FILE_NO);
%>

<liferay-ui:error message="upload-error" key="upload-error"/>

<liferay-ui:error 
	exception="<%= NoSuchDossierException.class %>" 
	message="<%= NoSuchDossierException.class.getName() %>" 
/>
<liferay-ui:error 
	exception="<%= NoSuchDossierPartException.class %>" 
	message="<%= NoSuchDossierPartException.class.getName() %>" 
/>
<liferay-ui:error 
	exception="<%= NoSuchAccountException.class %>" 
	message="<%= NoSuchAccountException.class.getName() %>" 
/>
<liferay-ui:error 
	exception="<%= NoSuchAccountTypeException.class %>" 
	message="<%= NoSuchAccountTypeException.class.getName() %>" 
/>
<liferay-ui:error 
	exception="<%= NoSuchAccountFolderException.class %>" 
	message="<%= NoSuchAccountFolderException.class.getName() %>" 
/>
<liferay-ui:error 
	exception="<%= NoSuchAccountOwnUserIdException.class %>" 
	message="<%= NoSuchAccountOwnUserIdException.class.getName() %>" 
/>

<liferay-ui:error 
	exception="<%= NoSuchAccountOwnOrgIdException.class %>" 
	message="<%= NoSuchAccountOwnOrgIdException.class.getName() %>" 
/>
<liferay-ui:error 
	exception="<%= PermissionDossierException.class %>" 
	message="<%= PermissionDossierException.class.getName() %>" 
/>

<liferay-ui:error 
	exception="<%= NoSuchFileEntryException.class %>" 
	message="<%= NoSuchFileEntryException.class.getName() %>" 
/>
<liferay-ui:error 
	exception="<%= DuplicateFileException.class %>" 
	message="<%= MessageKeys.DOSSIER_FILE_DUPLICATE_NAME %>"
/>

<aui:nav-bar cssClass="custom-toolbar">

	<aui:nav-bar-search cssClass="pull-right">
		<div class="form-search">
			<aui:form action="<%= searchURL %>" method="post" name="fmSearch">
				<aui:row>
					<aui:col width="50">
						<aui:input name="isSameTemplate" 
							type="checkbox" 
							label="search-dossier-file-same-template" 
							inlineField="<%=true %>"
							inlineLabel="left"
						/>
					</aui:col>
					<aui:col width="50">
						<liferay-ui:input-search 
							id="keywords1"
							name="keywords"
							title="keywords"
							placeholder='<%= LanguageUtil.get(locale, "keywords") %>' 
						/>
					</aui:col>
				</aui:row>
			</aui:form>
		</div>
	</aui:nav-bar-search>
</aui:nav-bar>

<portlet:actionURL var="cloneDossierFileURL" name="cloneDossierFile"/>

<aui:form action="<%=cloneDossierFileURL.toString() %>" method="post" name="fmSelectDossierFile">
	<liferay-ui:search-container 
		searchContainer="<%= new DossierFileSearch(renderRequest, SearchContainer.DEFAULT_DELTA, iteratorURL) %>" 
		headerNames="<%= headers %>"
	>
	
		<liferay-ui:search-container-results>
			<%
				DossierFileSearchTerms searchTerms = (DossierFileSearchTerms)searchContainer.getSearchTerms();
							
					List<DossierFile> dossierFiles = null;
					int totalCount = 0;
					try {
						dossierFiles = DossierFileLocalServiceUtil.searchDossierFile(scopeGroupId, searchTerms.getKeywords(), templateFileNo, 0, searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator());
						totalCount = DossierFileLocalServiceUtil.countDossierFile(scopeGroupId, searchTerms.getKeywords(), templateFileNo, 0);
					} catch(Exception e){
						
					}
				
					total = totalCount;
					results = dossierFiles;
					
					pageContext.setAttribute("results", results);
					pageContext.setAttribute("total", total);				
			%>
		</liferay-ui:search-container-results>	
			<liferay-ui:search-container-row 
				className="org.opencps.dossiermgt.model.DossierFile" 
				modelVar="dossierFile" 
				keyProperty="dossierFileId"
			>
				<%				
				    // no column
					row.addText(String.valueOf(row.getPos() + 1));
					
					// dossier file no column
					row.addText(dossierFile.getDossierFileNo());
									
					// dossier display name column
					row.addText(dossierFile.getDisplayName());
					
					row.addButton(LanguageUtil.get(locale, "select"), "javascript:" + renderResponse.getNamespace() + "selectDossierFile(" + dossierFile.getDossierFileId() +")");
					
				%>	
			</liferay-ui:search-container-row> 
		
		<liferay-ui:search-iterator/>
	</liferay-ui:search-container>
	<aui:input name="cloneDossierFileId" type="hidden"/>
	<aui:input name="redirectURL" type="hidden" value="<%=Validator.isNull(redirectURL) ? currentURL : redirectURL %>"/>
	<aui:input name="<%=DossierDisplayTerms.DOSSIER_ID %>" type="hidden" value="<%=dossierId %>"/>
	<aui:input name="groupDossierPartId" type="hidden" value="<%=groupDossierPartId %>"/>
	<aui:input name="<%=DossierFileDisplayTerms.DOSSIER_FILE_ID %>" type="hidden" value="<%=dossierFileId %>"/>
	<aui:input name="<%=DossierDisplayTerms.FILE_GROUP_ID %>" type="hidden" value="<%=fileGroupId %>"/>
	<aui:input name="<%=DossierFileDisplayTerms.DOSSIER_PART_ID %>" type="hidden" value="<%=dossierPartId %>"/>
	<aui:input name="<%=DossierFileDisplayTerms.DOSSIER_FILE_ORIGINAL %>" type="hidden" value="<%=String.valueOf(PortletConstants.DOSSIER_FILE_ORIGINAL) %>"/>
	<aui:input name="<%=DossierFileDisplayTerms.DOSSIER_FILE_TYPE %>" type="hidden" value="<%=String.valueOf(PortletConstants.DOSSIER_FILE_TYPE_INPUT) %>"/>
	<aui:input name="<%=DossierFileDisplayTerms.GROUP_NAME %>" type="hidden" value="<%=groupName %>"/>
	
</aui:form>
<aui:script>
	Liferay.provide(window, '<portlet:namespace/>selectDossierFile', function(id) {
		var A = AUI();
		var cloneDossierFile = A.one('#<portlet:namespace/>cloneDossierFileId').val(id);
		submitForm(document.<portlet:namespace />fmSelectDossierFile);
	});
</aui:script>