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
<%@page import="org.opencps.accountmgt.search.BusinessDisplayTerms"%>
<%@page import="org.opencps.accountmgt.service.BusinessLocalServiceUtil"%>
<%@page import="org.opencps.accountmgt.model.Business"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.liferay.portal.service.UserGroupLocalServiceUtil"%>
<%@page import="com.liferay.portal.model.UserGroup"%>
<%@page import="org.omg.PortableInterceptor.USER_EXCEPTION"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@ include file="/init.jsp" %>

<%

	Long userId = (Long) request.getAttribute(WebKeys.MAPPING_USERID);
	
	List<UserGroup> userGroups = new ArrayList<UserGroup>();
	String accountType = "";
	String [] ProfileSections = null;
	String path = "";
	String [][] categorySections = null;
	Citizen citizen = null;
	Business business = null;
	User userLogin = null;
	PasswordPolicy passwordPolicy = null;
	long citizenId = 0;
	long businessId = 0;
	
	try {
		
		userLogin = UserLocalServiceUtil.getUser(userId);
		userGroups = userLogin.getUserGroups();
		if (!userGroups.isEmpty()) {
			for(UserGroup userGroup : userGroups) {
				if(userGroup.getName().equals("CITIZEN") 
								|| userGroup.getName().equals("BUSINESS")) {
					accountType =  userGroup.getName() ;
					break;
				}
				
			}
		} else {
			_log.info("empty");
		}
		
		if(accountType.equals("CITIZEN")) {
			citizen = CitizenLocalServiceUtil.getCitizen(userId);
		
			ProfileSections = new String[2];
			ProfileSections[0] = "general_info";
			ProfileSections[1] = "edit_password_citizen";
			
			path = "/html/portlets/accountmgt/registration/citizen/";
		
		} else if(accountType.equals("BUSINESS")) {
			business = BusinessLocalServiceUtil.getBusiness(userId);
			
			ProfileSections = new String[3];
			ProfileSections[0] = "contact";
			ProfileSections[1] = "general_info";
			ProfileSections[2] = "edit_password_citizen";
			
			path = "/html/portlets/accountmgt/registration/business/";
			
		} else {
			citizen = CitizenLocalServiceUtil.getCitizen(userId);
			ProfileSections = new String[2];
			ProfileSections[0] = "general_info";
			ProfileSections[1] = "edit_password_citizen";
			
			path = "/html/portlets/accountmgt/registration/citizen/";
		}
		
		String [][] categorySectionss = {ProfileSections};
		categorySections = categorySectionss; 
		
		if(userLogin != null) {
			passwordPolicy = PasswordPolicyLocalServiceUtil
							.getDefaultPasswordPolicy(company.getCompanyId());
		} else {
			passwordPolicy = userLogin.getPasswordPolicy();
		}
		
		if(citizen != null  ) {
			citizenId = citizen.getCitizenId();
			
		} else if(business != null) {
			businessId = business.getBusinessId();
		}
		
	} catch(Exception e) {
		_log.error(e);
	}
	_log.info("accountType " + accountType);
	
%>

<portlet:actionURL var="updateCitizenProfileURL" name="updateCitizenProfile" >
	<portlet:param name="returnURL" value="<%=currentURL %>"/>
</portlet:actionURL>

<portlet:actionURL var="updateBusinessProfileURL" name="updateBusinessProfile" >
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
	action='<%=accountType.equals("CITIZEN") ? updateCitizenProfileURL.toString() : updateBusinessProfileURL.toString() %>'>
	<liferay-ui:form-navigator 
		backURL="<%= currentURL %>"
		categoryNames= "<%= UserMgtUtil._CITIZEN_CATEGORY_NAMES %>"	
		categorySections="<%=categorySections %>" 
		htmlBottom="<%= htmlBott %>"
		htmlTop="<%= htmlTop %>"
		jspPath="<%=path%>"
		>	
	</liferay-ui:form-navigator>
	
	<c:choose>
		<c:when test="<%=citizenId > 0 %>">
			<aui:input 
				name="<%=CitizenDisplayTerms.CITIZEN_ID %>" 
				value="<%=String.valueOf(citizen != null ? citizen.getCitizenId() : 0) %>"
				type="hidden"
			/>
		</c:when>
		<c:otherwise>
			<aui:input 
				name="<%=BusinessDisplayTerms.BUSINESS_BUSINESSID %>" 
				value="<%=String.valueOf(business != null ? business.getBusinessId() : 0) %>"
				type="hidden"
			/>
		</c:otherwise>
	</c:choose>
	
	
</aui:form>

<%!
	private Log _log = LogFactoryUtil.getLog(".html.portlets.accountmgt.citizenprofile.jsp");
%>