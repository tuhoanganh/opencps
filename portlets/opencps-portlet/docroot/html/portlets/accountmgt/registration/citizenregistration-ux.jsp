<%@page import="org.opencps.util.PortletPropsValues"%>
<%@page import="java.util.Date"%>
<%@page import="org.opencps.util.PortletUtil"%>
<%@page import="org.opencps.util.DateTimeUtil"%>
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

<%@page import="org.opencps.accountmgt.DuplicateCitizenEmailException"%>
<%@page import="org.opencps.accountmgt.OutOfLengthCitizenEmailException"%>
<%@page import="org.opencps.accountmgt.OutOfLengthCitizenAddressException"%>
<%@page import="org.opencps.accountmgt.OutOfLengthCitizenNameException"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="org.opencps.accountmgt.model.Citizen"%>
<%@page import="org.opencps.accountmgt.search.CitizenDisplayTerms"%>
<%@page import="org.opencps.util.MessageKeys"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="org.opencps.accountmgt.FileTypeFailException"%>
<%@page import="org.opencps.accountmgt.OutOfSizeFileUploadException"%>
<%@page import="org.opencps.accountmgt.InvalidFileUploadException"%>
<%@page import="org.opencps.accountmgt.InvalidWardCodeException"%>
<%@page import="org.opencps.accountmgt.InvalidDistricCodeException"%>
<%@page import="org.opencps.accountmgt.InvalidCityCodeException"%>
<%@ include file="../init.jsp" %>

<%
	if(request.getAttribute(WebKeys.CITIZEN_ENTRY) != null){
		citizen = (Citizen) request.getAttribute(WebKeys.CITIZEN_ENTRY);
	}

	long citizenId = citizen != null ? citizen.getCitizenId() : 0L;
	
	Date defaultBirthDate = DateTimeUtil.convertStringToDate("01/01/1970");
 		
 	PortletUtil.SplitDate spd = new PortletUtil.SplitDate(defaultBirthDate);
%>

<liferay-ui:error 
	exception="<%= OutOfLengthCitizenAddressException.class %>" 
	message="<%= OutOfLengthCitizenAddressException.class.getName() %>" 
/>

<liferay-ui:error 
	exception="<%= OutOfLengthCitizenEmailException.class %>" 
	message="<%= OutOfLengthCitizenEmailException.class.getName() %>" 
/>

<liferay-ui:error 
	exception="<%= OutOfLengthCitizenNameException.class %>" 
	message="<%= OutOfLengthCitizenNameException.class.getName() %>" 
/>

<liferay-ui:error 
	exception="<%= DuplicateCitizenEmailException.class %>" 
	message="<%= DuplicateCitizenEmailException.class.getName() %>" 
/>

<liferay-ui:error 
	key="<%=MessageKeys.ACCOUNT_SYSTEM_EXCEPTION_OCCURRED %>" 
	message="system.exception.occured" 
/>

<liferay-ui:error 
	exception="<%= InvalidCityCodeException.class %>" 
	message="<%= InvalidCityCodeException.class.getName() %>" 
/>
<liferay-ui:error 
	exception="<%= InvalidDistricCodeException.class %>" 
	message="<%= InvalidDistricCodeException.class.getName() %>" 
/>
<liferay-ui:error 
	exception="<%= InvalidWardCodeException.class %>" 
	message="<%= InvalidWardCodeException.class.getName() %>" 
/>
<liferay-ui:error 
	exception="<%= InvalidFileUploadException.class %>" 
	message="<%= InvalidFileUploadException.class.getName() %>" 
/>
<liferay-ui:error 
	exception="<%= FileTypeFailException.class %>" 
	message="<%= FileTypeFailException.class.getName() %>" 
/>
<liferay-ui:error 
	exception="<%= OutOfSizeFileUploadException.class %>" 
	message="<%= OutOfSizeFileUploadException.class.getName() %>" 
/>

<portlet:renderURL var="switcherBusinessRegisterURL">
	<portlet:param name="mvcPath" value='<%= templatePath + "businessregistration.jsp-ux" %>'/>
