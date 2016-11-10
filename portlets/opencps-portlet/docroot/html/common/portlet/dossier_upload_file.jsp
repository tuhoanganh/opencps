

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

<%@page import="org.opencps.dossiermgt.model.DossierPart"%>
<%@page import="org.opencps.dossiermgt.service.DossierPartLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.service.DossierFileLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.model.DossierFile"%>
<%@page import="org.opencps.util.DateTimeUtil"%>
<%@page import="org.opencps.util.PortletUtil"%>
<%@page import="java.util.Date"%>
<%@page import="org.opencps.util.PortletConstants"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="org.opencps.util.MessageKeys"%>
<%@page import="com.liferay.portlet.documentlibrary.DuplicateFileException"%>
<%@page import="com.liferay.portlet.documentlibrary.FileExtensionException"%>
<%@page import="org.opencps.dossiermgt.search.DossierDisplayTerms"%>
<%@page import="org.opencps.dossiermgt.search.DossierFileDisplayTerms"%>
<%@page import="com.liferay.portal.kernel.servlet.SessionErrors"%>
<%@page import="com.liferay.portal.kernel.servlet.SessionMessages"%>
<%@page import="com.liferay.portlet.documentlibrary.FileSizeException"%>
<%@page import="org.opencps.dossiermgt.PermissionDossierException"%>
<%@page import="org.opencps.accountmgt.NoSuchAccountOwnOrgIdException"%>
<%@page import="org.opencps.accountmgt.NoSuchAccountOwnUserIdException"%>
<%@page import="org.opencps.accountmgt.NoSuchAccountFolderException"%>
<%@page import="org.opencps.accountmgt.NoSuchAccountTypeException"%>
<%@page import="org.opencps.accountmgt.NoSuchAccountException"%>
<%@page import="org.opencps.dossiermgt.NoSuchDossierPartException"%>
<%@page import="org.opencps.dossiermgt.NoSuchDossierException"%>
<%@page import="com.liferay.portal.RolePermissionsException"%>
<%@page import="javax.portlet.PortletPreferences"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="org.opencps.util.PortletPropsValues"%>
<%@page import="com.liferay.portlet.documentlibrary.model.DLFileEntry"%>
<%@page import="com.liferay.portlet.documentlibrary.service.DLFileEntryLocalServiceUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@ include file="/init.jsp"%>

<%
	boolean success = false;

	try{
		success = !SessionMessages.isEmpty(renderRequest) && SessionErrors.isEmpty(renderRequest);
		
	}catch(Exception e){
		
	}
	
	long dossierId = ParamUtil.getLong(request, DossierDisplayTerms.DOSSIER_ID);
	
	long dossierPartId = ParamUtil.getLong(request, DossierFileDisplayTerms.DOSSIER_PART_ID);
	
	long dossierFileId = ParamUtil.getLong(request, DossierFileDisplayTerms.DOSSIER_FILE_ID);
	
	long fileGroupId = ParamUtil.getLong(request, DossierDisplayTerms.FILE_GROUP_ID);
	
	long groupDossierPartId = ParamUtil.getLong(request, "groupDossierPartId");

	String groupName = ParamUtil.getString(request, DossierFileDisplayTerms.GROUP_NAME);
	
	String modalDialogId = ParamUtil.getString(request, "modalDialogId");
	
	String redirectURL = ParamUtil.getString(request, "redirectURL");
	
	DossierFile dossierFile = null;
	
	if(dossierFileId > 0){
		try{
			
			dossierFile = DossierFileLocalServiceUtil.getDossierFile(dossierFileId);
			
		}catch(Exception e){}
		
	}
	
	String dossierPartName = StringPool.BLANK; 
	if(dossierPartId > 0){
		DossierPart dossierPart = DossierPartLocalServiceUtil.fetchDossierPart(dossierPartId);
		dossierPartName = Validator.isNotNull(dossierPart)?dossierPart.getPartName():StringPool.BLANK;
	}
	
	Date defaultDossierFileDate = dossierFile != null && dossierFile.getDossierFileDate() != null ? 
		dossierFile.getDossierFileDate() : DateTimeUtil.convertStringToDate("01/01/1970");
		
	PortletUtil.SplitDate spd = new PortletUtil.SplitDate(defaultDossierFileDate);
	
	PortletPreferences preferences = renderRequest.getPreferences();

	if (Validator.isNotNull(portletResource)) {
		preferences = PortletPreferencesFactoryUtil.getPortletSetup(request, portletResource);
	}
	
	String fileTypes = preferences.getValue("fileTypes", StringPool.BLANK);
	float maxUploadFileSizeInMb = GetterUtil.getFloat(preferences.getValue("maxUploadFileSizeInMb", StringPool.BLANK));
	float maxTotalUploadFileSizeInMb = GetterUtil.getFloat(preferences.getValue("maxTatalUploadFileSizeInMb", StringPool.BLANK));
	
	List<DossierFile> dossierFileList = new ArrayList<DossierFile>();
	if (dossierId > 0){
		try {
			dossierFileList = DossierFileLocalServiceUtil.getDossierFileByDossierId(dossierId);
		} catch (Exception e){}
	}
	
	float totalUploadFileSizeInByte = 0;
	
	if (!dossierFileList.isEmpty()){
		for (DossierFile tempDossierFile : dossierFileList){
			if (tempDossierFile.getRemoved() == 0){
				long fileEntryId = tempDossierFile.getFileEntryId();
				
				DLFileEntry fileEntry = null;
				try {
					fileEntry = DLFileEntryLocalServiceUtil.getDLFileEntry(fileEntryId);
				} catch (Exception e){}
				
				if (Validator.isNotNull(fileEntry)){
					totalUploadFileSizeInByte += fileEntry.getSize();
				}
			}
		}
	}
