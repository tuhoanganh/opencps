
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
<%@page import="org.opencps.util.MessageKeys"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="org.opencps.accountmgt.search.BusinessDisplayTerms"%>
<%@page import="org.opencps.accountmgt.model.Business"%>
<%@ include file="../init.jsp" %>

<%
	Business business = (Business) request.getAttribute(WebKeys.BUSINESS_ENTRY);
	long businessId = business!=null ? business.getBusinessId() : 0L;
	
%>


<portlet:actionURL var="updateBusinessURL" name="updateBusiness">
	<portlet:param 
		name="<%=BusinessDisplayTerms.BUSINESS_BUSINESSID %>" 
		value="<%=String.valueOf(businessId) %>"
	/>	
</portlet:actionURL>

<aui:form 
	action="<%=updateBusinessURL.toString() %>"
	name="fm"	
	method="post"
	enctype="multipart/form-data"
>
	<liferay-util:include 
		page="/html/portlets/accountmgt/registration/business/general_info.jsp" 
		servletContext="<%=application %>" 
	/> 
	
	<liferay-util:include 
		page="/html/portlets/accountmgt/registration/business/contact.jsp" 
		servletContext="<%=application %>" 
	/> 
	<aui:row>
		<aui:input 
			name="businessConfirm"
			type="checkbox" 
			label="<%=LanguageUtil.get(pageContext, 
						MessageKeys.ACCOUNTMGT_CONFIRM_KEY) %>"
			checked="false"
		/>
	</aui:row>
	
	<aui:row>
		<aui:button name="businessSubmit" type="submit" />
	</aui:row>
</aui:form>

<aui:script>

AUI().ready(function(A) {

	var businessConfirmCheckbox = A.one('#<portlet:namespace />businessConfirmCheckbox');
	
	if(businessConfirmCheckbox) {
		var buttonSubmit = A.one('#<portlet:namespace />businessSubmit');
		businessConfirmCheckbox.on('click',function() {
			businessConfirmCheckboxInput = A.one('#<portlet:namespace />businessConfirm');
			if(businessConfirmCheckboxInput.val() == 'true') {
				alert(businessConfirmCheckboxInput.val());
				buttonSubmit.attr('disabled', false);
				
			}
		});
	}
	
});

</aui:script>