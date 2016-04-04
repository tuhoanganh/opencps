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
<%@page import="org.opencps.datamgt.service.DictCollectionLocalServiceUtil"%>
<%@page import="org.opencps.datamgt.model.DictCollection"%>
<%@ include file="../../init.jsp" %>

<%

	Citizen citizen = (Citizen) request.getAttribute(WebKeys.CITIZEN_ENTRY);
	Citizen citizenFromProFile = (Citizen) request
					.getAttribute(WebKeys.CITIZEN_PROFILE_ENTRY);
	DictCollection dictCollection = null;
	long citizenID = citizen != null ? citizen.getCitizenId() : 0L;
	long citizenIdFromProfile = citizenFromProFile != null 
					? citizenFromProFile.getCitizenId() : 0L;
	
	Date defaultBirthDate = citizen != null && citizen.getBirthdate() != null ? 
		citizen.getBirthdate() : DateTimeUtil.convertStringToDate("01/01/1970");
		
	PortletUtil.SplitDate spd = new PortletUtil.SplitDate(defaultBirthDate);
	
	try {
		dictCollection = DictCollectionLocalServiceUtil
						.getDictCollection(scopeGroupId, "ADMINISTRATIVE_REGION");
	} catch(Exception e) {
		
	}
%>

<aui:model-context bean="<%=citizen%>" model="<%=Citizen.class%>" />

<c:choose>
	<c:when test="<%=citizenIdFromProfile!=0 %>">
		<aui:row>
			<aui:col width="50">
				<aui:input name="<%=CitizenDisplayTerms.CITIZEN_FULLNAME %>" disabled="true">
				<aui:validator name="required" />
				<aui:validator name="maxLength">255</aui:validator>
				</aui:input>
			</aui:col>
			
			<aui:col width="50">
				<aui:input name="<%=CitizenDisplayTerms.CITIZEN_PERSONALID %>" disabled="true">
				<aui:validator name="required" />
				</aui:input>
			</aui:col>
		</aui:row>
	</c:when>
	<c:otherwise>
		<aui:row>
			<aui:col width="50">
				<aui:input name="<%=CitizenDisplayTerms.CITIZEN_FULLNAME %>">
				<aui:validator name="required" />
				<aui:validator name="maxLength">255</aui:validator>
				</aui:input>
			</aui:col>
			
			<aui:col width="50">
				<aui:input name="<%=CitizenDisplayTerms.CITIZEN_PERSONALID %>">
				<aui:validator name="required" />
				</aui:input>
			</aui:col>
		</aui:row>
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="<%=citizenIdFromProfile != 0 %>">
			<aui:row>
				<aui:col width="50">
					<label class="control-label custom-lebel" for='<portlet:namespace/><%=CitizenDisplayTerms.CITIZEN_BIRTHDATE %>'>
						<liferay-ui:message key="birth-date"/>
					</label>
					<aui:input name="<%=CitizenDisplayTerms.CITIZEN_BIRTHDATE %>" disabled="true"/>
				</aui:col>
				
				<aui:col width="50">
					<aui:select name="<%=CitizenDisplayTerms.CITIZEN_GENDER %>" disabled="true">
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
	</c:when>
	<c:otherwise>
		<aui:row>
			<aui:col width="50">
				<label class="control-label custom-lebel" for='<portlet:namespace/><%=CitizenDisplayTerms.CITIZEN_BIRTHDATE %>'>
					<liferay-ui:message key="birth-date"/>
				</label>
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
	</c:otherwise>
</c:choose>

<aui:row>
	<aui:col width="100">
		<aui:input 
			name="<%=CitizenDisplayTerms.CITIZEN_ADDRESS %>" 
			cssClass="input100"
		>
			<aui:validator name="required" />
			<aui:validator name="maxLength">255</aui:validator>
		</aui:input>
	</aui:col>
</aui:row>

<aui:row>
	<aui:col width="100">
		<datamgt:ddr 
			cssClass="input100"
			depthLevel="3" 
			dictCollectionCode="ADMINISTRATIVE_REGION"
			itemNames="cityId,districtId,wardId"
			itemsEmptyOption="true,true,true"	
		/>	
	</aui:col>
</aui:row>

<aui:row>
	
	<c:choose>
		<c:when test="<%=citizenIdFromProfile!=0 %>">
			<aui:col width="50">
				<aui:input name= "<%=CitizenDisplayTerms.CITIZEN_EMAIL %>" disabled="true">
					<aui:validator name="required" />
					<aui:validator name="email" />
					<aui:validator name="maxLength">255</aui:validator>	
				</aui:input>
			</aui:col>	
		</c:when>
		<c:otherwise>
			<aui:col width="50">
				<aui:input name="<%=CitizenDisplayTerms.CITIZEN_EMAIL %>">
					<aui:validator name="required" />
					<aui:validator name="email" />
					<aui:validator name="maxLength">255</aui:validator>	
				</aui:input>
			</aui:col>
		</c:otherwise>
	</c:choose>
	
	
	<aui:col width="50">
		<aui:input name="<%=CitizenDisplayTerms.CITIZEN_TELNO %>">
			<aui:validator name="required" />
			<aui:validator name="minLength">10</aui:validator>
		</aui:input>
	</aui:col>
</aui:row>

<c:if test="<%=citizenIdFromProfile == 0 %>">

	<aui:row>
		<aui:col width="100">
		<aui:input type="file" name="<%=CitizenDisplayTerms.CITIZEN_ATTACHFILE %>" />
		</aui:col>
	</aui:row>


</c:if>