%>

<portlet:actionURL var="addAttachmentFileURL" name="addAttachmentFile"/>
	
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
	exception="<%= FileSizeException.class %>" 
	message="<%= FileSizeException.class.getName() %>" 
/>

<liferay-ui:error 
	exception="<%= FileExtensionException.class %>" 
	message="<%= FileExtensionException.class.getName() %>" 
/>

<liferay-ui:error 
	exception="<%= DuplicateFileException.class %>" 
	message="<%= MessageKeys.DOSSIER_FILE_DUPLICATE_NAME %>"
/>

<liferay-ui:error 
    exception="<%= RolePermissionsException.class %>" 
    message="<%= RolePermissionsException.class.getName() %>"
/>

<aui:form 
	name="fm" 
	method="post" 
	action="<%=addAttachmentFileURL%>" 
	enctype="multipart/form-data"
>
	<aui:input name="redirectURL" type="hidden" value="<%=Validator.isNotNull(redirectURL)?redirectURL: currentURL %>"/>
	<aui:input name="<%=DossierDisplayTerms.DOSSIER_ID %>" type="hidden" value="<%=dossierId %>"/>
	<aui:input name="groupDossierPartId" type="hidden" value="<%=groupDossierPartId %>"/>
	<aui:input name="<%=DossierFileDisplayTerms.DOSSIER_FILE_ID %>" type="hidden" value="<%=dossierFileId %>"/>
	<aui:input name="<%=DossierDisplayTerms.FILE_GROUP_ID %>" type="hidden" value="<%=fileGroupId %>"/>
	<aui:input name="<%=DossierFileDisplayTerms.DOSSIER_PART_ID %>" type="hidden" value="<%=dossierPartId %>"/>
	<aui:input name="<%=DossierFileDisplayTerms.DOSSIER_FILE_ORIGINAL %>" type="hidden" value="<%=String.valueOf(PortletConstants.DOSSIER_FILE_ORIGINAL) %>"/>
	<aui:input name="<%=DossierFileDisplayTerms.DOSSIER_FILE_TYPE %>" type="hidden" value="<%=String.valueOf(renderResponse.getNamespace().equals(StringPool.UNDERLINE + WebKeys.DOSSIER_MGT_PORTLET + StringPool.UNDERLINE)  ? PortletConstants.DOSSIER_FILE_TYPE_INPUT : PortletConstants.DOSSIER_FILE_TYPE_OUTPUT) %>"/>
	<aui:input name="<%=DossierFileDisplayTerms.GROUP_NAME %>" type="hidden" value="<%=groupName %>"/>
	<aui:input name="<%=DossierFileDisplayTerms.FILE_TYPES %>" type="hidden" value="<%=fileTypes %>"/>
	<aui:input name="<%=DossierFileDisplayTerms.MAX_UPLOAD_FILE_SIZE_IN_MB %>" type="hidden" value="<%=maxUploadFileSizeInMb %>"/>
	<aui:input name="<%=DossierFileDisplayTerms.MAX_TOTAL_UPLOAD_FILE_SIZE_IN_MB %>" type="hidden" value="<%=maxTotalUploadFileSizeInMb %>"/>
	
	<aui:row>
		<aui:col width="100">
			<aui:input name="<%= DossierFileDisplayTerms.DISPLAY_NAME %>" type="textarea" value="<%=dossierPartName %>" inlineLabel="true">
				<aui:validator name="required"/>
			</aui:input>
		</aui:col>
	</aui:row>
	<aui:row>
		<aui:col width="50">
			<aui:input name="<%= DossierFileDisplayTerms.DOSSIER_FILE_NO %>" type="text" inlineLabel="true"/>
		</aui:col>
		<aui:col width="50">
			
			<label class="control-label custom-lebel" for='<portlet:namespace/><%=DossierFileDisplayTerms.DOSSIER_FILE_DATE %>'>
				<liferay-ui:message key="dossier-file-date"/>
			</label>
		
			<liferay-ui:input-date
				dayParam="<%=DossierFileDisplayTerms.DOSSIER_FILE_DATE_DAY %>"
				dayValue="<%=spd.getDayOfMoth() %>"
				disabled="<%= false %>"
				monthParam="<%=DossierFileDisplayTerms.DOSSIER_FILE_DATE_MONTH %>"
				monthValue="<%=spd.getMonth() %>"
				name="<%=DossierFileDisplayTerms.DOSSIER_FILE_DATE%>"
				yearParam="<%=DossierFileDisplayTerms.DOSSIER_FILE_DATE_YEAR %>"
				yearValue="<%=spd.getYear() %>"
				formName="fm"
				autoFocus="<%=true %>"
				nullable="<%=dossierFile == null || dossierFile.getDossierFileDate() == null ? true : false %>"
			/>

		</aui:col>
	</aui:row>
	<aui:row>
		<aui:col width="100">
			<aui:input name="<%=DossierFileDisplayTerms.DOSSIER_FILE_UPLOAD %>" type="file">
				<aui:validator name="acceptFiles">
					<%
						if (fileTypes == StringPool.BLANK){
					%>
							'<%= StringUtil.merge(PortletPropsValues.ACCOUNTMGT_FILE_TYPE) %>'
					<%
						} else {
							String[] fileTypeArr = fileTypes.split("\\W+");
					%>
							'<%= StringUtil.merge(fileTypeArr, ", ") %>'
					<%
						}
					%>
				</aui:validator>
			</aui:input>
		</aui:col>
	</aui:row>
	
	<aui:row>
		<aui:button name="agree" value="agree"/>
		<aui:button name="cancel" type="button" value="cancel"/>
	</aui:row>
