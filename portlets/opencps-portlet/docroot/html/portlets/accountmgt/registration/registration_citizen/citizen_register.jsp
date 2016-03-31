
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
<%@page import="org.opencps.accountmgt.search.CitizenDisplayTerms"%>
<%@page import="org.opencps.util.PortletUtil"%>
<%@page import="java.util.Date"%>
<%@page import="org.opencps.util.DateTimeUtil"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="org.opencps.accountmgt.model.Citizen"%>
<%@page import="org.opencps.util.MessageKeys"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@ include file="../../init.jsp" %>

<%

	Citizen citizen = (Citizen) request.getAttribute(WebKeys.CITIZEN_ENTRY);
	long citizenID = citizen != null ? citizen.getCitizenId() : 0L;
	
	Date defaultBirthDate = citizen != null && citizen.getBirthdate() != null ? 
		citizen.getBirthdate() : DateTimeUtil.convertStringToDate("01/01/1970");
		PortletUtil.SplitDate spd = new PortletUtil.SplitDate(defaultBirthDate);
%>
<portlet:actionURL var="updateCitizenURL" name="updateCitizen">
	<portlet:param name="<%=CitizenDisplayTerms.CITIZEN_ID %>" value="<%=citizenID %>"/>
</portlet:actionURL>

<aui:form 
	action="<%=updateCitizenURL.toString() %>"
	name="fm"	
	method="post"
>
	<aui:model-context bean="<%=citizen%>" model="<%=Citizen.class%>" />

	<aui:row>
		
		<aui:col width="50">
			<aui:input name="<%=CitizenDisplayTerms.CITIZEN_ID %>" />
			<aui:validator name="required" />
			<aui:validator name="maxLength">255</aui:validator>
		</aui:col>
		
		<aui:col width="50">
			<aui:input name="<%=CitizenDisplayTerms.CITIZEN_PERSONALID %>" />
			<aui:validator name="required" />
		</aui:col>
		
	</aui:row>
	
	<aui:row>
	
		<aui:col width="50">
			<liferay-ui:input-date 
				dayParam="<%=CitizenDisplayTerms.BIRTH_DATE_DAY %>"
				dayValue="<%= spd.getDayOfMoth() %>"
				disabled="<%= false %>"
				monthParam="<%=CitizenDisplayTerms.BIRTH_DATE_MONTH %>"
				monthValue="<%= spd.getMonth() %>"
				name="<%=CitizenDisplayTerms.CITIZEN_BIRTHDATE %>"
				yearParam="<%=CitizenDisplayTerms.BIRTH_DATE_YEAR %>"
				yearValue="<%= spd.getYear() %>"
				formName="fm"
				autoFocus="<%=true %>"
			/>	
		</aui:col>
		
		<aui:col width="50">
			<aui:select name="<%=CitizenDisplayTerms.CITIZEN_GENDER %>">
				<%
					if(PortletPropsValues.USERMGT_GENDER_VALUES != null && 
						PortletPropsValues.USERMGT_GENDER_VALUES.length > 0){
						for(int g = 0; g < PortletPropsValues.USERMGT_GENDER_VALUES.length; g++){
							%>
								<aui:option 
									value="<%= PortletPropsValues.USERMGT_GENDER_VALUES[g]%>"
									selected="<%=citizen != null && citizen.getGender() == PortletPropsValues.USERMGT_GENDER_VALUES[g]%>"
								>
									<%= PortletUtil.getGender(PortletPropsValues.USERMGT_GENDER_VALUES[g], locale)%>
								</aui:option>
							<%
						}
					}
				%>
			</aui:select>
		</aui:col>
	</aui:row>
	
	<aui:row>
		<aui:input 
			name="<%=CitizenDisplayTerms.CITIZEN_ADDRESS %>" 
			cssClass="input100"
		>
			<aui:validator name="required" />
			<aui:validator name="maxLength">255</aui:validator>
		</aui:input>
	</aui:row>
	
	<aui:row>
		<datamgt:ddr 
			cssClass="input100"
			depthLevel="3" 
			dictCollectionCode="ADMINISTRATIVE_REGION"
			itemNames="cityCode,districtCode,wardCode"
			itemsEmptyOption="true,true,true"	
		>	
		</datamgt:ddr>
	</aui:row>
	
	<aui:row>
		<aui:col width="50">
			<aui:input name="<%=CitizenDisplayTerms.CITIZEN_EMAIL %>">
				<aui:validator name="required" />
				<aui:validator name="maxLength">255</aui:validator>	
			</aui:input>
		</aui:col>
		
		<aui:col width="50">
			<aui:input name=""></aui:input>
		</aui:col>
	</aui:row>
	
	<aui:row>
		<aui:input type="file" name="<%=CitizenDisplayTerms.CITIZEN_ATTACHFILE %>" />
	</aui:row>
	
	<aui:row>
		<aui:input 
			name="confirm"
			type="checkbox" 
			label="<%=LanguageUtil.get(pageContext, 
						MessageKeys.ACCOUNTMGT_CONFIRM_KEY) %>"
		>
		
		</aui:input>
	</aui:row>
	<aui:row>
		<aui:input name="submit" type="submit" value="resgister" disabled="true"></aui:input>
	</aui:row>
	
</aui:form>

<aui:script>

AUI().ready(function(A) {
	var checkbox = A.one('<portlet:namespace />checkbox');
	if(checkbox) {
		var buttonSubmit = A.one('<portlet:namespace />submit');
		if(checkbox.val() == 'true') {
			buttonSubmit.setAttribute('disabled', 'false');
		}
	}
});

</aui:script>