</portlet:renderURL>
<div class="ocps-register">
	<div class="opencps accountmgt fm-registration header">
		<%-- <div class="register-label">
			<liferay-ui:message key="register-citizen"/>
		</div> --%>
		<div>
			<aui:row>
				<aui:col width="30" cssClass="opcs-register-label">
					<label><liferay-ui:message key="register"></liferay-ui:message></label>
				</aui:col>
				<aui:col width="30">
					<aui:row>
						<aui:col width="50">
							<aui:input type="radio" name="typeOfRegister" value="citizen" inlineLabel="right" label="citizen" checked="true"/>
						</aui:col>
						<aui:col width="50">
							<aui:input type="radio" name="typeOfRegister" value="business" inlineLabel="right" label="business"/>
						</aui:col>
					</aui:row>
				</aui:col>
				<aui:col width="30" cssClass="opcs-register-login">
					<a href='<%=themeDisplay.getURLSignIn() %>'><liferay-ui:message key="login" /></a>
				</aui:col>
			</aui:row>
			<%-- <aui:button name="business" value="business" type="button" href="<%=switcherBusinessRegisterURL.toString() %>"/> --%>
		</div>
	</div>
	
	<div class="bottom-horizontal-line"></div>
	
	
	<portlet:actionURL var="updateCitizenURL" name="updateCitizen">
		<portlet:param 
			name="<%=CitizenDisplayTerms.CITIZEN_ID %>" 
			value="<%=String.valueOf(citizenId) %>"
		/>
		<portlet:param name="currentURL" value="<%=currentURL %>"/>
	</portlet:actionURL>
	
	<aui:form 
		action="<%=updateCitizenURL.toString() %>"
		name="fm"
		method="post"
		enctype="multipart/form-data"
		onSubmit='<%= "event.preventDefault(); " + renderResponse.getNamespace() + "registerAccount();" %>'
	>
	
		<aui:input name="citizenRegStep_cfg" value="<%=citizenRegStep_cfg %>" type="hidden"></aui:input>
		
		<%-- <liferay-util:include 
			page="/html/portlets/accountmgt/registration/citizen/general_info-ux.jsp" 
			servletContext="<%=application %>" 
		/> 
	
		<liferay-util:include 
			page="/html/portlets/accountmgt/registration/citizen/contact-ux.jsp" 
			servletContext="<%=application %>" 
		/>  --%>
		
	<div class = "ocps-bound-left-right">
		<div class = "ocps-res-left">
			<aui:row>
				<aui:input 
 					name="<%=CitizenDisplayTerms.CITIZEN_FULLNAME %>" 
 					cssClass="input100"
 					placeholder="<%=CitizenDisplayTerms.CITIZEN_FULLNAME %>"
 				>
	 				<aui:validator name="required" />
	 				<aui:validator name="maxLength">255</aui:validator>
 				</aui:input>
			</aui:row>
			
			<aui:row>
				<aui:input 
					name="<%=CitizenDisplayTerms.CITIZEN_EMAIL %>"
					cssClass="input100"
					placeholder="<%=CitizenDisplayTerms.CITIZEN_EMAIL %>"
				>
					<aui:validator name="required" />
					<aui:validator name="email" />
					<aui:validator name="maxLength">255</aui:validator>	
				</aui:input>
			</aui:row>
			
			<aui:row>
				<aui:input 
 					name="<%=CitizenDisplayTerms.CITIZEN_PERSONALID %>"
 					cssClass="input100"
 					placeholder="<%=CitizenDisplayTerms.CITIZEN_PERSONALID %>"
 				/>
			</aui:row>
			
			<aui:row>
				<aui:input name="<%=CitizenDisplayTerms.CITIZEN_TELNO %>" cssClass="input100" placeholder="<%=CitizenDisplayTerms.CITIZEN_TELNO %>">
					<aui:validator name="minLength">10</aui:validator>
				</aui:input>
			</aui:row>
			
			<aui:row>
				<label class="control-label custom-lebel" for='<portlet:namespace/><%=CitizenDisplayTerms.CITIZEN_BIRTHDATE %>'>
 					<liferay-ui:message key="birth-date"/>
 				</label>
 				<liferay-ui:input-date 
 					
 					dayParam="<%=CitizenDisplayTerms.BIRTH_DATE_DAY %>"
 					dayValue="<%= spd.getDayOfMoth() %>"
 					monthParam="<%=CitizenDisplayTerms.BIRTH_DATE_MONTH %>"
 					monthValue="<%= spd.getMonth() %>"
 					name="<%=CitizenDisplayTerms.CITIZEN_BIRTHDATE %>"
 					yearParam="<%=CitizenDisplayTerms.BIRTH_DATE_YEAR %>"
 					yearValue="<%= spd.getYear() %>"
 					formName="fm"
 					autoFocus="<%=true %>"
 					cssClass="input100 ocps-input-date"
 				>
 				</liferay-ui:input-date>
			</aui:row>
			
			<aui:row cssClass="input100 ocps-file-attach">
				<aui:input 
					type="file" 
					name="<%=CitizenDisplayTerms.CITIZEN_ATTACHFILE %>" 
					>
					<aui:validator name="acceptFiles">
						'<%= StringUtil.merge( PortletPropsValues.ACCOUNTMGT_FILE_TYPE) %>'
					</aui:validator>
				</aui:input>
			</aui:row>
		</div>
		
		<div class = "ocps-res-right">
			<aui:row>
				<aui:select 
 					name="<%=CitizenDisplayTerms.CITIZEN_GENDER %>"
 					cssClass="input100"
 				>
 					<aui:option label="<%=CitizenDisplayTerms.CITIZEN_GENDER %>" value="<%=CitizenDisplayTerms.CITIZEN_GENDER %>" />
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
			</aui:row>
			
			<aui:row>
				<aui:input 
					name="<%=CitizenDisplayTerms.CITIZEN_ADDRESS %>" 
					cssClass="input100"
					placeholder="<%=CitizenDisplayTerms.CITIZEN_ADDRESS %>"
				>
					<aui:validator name="maxLength">255</aui:validator>
				</aui:input>
			</aui:row>
			
			<aui:row>
				<datamgt:ddr 
					cssClass="input100"
					depthLevel="3" 
					dictCollectionCode="ADMINISTRATIVE_REGION"
					itemNames="cityId,districtId,wardId"
					itemsEmptyOption="true,true,true"
					displayStyle="vertical"
				/>	
			</aui:row>
			
			<aui:row>
				<aui:input 
					name="termsOfUse"
					type="checkbox" 
					label="terms-of-use"
					cssClass="ocps-terms-of-use"
				/>
			</aui:row>
		</div>
	</div>
		<aui:row cssClass="ocps-register-button">
			<aui:button name="register" type="submit" value="register" disabled="true" />
			<%-- <aui:button name="back" type="button" value="back" onClick="window.history.back();"/> --%>
		</aui:row>
	</aui:form>
