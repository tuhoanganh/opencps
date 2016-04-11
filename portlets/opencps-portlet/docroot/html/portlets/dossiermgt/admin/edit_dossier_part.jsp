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
<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="org.opencps.dossiermgt.model.DossierPart"%>
<%@page import="org.opencps.dossiermgt.search.DossierPartDisplayTerms"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@page import="org.opencps.dossiermgt.util.DossierMgtUtil"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="org.opencps.dossiermgt.service.DossierPartLocalServiceUtil"%>
<%
	DossierPart dossierPart = (DossierPart) request.getAttribute(WebKeys.DOSSIER_PART_ENTRY);
	DossierPart dossierPartIsAddChilds = null;
	long dossierPartId = dossierPart != null ? dossierPart.getDossierpartId() : 0L;
	int [] dossierType = {1,2,3,4,5};
	String isAddChilds = ParamUtil.getString(request, "isAddChild");
	try {
		
		
 	} catch(Exception e) {
		_log.error(e);
	}
%>

<portlet:actionURL name="updateDossierPart" var="updateDossierPartURL" >
	<portlet:param 
		name="<%=DossierPartDisplayTerms.DOSSIERPART_DOSSIERPARTID %>" 
		value="<%=String.valueOf(dossierPartId)%>"
	/>
</portlet:actionURL>

<aui:form 
	action="<%=updateDossierPartURL.toString() %>"
	method="post"
	name="fm"
>	
	<c:choose>
		<c:when test="<%=Validator.isNotNull(isAddChilds)%>">
			<aui:model-context bean="<%=dossierPartIsAddChilds%>" model="<%=DossierPart.class%>" />
		</c:when>
		<c:otherwise>
			<aui:model-context bean="<%=dossierPart%>" model="<%=DossierPart.class%>" />
		</c:otherwise>
	</c:choose>
	
	<aui:row>
		<aui:col cssClass="input60">
			<aui:input name="<%=DossierPartDisplayTerms.DOSSIERPART_PARTNAME %>">
				<aui:validator name="required" />
				<aui:validator name="maxLength">255</aui:validator>
			</aui:input>
		</aui:col>
		
		<aui:col cssClass="input30">
			<aui:input name="<%=DossierPartDisplayTerms.DOSSIERPART_PARTNO %>">
				<aui:validator name="required" />
				<aui:validator name="maxLength">100</aui:validator>
			</aui:input>
		</aui:col>
	</aui:row>
	
	<aui:row>
		<aui:input 
			name="<%=DossierPartDisplayTerms.DOSSIERPART_PARTTIP %>"
			cssClass="input90"
		>
			<aui:validator name="requied" />
			<aui:validator name="maxLength">255</aui:validator>
		</aui:input>
	</aui:row>
	
	<aui:row>
		<aui:col cssClass="input60">
			<aui:select name="<%=DossierPartDisplayTerms.DOSSIERPART_PARENTID %>">
				<c:choose>
					<c:when test="<%=Validator.isNotNull(isAddChilds) && Validator.isNotNull(dossierPart)%>">
						<aui:option value="<%=dossierPart.getDossierpartId() %>"></aui:option>
					</c:when>
					<c:otherwise>
						<aui:option value="<%=0 %>">
							<liferay-ui:message key="root" />
						</aui:option>
					</c:otherwise>
				</c:choose>
			</aui:select>
		</aui:col>
		
		<aui:col cssClass="input30">
			<aui:input name="<%=DossierPartDisplayTerms.DOSSIERPART_SIBLING %>">
				<aui:validator name="required" />
				<aui:validator name="numbers" />
			</aui:input>
		</aui:col>
	</aui:row>
	
	<aui:row>
		<aui:col cssClass="input30">
			<aui:select name="<%=DossierPartDisplayTerms.DOSSIERPART_PARTTYPE %>">
				<aui:option value="<%=0 %>">
					<liferay-ui:message key="root" />
				</aui:option>
				<%
					for(int dosType : dossierType) {
						%>
							<aui:option value="<%=dosType %>">
								<%=DossierMgtUtil.getNameOfPartType(dosType, themeDisplay.getLocale()) %>
							</aui:option>
						<%
					}
				%>
			</aui:select>
		</aui:col>
		
		<aui:col cssClass="input30">
			<aui:input name="<%=DossierPartDisplayTerms.DOSSIERPART_TEMPLATEFILENO %>">
				<aui:validator name="required" />
				<aui:validator name="maxLength"> 100</aui:validator>
			</aui:input>
		</aui:col>
		
		<aui:col cssClass="input30">
			<aui:input 
			name="<%=DossierPartDisplayTerms.DOSSIERPART_REQUIRED %>"
			type="checkboxs"	
		/>
		</aui:col>
	</aui:row>
		
	<aui:row>
		<aui:col cssClass="input90">
			<aui:input name="<%=DossierPartDisplayTerms.DOSSIERPART_FORMSCRIPT %>" />
		</aui:col>
	</aui:row>
	
	<aui:row>
		<aui:input name="<%=DossierPartDisplayTerms.DOSSIERPART_SAMPLEDATA %>" />
	</aui:row>
	
	<aui:button name="submit" value="submit"/>
</aui:form>

<aui:script>

AUI().ready(function(A) {
	var partType = A.one('#<portlet:namespace /><%=DossierPartDisplayTerms.DOSSIERPART_PARTTYPE %>');
	if(partType) {
		var formScript = A.one('#<portlet:namespace /><%=DossierPartDisplayTerms.DOSSIERPART_FORMSCRIPT %>');
		partType.on('change',function() {
			alert(partType.val());
			if(partType.val() == "1" || partType.val() == "2" || partType.val() == "5") {
				alert("show");
				formScript.show();
			} else {
				alert("hide");
				formScript.hide();
			}	
		});
		
	}
});

</aui:script>

<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.dossiermgt.admin.edit_dossier_part.jsp");
%>