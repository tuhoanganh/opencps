
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
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="org.opencps.accountmgt.model.Citizen"%>
<%@page import="org.opencps.accountmgt.search.CitizenDisplayTerms"%>
<%@page import="org.opencps.util.MessageKeys"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>

<%@ include file="../init.jsp" %>

<%
	Citizen citizen = (Citizen) request.getAttribute(WebKeys.CITIZEN_ENTRY);
	long citizenID = citizen != null ? citizen.getCitizenId() : 0L;
%>


<portlet:actionURL var="updateCitizenURL" name="updateCitizen">
	<portlet:param 
		name="<%=CitizenDisplayTerms.CITIZEN_ID %>" 
		value="<%=String.valueOf(citizenID) %>"
	/>	
</portlet:actionURL>

<aui:form 
	action="<%=updateCitizenURL.toString() %>"
	name="fm"	
	method="post"
	enctype="multipart/form-data"
>
	<liferay-util:include 
		page="/html/portlets/accountmgt/registration/citizen/general_info.jsp" 
		servletContext="<%=application %>" 
	/> 
	<aui:row>
		<aui:input 
			name="citizenConfirm"
			type="checkbox" 
			label="<%=LanguageUtil.get(pageContext, 
						MessageKeys.ACCOUNTMGT_CONFIRM_KEY) %>"
		/>
	</aui:row>
	<aui:row>
		<aui:button name="citizenSubmit" type="submit" />
	</aui:row>
</aui:form>

<aui:script>

AUI().ready(function(A) {

	var checkboxConFirm = A.one('#<portlet:namespace />citizenConfirmCheckbox');
	
	if(checkboxConFirm) {
		var buttonSubmit = A.one('#<portlet:namespace />citizenSubmit');
		checkboxConFirm.on('click',function() {
			
			var checkboxConFirmInput = A.one('#<portlet:namespace />citizenConfirm');
			
			if(checkboxConFirmInput.val() == 'true') {
				
				if(buttonSubmit) {
					alert(buttonSubmit.val() + " alert(buttonSubmit.val())");
					
				}
			}
		});
	}
	
});

</aui:script>