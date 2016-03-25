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
<%@page import="org.opencps.usermgt.search.EmployeeDisplayTerm"%>
<%@page import="org.opencps.usermgt.service.WorkingUnitLocalServiceUtil"%>
<%@page import="org.opencps.usermgt.model.WorkingUnit"%>
<%@page import="org.opencps.usermgt.model.JobPos"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="org.opencps.usermgt.model.Employee"%>
<%@page import="java.util.List"%>
<%@page import="org.opencps.usermgt.service.EmployeeLocalServiceUtil"%>
<%@page import="org.opencps.usermgt.service.JobPosLocalServiceUtil"%>
<%@page import="java.util.ArrayList"%>
<%@ include file="../../init.jsp"%>
<%

	Employee employee = (Employee)request.getAttribute(WebKeys.EMPLOYEE_ENTRY);

	WorkingUnit mappingWorkingUnit = (WorkingUnit)request.getAttribute(WebKeys.WORKING_UNIT_MAPPING_ENTRY);
	
	JobPos mainJobPos = (JobPos)request.getAttribute(WebKeys.MAIN_JOB_POS_ENTRY);
	
	List<JobPos> jobPoses = new ArrayList<JobPos>();
	
	if(employee != null){
		try{
			jobPoses = JobPosLocalServiceUtil.getEmployeeJobPoses(employee.getEmployeeId());
		}catch(Exception e){
			// Nothing todo
		}
	}
	
	int[] jobPosIndexes = null;

	String jobPosIndexesParam = ParamUtil.getString(request, "jobPosIndexesParam");

	if (jobPoses != null && !jobPoses.isEmpty()) {
		
		jobPosIndexes = new int[jobPoses.size()];
		
		int count = 0;
		
		for(JobPos jobPos : jobPoses){
			jobPosIndexes[count] = count;
			count ++;
		}
	}
	else {
		if (jobPosIndexes == null) {
			jobPosIndexes = new int[] {0};
		}
	}
	
	List<WorkingUnit> workingUnits = WorkingUnitLocalServiceUtil.getWorkingUnit(scopeGroupId, true);
%>

<aui:model-context bean="<%=mappingWorkingUnit%>" model="<%=WorkingUnit.class%>" />

<aui:row>
	<aui:col width="100">
		<aui:select 
			name="<%= EmployeeDisplayTerm.WORKING_UNIT_ID %>" 
			cssClass="input95"
			showEmptyOption="<%=true %>"
			required="<%=true %>"
		>
			<%
				if(workingUnits != null){
					for(WorkingUnit workingUnit : workingUnits){
						%>
							<aui:option 
								value="<%= workingUnit.getWorkingunitId()%>"
								selected="<%=mappingWorkingUnit != null && mappingWorkingUnit.getWorkingunitId() == workingUnit.getWorkingunitId()%>"
							>
								<%=workingUnit.getName() %>
							</aui:option>
						<%
					}
				}
			%>
		</aui:select>
	</aui:col>
</aui:row>

<aui:row>
	<aui:col width="100">
		<aui:input 
			name="<%= EmployeeDisplayTerm.WORKING_STATUS  %>" 
			type="checkbox" 
			inlineField="<%= true %>" 
			inlineLabel="right"
			value="<%=employee != null ? employee.getWorkingStatus() : 0 %>"
		/>
	</aui:col>
</aui:row>

<label><liferay-ui:message key="main-jobpos"/></label>
<aui:row id="mainJobPosBoundingBox">
	<aui:col width="50">
		<aui:select 
			name='<%= EmployeeDisplayTerm.WORKING_UNIT_ID%>' 
			label="" 
			onChange='<%=renderResponse.getNamespace() + "getJobPosByWorkingUnitId(this)" %>'
			required="<%=true %>"
		>
			<aui:option value=""><liferay-ui:message key="select-working-unit"/></aui:option>
		</aui:select>
	</aui:col>
	<aui:col width="50">
		<aui:select 
			name='<%=EmployeeDisplayTerm.MAIN_JOBPOS_ID %>' 
			label="" 
			required="<%=true %>">
			<aui:option value=""><liferay-ui:message key="select-jobpos"/></aui:option>
		</aui:select>
	</aui:col>
</aui:row>

<label><liferay-ui:message key="other-jobpos"/></label>
<aui:row id="opencps-usermgt-employee-jobpos">
	<aui:fieldset id="boundingBox">
	<%
		for(int i = 0; i < jobPosIndexes.length; i++){
			int jobPosIndex = jobPosIndexes[i];
			%>
				<div class="lfr-form-row lfr-form-row-inline">
					<div class="row-fields">
						<aui:col width="50">
							<aui:select 
								name='<%= EmployeeDisplayTerm.WORKING_UNIT_ID + jobPosIndex %>' 
								label="" 
								onChange='<%=renderResponse.getNamespace() + "getJobPosByWorkingUnitId(this)" %>'
							>
								<aui:option value=""><liferay-ui:message key="select-working-unit"/></aui:option>
							</aui:select>
						</aui:col>
						<aui:col width="50">
							<aui:select 
								name='<%= "jobPosId" + jobPosIndex %>' 
								label=""
							>
								<aui:option value=""><liferay-ui:message key="select-jobpos"/></aui:option>
							</aui:select>
						</aui:col>
					</div>
				</div>
				
			<%
		}
	%>
	</aui:fieldset>
	
	<aui:input name="jobPosIndexes" type="hidden" value="<%= StringUtil.merge(jobPosIndexes)%>" />
</aui:row>
