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
<%@page import="org.opencps.util.PortletUtil"%>
<%@page import="org.opencps.util.DateTimeUtil"%>
<%@page import="java.util.Date"%>
<%@page import="org.opencps.util.PortletPropsValues"%>
<%@page import="org.opencps.usermgt.search.EmployeeDisplayTerm"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="org.opencps.usermgt.model.Employee"%>
<%@ include file="../../init.jsp"%>


<%
	Employee employee = (Employee)request.getAttribute(WebKeys.EMPLOYEE_ENTRY);

	Date defaultBirthDate = employee != null && employee.getBirthdate() != null ? 
			employee.getBirthdate() : DateTimeUtil.convertStringToDate("01/01/1970");
	PortletUtil.SplitDate spd = new PortletUtil.SplitDate(defaultBirthDate);
%>

<aui:model-context bean="<%=employee%>" model="<%=Employee.class%>" />

<aui:row>
	<aui:col width="50">
		<aui:input 
			name="<%=EmployeeDisplayTerm.EMPLOYEE_NO %>" 
		>
			<aui:validator name="required"/>
			<aui:validator name="maxLength">
				<%=PortletPropsValues.USERMGT_EMPLOYEE_EMPLOYEENO_LENGTH %>
			</aui:validator>
		</aui:input>
	</aui:col>
	<aui:col width="50"></aui:col>
</aui:row>
<aui:row>
	<aui:col width="50">
		<aui:input 
			name="<%=EmployeeDisplayTerm.FULL_NAME %>" 
		>
			<aui:validator name="required"/>
			<aui:validator name="maxLength">
				<%=PortletPropsValues.USERMGT_EMPLOYEE_FULLNAME_LENGTH %>
			</aui:validator>
		</aui:input>
		
		<label class="control-label custom-lebel" for='<portlet:namespace/><%=EmployeeDisplayTerm.BIRTH_DATE %>'>
			<liferay-ui:message key="birth-date"/>
		</label>
		
		<liferay-ui:input-date
			dayParam="<%=EmployeeDisplayTerm.BIRTH_DATE_DAY %>"
			dayValue="<%= spd.getDayOfMoth() %>"
			disabled="<%= false %>"
			monthParam="<%=EmployeeDisplayTerm.BIRTH_DATE_MONTH %>"
			monthValue="<%= spd.getMonth() %>"
			name="<%=EmployeeDisplayTerm.BIRTH_DATE %>"
			yearParam="<%=EmployeeDisplayTerm.BIRTH_DATE_YEAR %>"
			yearValue="<%= spd.getYear() %>"
			formName="fm"
			autoFocus="<%=true %>"
		/>
		
		
		<aui:input 
			name="<%= EmployeeDisplayTerm.MOBILE%>"
		>
			<aui:validator name="maxLength">
				<%=PortletPropsValues.USERMGT_EMPLOYEE_MOBILE_LENGTH%>
			</aui:validator>
		</aui:input>
	</aui:col>
	
	<aui:col width="50">
		<aui:select name="<%= EmployeeDisplayTerm.GENDER%>">
			<%
				if(PortletPropsValues.USERMGT_GENDER_VALUES != null && 
					PortletPropsValues.USERMGT_GENDER_VALUES.length > 0){
					for(int g = 0; g < PortletPropsValues.USERMGT_GENDER_VALUES.length; g++){
						%>
							<aui:option 
								value="<%= PortletPropsValues.USERMGT_GENDER_VALUES[g]%>"
								selected="<%=employee != null && employee.getGender() == PortletPropsValues.USERMGT_GENDER_VALUES[g]%>"
							>
								<%= PortletUtil.getGender(PortletPropsValues.USERMGT_GENDER_VALUES[g], locale)%>
							</aui:option>
						<%
					}
				}
			%>
		</aui:select>
		
		<aui:input 
			name="<%= EmployeeDisplayTerm.EMAIL%>"
		>
			<aui:validator name="required"/>
			<aui:validator name="email"/>
			<aui:validator name="maxLength">
				<%=PortletPropsValues.USERMGT_EMPLOYEE_EMAIL_LENGTH %>
			</aui:validator>
		</aui:input>
		
		<aui:input 
			name="<%= EmployeeDisplayTerm.TEL_NO%>"
		>
			<aui:validator name="maxLength">
				<%=PortletPropsValues.USERMGT_EMPLOYEE_TELNO_LENGTH%>
			</aui:validator>
		</aui:input>
	</aui:col>
</aui:row>


