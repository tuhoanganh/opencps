
<%@page import="org.opencps.accountmgt.search.BusinessDisplayTerms"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="org.opencps.util.DLFileEntryUtil"%>
<%@page import="com.liferay.portal.kernel.repository.model.FileEntry"%>
<%@page import="com.liferay.portlet.documentlibrary.util.DLUtil"%>
<%@page import="org.opencps.accountmgt.search.CitizenDisplayTerms"%>
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
<%@page
	import="org.opencps.accountmgt.service.BusinessDomainLocalServiceUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="org.opencps.accountmgt.model.BusinessDomain"%>
<%@page import="org.opencps.util.PortletUtil"%>
<%@page import="org.opencps.datamgt.service.DictItemLocalServiceUtil"%>
<%@page import="org.opencps.datamgt.model.DictItem"%>
<%@page import="com.liferay.portlet.documentlibrary.model.DLFileEntry"%>
<%@page
	import="com.liferay.portlet.documentlibrary.service.DLFileEntryLocalServiceUtil"%>
<%@page import="org.opencps.util.DateTimeUtil"%>
<%@page import="org.opencps.util.PortletPropsValues"%>
<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@ include file="/init.jsp"%>
<%
	DictItem dictItemCity = null;
	DictItem dictItemDistrict = null;
	DictItem dictItemWard = null;
	
	List<BusinessDomain> businessDomains = new ArrayList<BusinessDomain>();
	
	String url = StringPool.BLANK;
	String cityName = StringPool.BLANK;
	String districtName = StringPool.BLANK;
	String wardName = StringPool.BLANK;
	
	PortletURL editProFile = renderResponse.createRenderURL();
	editProFile.setParameter("mvcPath", templatePath + "viewprofile.jsp");
	if(citizen!=null) {
		try {
			long fileEntryId = citizen.getAttachFile();
			if(fileEntryId > 0) {
				FileEntry fileEntry = DLFileEntryUtil.getFileEntry(fileEntryId);
				url = DLUtil.getPreviewURL(fileEntry,
					fileEntry.getFileVersion(), themeDisplay,
					StringPool.BLANK);
			}
		} catch(Exception e){}
		editProFile.setParameter(CitizenDisplayTerms.CITIZEN_ID, String.valueOf(citizen.getCitizenId()));
		dictItemCity = PortletUtil.getDictItem("ADMINISTRATIVE_REGION", citizen.getCityCode(), scopeGroupId);
		dictItemDistrict = PortletUtil.getDictItem("ADMINISTRATIVE_REGION", citizen.getDistrictCode(), scopeGroupId);
		dictItemWard = PortletUtil.getDictItem("ADMINISTRATIVE_REGION", citizen.getWardCode(), scopeGroupId);
		
		cityName = dictItemCity.getItemName(themeDisplay.getLocale(), true);
		districtName = dictItemDistrict.getItemName(themeDisplay.getLocale(), true);
		wardName = dictItemWard.getItemName(themeDisplay.getLocale(), true);
	} else if (business!=null) {
		long fileEntryId = business.getAttachFile();
		if(fileEntryId > 0) {
			FileEntry fileEntry = DLFileEntryUtil.getFileEntry(fileEntryId);
			url = DLUtil.getPreviewURL(fileEntry,
				fileEntry.getFileVersion(), themeDisplay,
				StringPool.BLANK);
		}
		
		try {
			businessDomains = BusinessDomainLocalServiceUtil
							.getBusinessDomains(business.getBusinessId());
		} catch(Exception e) {
			_log.error(e);
		}
		
		editProFile.setParameter(BusinessDisplayTerms.BUSINESS_BUSINESSID, String.valueOf(business.getBusinessId()));
		dictItemCity = PortletUtil.getDictItem("ADMINISTRATIVE_REGION", business.getCityCode(), scopeGroupId);
		dictItemDistrict = PortletUtil.getDictItem("ADMINISTRATIVE_REGION", business.getDistrictCode(), scopeGroupId);
		dictItemWard = PortletUtil.getDictItem("ADMINISTRATIVE_REGION", business.getWardCode(), scopeGroupId);
		
		cityName = dictItemCity.getItemName(themeDisplay.getLocale(), true);
		districtName = dictItemDistrict.getItemName(themeDisplay.getLocale(), true);
		wardName = dictItemWard.getItemName(themeDisplay.getLocale(), true);
	
	}
