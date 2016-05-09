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

<%
	long processWorkflowId = ParamUtil.getLong(request, "processWorkflowId");
	long serviceProcessId = ParamUtil.getLong(request, "serviceProcessId");
%>

<aui:form name="fm" action="" method="post">
	<aui:select name="assignToUser" label="assign-to-user">
		
	</aui:select>
	
	<aui:input name="requestPayment" label="request-payment" type="text"/>
	
	<aui:input name="receptionNo" label="reception-no" type="text"/>
	
	<aui:input name="deadline"/>
	
	<label class="control-label custom-lebel" for='<portlet:namespace/><%="deadline" %>'>
		<liferay-ui:message key="deadline"/>
	</label>
		
		<liferay-ui:input-date
			dayParam="deadlineDay"
			disabled="<%= false %>"
			monthParam="deadlineMonth"
			name="deadline"
			yearParam="deadlineYear"
			formName="fm"
			autoFocus="<%=true %>"
			nullable="<%=true %>"
		/>
</aui:form>