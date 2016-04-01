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
<%@page import="org.opencps.util.MessageKeys"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="org.opencps.accountmgt.search.BusinessDisplayTerms"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="org.opencps.accountmgt.model.Business"%>
<%@ include file="../../init.jsp" %>

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

<aui:form action="<%=updateBusinessURL.toString() %>" method="post" name="fm">

	<aui:row>
		<aui:col width="50">
			<aui:input name="<%=BusinessDisplayTerms.BUSINESS_NAME %>" >
				<aui:validator name="required" />
				<aui:validator name="maxLength">255</aui:validator>
			</aui:input>
		</aui:col>
		
		<aui:col width="50">
			<aui:input name="<%=BusinessDisplayTerms.BUSINESS_IDNUMBER %>">
				<aui:input name="required" />
				<aui:validator name="maxLength">100</aui:validator>
			</aui:input>
		</aui:col>
	</aui:row>
	
	<aui:row>
		<aui:col width="50">
			<aui:input name="<%= BusinessDisplayTerms.BUSINESS_ENNAME %>">
				<aui:validator name="required" />
				<aui:validator name="maxLength">255</aui:validator>
			</aui:input>
		</aui:col>
		
		<aui:col width="50">
			<aui:input name="<%=BusinessDisplayTerms.BUSINESS_SHORTNAME %>">
				<aui:validator name="maxLength">100</aui:validator>
			</aui:input>
		</aui:col>
	</aui:row>
	
	<aui:row>
		<aui:select 
			name="<%=BusinessDisplayTerms.BUSINESS_BUSINESSTYPE %>">
			<aui:option value="<%=0 %>">?????</aui:option>
		</aui:select>
	</aui:row>
	<aui:row cssClass="scrollfield">
			<aui:input 
			name="test1"
			type="checkbox" 
			label="<%=LanguageUtil.get(pageContext, 
						MessageKeys.ACCOUNTMGT_CONFIRM_KEY) %>"
			>
			</aui:input>
			
			<aui:input 
			name="test2"
			type="checkbox" 
			label="<%=LanguageUtil.get(pageContext, 
						MessageKeys.ACCOUNTMGT_CONFIRM_KEY) %>"
			>
			</aui:input>
			
			<aui:input 
			name="test3"
			type="checkbox" 
			label="<%=LanguageUtil.get(pageContext, 
						MessageKeys.ACCOUNTMGT_CONFIRM_KEY) %>"
			>
			</aui:input>
			<aui:input 
			name="test4"
			type="checkbox" 
			label="<%=LanguageUtil.get(pageContext, 
						MessageKeys.ACCOUNTMGT_CONFIRM_KEY) %>"
			>
			</aui:input>
			<aui:input 
			name="test5"
			type="checkbox" 
			label="<%=LanguageUtil.get(pageContext, 
						MessageKeys.ACCOUNTMGT_CONFIRM_KEY) %>"
			>
			</aui:input>
			<aui:input 
			name="test6"
			type="checkbox" 
			label="<%=LanguageUtil.get(pageContext, 
						MessageKeys.ACCOUNTMGT_CONFIRM_KEY) %>"
			>
			</aui:input>
			<aui:input 
			name="test7"
			type="checkbox" 
			label="<%=LanguageUtil.get(pageContext, 
						MessageKeys.ACCOUNTMGT_CONFIRM_KEY) %>"
			>
			</aui:input>
			<aui:input 
			name="test8"
			type="checkbox" 
			label="<%=LanguageUtil.get(pageContext, 
						MessageKeys.ACCOUNTMGT_CONFIRM_KEY) %>"
			>
			</aui:input>
	</aui:row>
	
	<aui:row>
		<aui:input name="<%=BusinessDisplayTerms.BUSINESS_ADDRESS %>">
			<aui:validator name="required" />
			<aui:validator name="maxLength">500</aui:validator>
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
			<aui:input name="<%=BusinessDisplayTerms.BUSINESS_EMAIL %>">
				<aui:validator name="required" />
				<aui:validator name="maxLength">255</aui:validator>
			</aui:input>
		</aui:col>
		
		<aui:col width="50">
			<aui:input name="<%=BusinessDisplayTerms.BUSINESS_TELNO %>">
				<aui:validator name="required" />
				<aui:validator name="maxLength">20</aui:validator>
			</aui:input>
		</aui:col>
	</aui:row>
	
	<aui:row>
		<aui:col width="50">
			<aui:input name="<%=BusinessDisplayTerms.BUSINESS_REPRESENTATIVENAME %>">
				<aui:validator name="required" />
				<aui:validator name="maxLength">255</aui:validator>
			</aui:input>
		</aui:col> 
		
		<aui:col width="50">
			<aui:input name="<%=BusinessDisplayTerms.BUSINESS_REPRESENTATIVEROLE %>">
				<aui:validator name="required" />
				<aui:validator name="maxLength">100</aui:validator>
			</aui:input>
		</aui:col>
	</aui:row>
	
	<aui:row>
			<aui:input type="file" name="<%=BusinessDisplayTerms.BUSINESS_ATTACHFILE %>" />
	</aui:row>
	
	<aui:row>
		<aui:input 
			name="businessConfirm"
			type="checkbox" 
			label="<%=LanguageUtil.get(pageContext, 
						MessageKeys.ACCOUNTMGT_CONFIRM_KEY) %>"
			checked="false"
		>
		
		</aui:input>
	</aui:row>
	<aui:row>
		<aui:button name="businessSubmit" type="submit"  disabled="true" />
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