</aui:form>

<aui:script use="aui-base,aui-io">
	AUI().ready(function(A){
		
		var cancelButton = A.one('#<portlet:namespace/>cancel');
		var agreeButton = A.one('#<portlet:namespace/>agree');
		var success = '<%=success%>';
		
		if(cancelButton){
			cancelButton.on('click', function(){
				<portlet:namespace/>closeDialog();
			});
		}
		
		if(success == 'true'){
			<portlet:namespace/>closeDialog();
		}
		
		// Validate size and type file upload
		
		var maxUploadFileSizeInMb = <%=maxUploadFileSizeInMb %>;
		var maxUploadFileSizeInByte = maxUploadFileSizeInMb*1024*1024;
		
		var maxTotalUploadFileSizeInMb = <%=maxTotalUploadFileSizeInMb%>
		var maxTotalUploadFileSizeInByte = maxTotalUploadFileSizeInMb*1024*1024;
		
		var fileUploadSizeInByte = 0;
		var totalUploadFileSizeInByte = <%=totalUploadFileSizeInByte%>;
		
		$('#<portlet:namespace />dossierFileUpload').on('change', function() {
			fileUploadSizeInByte = this.files[0].size;
			totalUploadFileSizeInByte += fileUploadSizeInByte;
		});
		
		if(agreeButton) {
			agreeButton.on('click', function() {
				
				if (fileUploadSizeInByte == 0){
					alert('<%= LanguageUtil.get(themeDisplay.getLocale(), "please-upload-dossier-part-required-before-send") %>');
				} else
				if (fileUploadSizeInByte > maxUploadFileSizeInByte && maxUploadFileSizeInByte > 0) {
					alert('<%= LanguageUtil.get(themeDisplay.getLocale(), "please-upload-dossier-part-size-smaller-than") %>' + ' ' + maxUploadFileSizeInMb + ' Mb');
				}else 
				if (totalUploadFileSizeInByte > maxTotalUploadFileSizeInByte && maxTotalUploadFileSizeInByte > 0) {
					alert('<%= LanguageUtil.get(themeDisplay.getLocale(), "overload-total-file-upload-size") %>' + ' ' + maxTotalUploadFileSizeInMb + ' Mb');
				}else 
				{
					submitForm(document.<portlet:namespace />fm);
				}
				
			});
		}
		
	});

	Liferay.provide(window, '<portlet:namespace/>closeDialog', function() {
		var data = {
			'conserveHash': true
		};
		
		Liferay.Util.getOpener().Liferay.Portlet.refresh('#p_p_id' + '<portlet:namespace/>', data);
		var dialog = Liferay.Util.getWindow('<portlet:namespace/>' + '<%=modalDialogId%>');
		dialog.destroy(); // You can try toggle/hide whate
	});

</aui:script>