</div>


<aui:script use="liferay-portlet-url">
	AUI().ready(function(A) {
		var termsOfUseCheckbox = A.one('#<portlet:namespace />termsOfUseCheckbox');
		var allRadios = A.all( "input[type='radio']" );
		
		var typeValue =  A.one("input[name=<portlet:namespace/>typeOfRegister]:checked").get("value");
				
		allRadios.on( 'change', function(e) {
			var selectedRadioButtonValue = e.currentTarget.get('value');
			var url =Liferay.PortletURL.createRenderURL(); 
			url.setPortletId("<%= themeDisplay.getPortletDisplay().getId() %>");
			url.setWindowState('normal');
			url.setParameter("mvcPath", "/html/portlets/accountmgt/registration/registration.jsp");
			url.setParameter("type", selectedRadioButtonValue);
			
			window.location.href = url.toString();
		}); 
		
		if(termsOfUseCheckbox) {
			termsOfUseCheckbox.on('click',function() {
				
				var termsOfUse = A.one('#<portlet:namespace />termsOfUse');
				
				var register = A.one('#<portlet:namespace />register');
				
				if(termsOfUse.val() == 'true'){
					register.removeClass('disabled');
					register.removeAttribute('disabled');
				}else{
					register.addClass('disabled');
					register.setAttribute('disabled' , 'true');
				}
			});
		}
		
	});

	Liferay.provide(window, '<portlet:namespace />registerAccount', function() {
		A = AUI();
		var register = A.one('#<portlet:namespace />register');
		var termsOfUse = A.one('#<portlet:namespace />termsOfUse');
		
		if(termsOfUse.val() == 'true'){
			submitForm(document.<portlet:namespace />fm);
		}else{
			return;
		}
	});
	
</aui:script>