%>
<c:choose>
	<c:when test="<%=themeDisplay.isSignedIn() %>">
		<c:choose>
			<c:when test="<%=citizen != null || business != null%>">
				<div class="opencps-accountinfo-wrapper">
					<div class="header">
						<p>
							<liferay-ui:message key="account_info" />
						</p>
					</div>
					<div class="content">
						<div class="content-part left">
							<c:choose>
								<c:when test="<%=citizen != null%>">
									<aui:row>
										<p>
											<liferay-ui:message key="citizen-account_info"/>
										</p>
									</aui:row>
									<aui:row>
										<aui:col width="30">
											<label><liferay-ui:message key="citizen-name" /></label>
										</aui:col>
										<aui:col width="50">
											<div><%=citizen.getFullName()%></div>
										</aui:col>
										<aui:col width="20">
											<a href="<%=editProFile.toString() %>" class="fix"><liferay-ui:message key="edit"/></a>
										</aui:col>
									</aui:row>
									
									<aui:row>
										<aui:col width="30">
											<label><liferay-ui:message key="pass-words" /></label>
										</aui:col>
										<aui:col width="50">
											<div>*******</div>
										</aui:col>
										<aui:col width="20">
											<a href="<%=editProFile.toString() %>" class="fix"><liferay-ui:message key="edit"/></a>
										</aui:col>
									</aui:row>
									
									<aui:row>
										<aui:col width="30">
											<label><liferay-ui:message key="birth-date" /></label>
										</aui:col>
										<aui:col width="50">
											<div><%=Validator.isNotNull(citizen.getBirthdate()) ? DateTimeUtil.convertDateToString(citizen.getBirthdate()
												, DateTimeUtil._VN_DATE_FORMAT) : StringPool.BLANK  %></div>
										</aui:col>
										<aui:col width="20">
											<a href="<%=editProFile.toString() %>" class="fix"><liferay-ui:message key="edit"/></a>
										</aui:col>
									</aui:row>
									
									<aui:row>
										<aui:col width="30">
											<label><liferay-ui:message key="telNo" /></label>
										</aui:col>
										<aui:col width="50">
											<div><%=citizen.getTelNo() %></div>
										</aui:col>
										<aui:col width="20">
											<a href="<%=editProFile.toString() %>" class="fix"><liferay-ui:message key="edit"/></a>
										</aui:col>
									</aui:row>
									
									<aui:row>
										<aui:col width="30">
											<label><liferay-ui:message key="personal-id" /></label>
										</aui:col>
										<aui:col width="50">
											<div><%=citizen.getPersonalId() %></div>
										</aui:col>
										<aui:col width="20">
											<a href="<%=editProFile.toString() %>" class="fix"><liferay-ui:message key="edit"/></a>
										</aui:col>
									</aui:row>
									
									<aui:row>
										<aui:col width="30">
											<label><liferay-ui:message key="file-attach-url" /></label>
										</aui:col>
										<aui:col width="50">
											<div><a href="<%=url %>"></a></div>
										</aui:col>
										<aui:col width="20">
											<a href="<%=editProFile.toString() %>" class="fix"><liferay-ui:message key="edit"/></a>
										</aui:col>
									</aui:row>
								</c:when>
								<c:otherwise>
									<aui:row>
										<aui:col width="30">
											<label><liferay-ui:message key="business-name" /></label>
										</aui:col>
										<aui:col width="50">
											<div><%=business.getName() %></div>
										</aui:col>
										<aui:col width="20">
											<a href="<%=editProFile.toString() %>" class="fix"><liferay-ui:message key="edit"/></a>
										</aui:col>
									</aui:row>
									
									<aui:row>
										<aui:col width="30">
											<label><liferay-ui:message key="business-id" /></label>
										</aui:col>
										<aui:col width="50">
											<div><%=business.getBusinessId() %></div>
										</aui:col>
										<aui:col width="20">
											<a href="<%=editProFile.toString() %>" class="fix"><liferay-ui:message key="edit"/></a>
										</aui:col>
									</aui:row>
									
									<aui:row>
										<aui:col width="30">
											<label><liferay-ui:message key="business-shortname" /></label>
										</aui:col>
										<aui:col width="50">
											<div><%= business.getShortName() %></div>
										</aui:col>
										<aui:col width="20">
											<a href="<%=editProFile.toString() %>" class="fix"><liferay-ui:message key="edit"/></a>
										</aui:col>
									</aui:row>
									
									<aui:row>
										<aui:col width="30">
											<label><liferay-ui:message key="business-type" /></label>
										</aui:col>
										<aui:col width="50">
											<div><%=business.getBusinessType() %></div>
										</aui:col>
										<aui:col width="20">
											<a href="<%=editProFile.toString() %>" class="fix"><liferay-ui:message key="edit"/></a>
										</aui:col>
									</aui:row>
									
									<aui:row>
										<div>
				                            <p>
				                                <span class="fix_topleft"><liferay-ui:message key="business-domain"/></span>
				                                	<label class="box_scroll">
				                                <%
				                                	if(businessDomains.isEmpty()) {
				                                		//nothing to do
				                                	} else {
				                                		%>
							                                    <%
							                                    	for(BusinessDomain businessDomain : businessDomains) {
							                                    		%>
							                                    			<span><%=PortletUtil.getDictItem(PortletPropsValues.DATAMGT_MASTERDATA_BUSINESS_DOMAIN, businessDomain.getBusinessDomainId(), scopeGroupId) %></span>
							                                    		<%
							                                    	}
							                                    %>
							                                </label>
				                                			
				                                		<%
				                                	}
				                                %>
				                            </p>
				                            <a href="<%=editProFile.toString() %>" class="fix"><liferay-ui:message key="edit"/></a>
				                        </div>
				                     </aui:row>
				                     
				                     <aui:row>
										<aui:col width="30">
											<label><liferay-ui:message key="file-attach-url" /></label>
										</aui:col>
										<aui:col width="50">
											<div><a href="<%=url %>"></a></div>
										</aui:col>
										<aui:col width="20">
											<a href="<%=editProFile.toString() %>" class="fix"><liferay-ui:message key="edit"/></a>
										</aui:col>
									</aui:row>
								</c:otherwise>
							</c:choose>
						</div>
		
						<div class="content-part right">
							<c:choose>
								<c:when test="<%=Validator.isNotNull(citizen) %>">
									<aui:row>
										<p>
											<liferay-ui:message key="citizen-address"/>
										</p>
									</aui:row>
									
									<aui:row>
										<aui:col width="30">
											<label><liferay-ui:message key="address" /></label>
										</aui:col>
										<aui:col width="50">
											<div><a href="<%=citizen.getAddress() %>"></a></div>
										</aui:col>
										<aui:col width="20">
											<a href="<%=editProFile.toString() %>" class="fix"><liferay-ui:message key="edit"/></a>
										</aui:col>
									</aui:row>
									
									<aui:row>
										<aui:col width="30">
											<label><liferay-ui:message key="city-name" /></label>
										</aui:col>
										<aui:col width="50">
											<div><a href="<%=cityName %>"></a></div>
										</aui:col>
										<aui:col width="20">
											<a href="<%=editProFile.toString() %>" class="fix"><liferay-ui:message key="edit"/></a>
										</aui:col>
									</aui:row>
									
									<aui:row>
										<aui:col width="30">
											<label><liferay-ui:message key="district-name" /></label>
										</aui:col>
										<aui:col width="50">
											<div><a href="<%=districtName %>"></a></div>
										</aui:col>
										<aui:col width="20">
											<a href="<%=editProFile.toString() %>" class="fix"><liferay-ui:message key="edit"/></a>
										</aui:col>
									</aui:row>
									
									<aui:row>
										<aui:col width="30">
											<label><liferay-ui:message key="ward-name" /></label>
										</aui:col>
										<aui:col width="50">
											<div><a href="<%=wardName %>"></a></div>
										</aui:col>
										<aui:col width="20">
											<a href="<%=editProFile.toString() %>" class="fix"><liferay-ui:message key="edit"/></a>
										</aui:col>
									</aui:row>
								</c:when>
								<c:otherwise>
									<aui:row>
										<aui:col width="30">
											<label><liferay-ui:message key="address" /></label>
										</aui:col>
										<aui:col width="50">
											<div><a href="<%=business.getAddress() %>"></a></div>
										</aui:col>
										<aui:col width="20">
											<a href="<%=editProFile.toString() %>" class="fix"><liferay-ui:message key="edit"/></a>
										</aui:col>
									</aui:row>
									
									<aui:row>
										<aui:col width="30">
											<label><liferay-ui:message key="city-name" /></label>
										</aui:col>
										<aui:col width="50">
											<div><a href="<%=cityName %>"></a></div>
										</aui:col>
										<aui:col width="20">
											<a href="<%=editProFile.toString() %>" class="fix"><liferay-ui:message key="edit"/></a>
										</aui:col>
									</aui:row>
									
									<aui:row>
										<aui:col width="30">
											<label><liferay-ui:message key="district-name" /></label>
										</aui:col>
										<aui:col width="50">
											<div><a href="<%=districtName %>"></a></div>
										</aui:col>
										<aui:col width="20">
											<a href="<%=editProFile.toString() %>" class="fix"><liferay-ui:message key="edit"/></a>
										</aui:col>
									</aui:row>
									
									<aui:row>
										<aui:col width="30">
											<label><liferay-ui:message key="ward-name" /></label>
										</aui:col>
										<aui:col width="50">
											<div><a href="<%=wardName %>"></a></div>
										</aui:col>
										<aui:col width="20">
											<a href="<%=editProFile.toString() %>" class="fix"><liferay-ui:message key="edit"/></a>
										</aui:col>
									</aui:row>
									
									<aui:row>
										<aui:col width="30">
											<label><liferay-ui:message key="ward-name" /></label>
										</aui:col>
										<aui:col width="50">
											<div><a href="<%=wardName %>"></a></div>
										</aui:col>
										<aui:col width="20">
											<a href="<%=editProFile.toString() %>" class="fix"><liferay-ui:message key="edit"/></a>
										</aui:col>
									</aui:row>
									
									<aui:row>
										<aui:col width="30">
											<label><liferay-ui:message key="email" /></label>
										</aui:col>
										<aui:col width="50">
											<div><a href="<%=business.getEmail() %>"></a></div>
										</aui:col>
										<aui:col width="20">
											<a href="<%=editProFile.toString() %>" class="fix"><liferay-ui:message key="edit"/></a>
										</aui:col>
									</aui:row>
									
									<aui:row>
										<aui:col width="30">
											<label><liferay-ui:message key="telNo" /></label>
										</aui:col>
										<aui:col width="50">
											<div><a href="<%=business.getTelNo() %>"></a></div>
										</aui:col>
										<aui:col width="20">
											<a href="<%=editProFile.toString() %>" class="fix"><liferay-ui:message key="edit"/></a>
										</aui:col>
									</aui:row>
									
									<aui:row>
										<aui:col width="30">
											<label><liferay-ui:message key="representative-Name" /></label>
										</aui:col>
										<aui:col width="50">
											<div><a href="<%=business.getRepresentativeName() %>"></a></div>
										</aui:col>
										<aui:col width="20">
											<a href="<%=editProFile.toString() %>" class="fix"><liferay-ui:message key="edit"/></a>
										</aui:col>
									</aui:row>
									
									<aui:row>
										<aui:col width="30">
											<label><liferay-ui:message key="representative-Role" /></label>
										</aui:col>
										<aui:col width="50">
											<div><a href="<%=business.getRepresentativeRole() %>"></a></div>
										</aui:col>
										<aui:col width="20">
											
											<a href="<%=editProFile.toString() %>" class="fix"><liferay-ui:message key="edit"/></a>
										</aui:col>
									</aui:row>
								</c:otherwise>
							</c:choose>
						</div>
					</div>
				</div>
			</c:when>
			<c:otherwise>
				<div class="portlet-msg-alert">
					<liferay-ui:message key="you-have-not-a-citizen-or-business-role" />
				</div>
			</c:otherwise>
		</c:choose>
	</c:when>
	<c:otherwise>
		<div class="portlet-msg-alert"><liferay-ui:message key="please-sign-in-to-view-profile"/></div>
	</c:otherwise>
</c:choose>

<%!private Log _log =
		LogFactoryUtil.getLog(".html.portlets.accountmgt.citizenprofile.jsp");%>