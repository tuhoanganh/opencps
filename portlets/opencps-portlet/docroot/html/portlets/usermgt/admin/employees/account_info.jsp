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
<%@page import="org.opencps.util.PortletPropsValues"%>
<%@page import="org.opencps.usermgt.search.EmployeeDisplayTerm"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="com.liferay.portal.model.User"%>
<%@ include file="../../init.jsp"%>

<%
	User mappingUser = (User)request.getAttribute(WebKeys.USER_MAPPING_ENTRY);
%>
<aui:model-context bean="<%=mappingUser%>" model="<%=User.class%>" />

<aui:row>
	<aui:col width="100">
		<aui:input 
			name="isMappingUser" 
			type="checkbox" 
			inlineField="<%= true %>" 
			inlineLabel="right" 
			disabled="<%=true %>"
			value="<%=mappingUser != null ? true : false %>"
		/>
	</aui:col>
</aui:row>

<aui:row>
	<aui:col width="50">
		<aui:input 
			name="<%= EmployeeDisplayTerm.SCREEN_NAME%>"
			disabled="<%=mappingUser != null ? true : false  %>"
		>
			<aui:validator name="required"/>
		</aui:input>
	</aui:col>
	
	<aui:col width="50">
		<aui:input 
			name="<%= EmployeeDisplayTerm.USER_EMAIL%>"
			disabled="<%=mappingUser != null ? true : false  %>"
		>
			<aui:validator name="required"/>
			<aui:validator name="email"/>
			<aui:validator name="maxLength">
				<%= PortletPropsValues.USERMGT_EMPLOYEE_EMAIL_LENGTH %>
			</aui:validator>
		</aui:input>
	</aui:col>
</aui:row>

<aui:row>
	<aui:col width="50">
		<aui:input name="<%= EmployeeDisplayTerm.PASS_WORD%>" type="password"></aui:input>
	</aui:col>
	
	<aui:col width="50">
		<aui:input name="<%= EmployeeDisplayTerm.RE_PASS_WORD%>" type="password"/>
	</aui:col>
</aui:row>