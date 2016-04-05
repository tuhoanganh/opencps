
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
<%@page import="org.opencps.usermgt.util.UserMgtUtil"%>
<%@page import="org.opencps.accountmgt.model.Business"%>
<%@page import="org.opencps.accountmgt.model.Citizen"%>
<%@page import="org.opencps.accountmgt.search.BusinessDisplayTerms"%>
<%@page import="org.opencps.accountmgt.search.CitizenDisplayTerms"%>
<%@ include file="../init.jsp" %>

<%
	long citizenId = ParamUtil.getLong(request, CitizenDisplayTerms.CITIZEN_ID);
	long businessId = ParamUtil.getLong(request, BusinessDisplayTerms.BUSINESS_BUSINESSID);
	String [] ProfileSections = null;
	String [][] categorySections = null;
	String path = StringPool.BLANK;
	Citizen citizen = null;
	Business business = null;
	
	if(citizenId > 0 ) {
		ProfileSections = new String[2];
		ProfileSections[0] = "general_info";
		ProfileSections[1] = "edit_password";
		
		path = "/html/portlets/accountmgt/registration/citizen/";
	} else if(businessId > 0) {
		
		ProfileSections = new String[3];
		ProfileSections[0] = "general_info";
		ProfileSections[1] = "contact";
		ProfileSections[2] = "edit_password";
		
		path = "/html/portlets/accountmgt/registration/business/";
	}
	String [][] categorySectionss = {ProfileSections};
	categorySections = categorySectionss; 
	
	
	
%>

<portlet:actionURL var="updateCitizenProfileURL" name="updateCitizenProfile" >
	<portlet:param name="returnURL" value="<%=currentURL %>"/>
</portlet:actionURL>

<portlet:actionURL var="updateBusinessProfileURL" name="updateBusinessProfile" >
	<portlet:param name="returnURL" value="<%=currentURL %>"/>
</portlet:actionURL>

<liferay-util:buffer var="htmlTop">
	<liferay-ui:icon iconCssClass="icon-home" />
</liferay-util:buffer>

<liferay-util:buffer var="htmlBot">

</liferay-util:buffer>

<aui:form 
	name="fm" 
	method="post" 
	action='<%= citizenId > 0 ? updateCitizenProfileURL.toString() : updateBusinessProfileURL.toString() %>'
	enctype="multipart/form-data"	
>
	
	<liferay-ui:form-navigator 
		backURL="<%= currentURL %>"
		categoryNames= "<%= UserMgtUtil._PROFILE_CATEGORY_NAMES %>"	
		categorySections="<%=categorySections %>" 
		htmlBottom="<%= htmlBot %>"
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

