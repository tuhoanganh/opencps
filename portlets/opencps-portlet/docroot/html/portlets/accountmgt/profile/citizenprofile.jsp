
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
<%@page import="com.liferay.portal.model.User"%>
<%@page import="com.liferay.portal.service.UserLocalServiceUtil"%>
<%@page import="org.opencps.accountmgt.service.CitizenLocalServiceUtil"%>
<%@page import="org.opencps.accountmgt.model.Citizen"%>
<%@page import="com.liferay.portal.kernel.util.HtmlUtil"%>
<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@page import="com.liferay.portal.service.ServiceContextFactory"%>
<%@page import="com.liferay.portal.service.ServiceContext"%>
<%@page import="org.opencps.accountmgt.search.CitizenDisplayTerms"%>
<%@page import="org.opencps.usermgt.util.UserMgtUtil"%>
<%@page import="com.liferay.portal.service.PasswordPolicyLocalServiceUtil"%>
<%@page import="com.liferay.portal.model.PasswordPolicy"%>
<%@page import="com.liferay.portal.UserLockoutException"%>
<%@ include file="/init.jsp" %>

<%
	String [] citizenProfileSections = {"general_citizen_profile", "edit_password_citizen"};
	String [][] categorySections = {citizenProfileSections};
	ServiceContext serviceContext = null;
	Citizen citizen = null;
	User userLogin = null;
	PasswordPolicy passwordPolicy = null;
	try {
		serviceContext = ServiceContextFactory.getInstance(request);
		/* citizen = CitizenLocalServiceUtil.getCitizenByMapingUserId(serviceContext.getUserId()); */
		userLogin = UserLocalServiceUtil.getUser(serviceContext.getUserId());
		if(userLogin != null) {
			passwordPolicy = PasswordPolicyLocalServiceUtil
							.getDefaultPasswordPolicy(company.getCompanyId());
		} else {
			passwordPolicy = userLogin.getPasswordPolicy();
		}
		
		
	} catch(Exception e) {
		_log.error(e);
	}
%>
<portlet:actionURL var="updateCitizenProfileURL" name="updateCitizenProfile" >
	<portlet:param name="returnURL" value="<%=currentURL %>"/>
</portlet:actionURL>

<liferay-util:buffer var="htmlTop">
	<c:if test="<%= userLogin != null %>">
		<div class="user-info">
			<div class="float-container">
				<img alt="<%= HtmlUtil.escape(userLogin.getFullName()) %>" class="user-logo" src="<%= userLogin.getPortraitURL(themeDisplay) %>" />

				<span class="user-name"><%= HtmlUtil.escape(userLogin.getFullName()) %></span>
			</div>
		</div>
	</c:if> 
</liferay-util:buffer>
<liferay-util:buffer var="htmlBott">

	<%
	boolean lockedOut = false;

	if ((userLogin != null) && (passwordPolicy != null)) {
		try {
			UserLocalServiceUtil.checkLockout(userLogin);
		}
		catch (UserLockoutException ule) {
			lockedOut = true;
		}
	}
	%>

	<c:if test="<%= lockedOut %>">
		<aui:button-row>
			<div class="alert alert-block"><liferay-ui:message key="this-user-account-has-been-locked-due-to-excessive-failed-login-attempts" /></div>

			<%
			String taglibOnClick = renderResponse.getNamespace() + "saveUser('unlock');";
			%>

			<aui:button onClick="<%= taglibOnClick %>" value="unlock" />
		</aui:button-row>
	</c:if>
</liferay-util:buffer>
<aui:form name="fm" 
	method="post" 
	action="<%=updateCitizenProfileURL.toString() %>">
	<liferay-ui:form-navigator 
		backURL="<%= currentURL %>"
		categoryNames= "<%= UserMgtUtil._WORKING_UNIT_CATEGORY_NAMES %>"	
		categorySections="<%=categorySections %>" 
		htmlBottom="<%= htmlBott %>"
		htmlTop="<%= htmlTop %>"
		jspPath='<%=templatePath + "citizen_front_office_profile/" %>'
		>	
	</liferay-ui:form-navigator>
	<aui:input 
		name="<%=CitizenDisplayTerms.CITIZEN_MAPPINGUSERID %>" 
		value="<%=String.valueOf(userLogin != null ? userLogin.getUserId() : 0) %>"
		type="hidden"
	></aui:input>
</aui:form>

<%!
	private Log _log = LogFactoryUtil.getLog(".html.portlets.accountmgt.citizenprofile.jsp");
%>