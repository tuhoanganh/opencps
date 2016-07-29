
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
<%@page import="org.opencps.accountmgt.service.BusinessDomainLocalServiceUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="org.opencps.accountmgt.model.BusinessDomain"%>
<%@page import="org.opencps.util.PortletUtil"%>
<%@page import="org.opencps.datamgt.service.DictItemLocalServiceUtil"%>
<%@page import="org.opencps.datamgt.model.DictItem"%>
<%@page import="com.liferay.portlet.documentlibrary.model.DLFileEntry"%>
<%@page import="com.liferay.portlet.documentlibrary.service.DLFileEntryLocalServiceUtil"%>
<%@page import="org.opencps.util.DateTimeUtil"%>
<%@page import="org.opencps.util.PortletPropsValues"%>
<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@ include file="/init.jsp" %>
<%
	String email = StringPool.BLANK;
	String passWords = StringPool.BLANK;
	String birthDate = StringPool.BLANK;
	String phoneNum = StringPool.BLANK;
	String personId = StringPool.BLANK;
	String urlShowFile = StringPool.BLANK;
	String address = StringPool.BLANK;
	String cityName = StringPool.BLANK;
	String districtName = StringPool.BLANK;
	String wardName = StringPool.BLANK;

	String name = StringPool.BLANK;
	String enName = StringPool.BLANK;
	String shotName = StringPool.BLANK;
	String businessType = StringPool.BLANK;
	String representativeName = StringPool.BLANK;
	String representativeRole = StringPool.BLANK;
	String businessID = StringPool.BLANK;
	
	List<BusinessDomain> businessDomains = new ArrayList<BusinessDomain>();
	DLFileEntry dlFileEntry = null;
	DictItem dictItemCity = null;
	DictItem dictItemDistrict = null;
	DictItem dictItemWard = null;
	
	if(Validator.isNotNull(accountType) && accountType.equals(PortletPropsValues.USERMGT_USERGROUP_NAME_CITIZEN)) {
		if(Validator.isNotNull(citizen)) {
			try {
				dlFileEntry = DLFileEntryLocalServiceUtil.getDLFileEntry(citizen.getAttachFile());
				urlShowFile = themeDisplay.getPortalURL()+"/c/document_library/get_file?uuid="+dlFileEntry.getUuid()+"&groupId="+themeDisplay.getScopeGroupId() ;
			} catch (Exception e) {
				_log.error(e);
			}
			
			name = citizen.getFullName();
			email = citizen.getEmail();
			passWords = user.getPassword();
			birthDate = DateTimeUtil.convertDateToString(citizen.getBirthdate()
				, DateTimeUtil._VN_DATE_FORMAT);
			phoneNum = citizen.getTelNo();
			personId = citizen.getPersonalId();
			address = citizen.getAddress();
			
			dictItemCity = PortletUtil.getDictItem("ADMINISTRATIVE_REGION", citizen.getCityCode(), scopeGroupId);
			dictItemDistrict = PortletUtil.getDictItem("ADMINISTRATIVE_REGION", citizen.getDistrictCode(), scopeGroupId);
			dictItemWard = PortletUtil.getDictItem("ADMINISTRATIVE_REGION", citizen.getWardCode(), scopeGroupId);
			
			cityName = dictItemCity.getItemName(themeDisplay.getLocale(), true);
			districtName = dictItemDistrict.getItemName(themeDisplay.getLocale(), true);
			wardName = dictItemWard.getItemName(themeDisplay.getLocale(), true);
			
		
		}
	} else if(Validator.isNotNull(accountType) && accountType.equals(PortletPropsValues.USERMGT_USERGROUP_NAME_BUSINESS)) {
		
		if(Validator.isNotNull(business)) {
			try {
				dlFileEntry = DLFileEntryLocalServiceUtil.getDLFileEntry(business.getAttachFile());
				urlShowFile = themeDisplay.getPortalURL()+"/c/document_library/get_file?uuid="+dlFileEntry.getUuid()+"&groupId="+themeDisplay.getScopeGroupId() ;
			} catch (Exception e) {
				_log.error(e);
			}
			
			try {
				businessDomains = BusinessDomainLocalServiceUtil
								.getBusinessDomains(business.getBusinessId());
			} catch(Exception e) {
				_log.error(e);
			}
			
			email = business.getEmail();
			passWords = user.getPassword();
			phoneNum = business.getTelNo();
			address = business.getAddress();
			representativeName = business.getRepresentativeName();
			representativeRole = business.getRepresentativeRole();
			businessID = business.getIdNumber();
			dictItemCity = PortletUtil.getDictItem("ADMINISTRATIVE_REGION", business.getCityCode(), scopeGroupId);
			dictItemDistrict = PortletUtil.getDictItem("ADMINISTRATIVE_REGION", business.getDistrictCode(), scopeGroupId);
			dictItemWard = PortletUtil.getDictItem("ADMINISTRATIVE_REGION", business.getWardCode(), scopeGroupId);
			
			cityName = dictItemCity.getItemName(themeDisplay.getLocale(), true);
			districtName = dictItemDistrict.getItemName(themeDisplay.getLocale(), true);
			wardName = dictItemWard.getItemName(themeDisplay.getLocale(), true);
		}
	}

%>
<c:choose>
	<c:when test="<%=themeDisplay.isSignedIn() %>">
		<c:choose>
			<c:when test="<%=accountType.equals(PortletPropsValues.USERMGT_USERGROUP_NAME_CITIZEN) %>">
				<div class="container">
		            <div class="account_info">
		                <div class="head"><p><liferay-ui:message key="account_info"/></p></div>
		                <div class="content">
		                    <div class="left">
		                        <div>
		                            <p><liferay-ui:message key="citizen-account_info"/></p>
		                        </div>
		                        <div>
		                            <p><span><liferay-ui:message key="citizen-name"/></span><label><%=name %></label></p>
		                            <a href="" class="fix"><liferay-ui:message key="edit"/></a>
		                        </div>
		                        <div>
		                            <p><span><liferay-ui:message key="email"/></span> <label><%= email %></label></p>
		                            <!-- <button>lưu thay đổi</button> -->
		                            <a href="" class="fixing"><i class="fa fa-times" aria-hidden="true"></i></a>
		                        </div>
		                        <%-- <div>
		                            <p><span><liferay-ui:message key="pass-words"/></span> <label>********</label></p>
		                            <a href="" class="fix"><liferay-ui:message key="edit"/></a>
		                        </div> --%>
		                        <div>
		                            <p><span><liferay-ui:message key="birth-date"/></span> <label><%=birthDate %></label></p>
		                            <a href="" class="fix"><liferay-ui:message key="edit"/></a>
		                        </div>
		                        <div>
		                            <p><span><liferay-ui:message key="telNo"/></span> <label><%=phoneNum %></label></p>
		                            <a href="" class="fix"><liferay-ui:message key="edit"/></a>
		                        </div>
		                        <div>
		                            <p><span><liferay-ui:message key="personal-id"/></span> <label><%=personId %></label></p>
		                            <a href="" class="fix"><liferay-ui:message key="edit"/></a>
		                        </div>
		                        <div>
		                            <p><span><liferay-ui:message key="file-attach-url"/></span> <label><a href="<%=urlShowFile%>"><liferay-ui:message key="click-to-view-file"/></a></label></p>
		                            <a href="" class="fix"><liferay-ui:message key="edit"/></a>
		                        </div>
		                    </div>
		                    <div class="right">
		                        <div>
		                            <p><liferay-ui:message key="citizen-address"/></p>
		                        </div>
		                        <div>
		                            <p><span><liferay-ui:message key="address"/></span> <label><%=address %></label></p>
		                            <a href="" class="fix"><liferay-ui:message key="edit"/></a>
		                        </div>
		                        <div>
		                            <p><span><liferay-ui:message key="city-name"/></span> <label><%=cityName %></label></p>
		                            <a href="" class="fix"><liferay-ui:message key="edit"/></a>
		                        </div>
		                        <div>
		                            <p><span><liferay-ui:message key="district-name"/></span> <label><%=districtName %></label></p>
		                            <a href="" class="add"><liferay-ui:message key="add"/></a>
		                        </div>
		                        <div>
		                            <p><span><liferay-ui:message key="ward-name"/></span> <label><%=wardName %></label></p>
		                            <a href="" class="add"><liferay-ui:message key="add"/></a>
		                        </div>
		                        
		                    </div>
		                </div>
		            </div>
		        </div>
			</c:when>
			<c:when test="<%=accountType.equals(PortletPropsValues.USERMGT_USERGROUP_NAME_BUSINESS) %>">
				<div class="container">
		            <div class="account_info account_company">
		                <div class="head"><p><liferay-ui:message key="account_info"/></p></div>
		                <div class="content">
		                    <div class="left">
		                        <div>
		                            <p><span><liferay-ui:message key="business-name"/></span> <label><%=name %></label></p>
		                            <a href="" class="fix"><liferay-ui:message key="edit"/></a>
		                        </div>
		                        <div>
		                            <p><span><liferay-ui:message key="business-id"/></span> <label><%=businessID %></label></p>
		                            <a href="" class="fix"><liferay-ui:message key="edit"/></a>
		                        </div>
		                        <div>
		                            <p><span><liferay-ui:message key="business-enname"/></span> <label><%=enName %></label></p>
		                            <a href="" class="fix"><liferay-ui:message key="edit"/></a>
		                        </div>
		                        <div>
		                            <p><span><liferay-ui:message key="business-shortname"/></span> <label><%=shotName %></label></p>
		                            <a href="" class="fix"><liferay-ui:message key="edit"/></a>
		                        </div>
		                        <div>
		                            <p><span><liferay-ui:message key="business-type"/></span> <label><%=businessType %></label></p>
		                            <a href="" class="fix"><liferay-ui:message key="edit"/></a>
		                        </div>
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
		                            <a href="" class="fix"><liferay-ui:message key="edit"/></a>
		                        </div>
		                        <div>
		                            <p><span><liferay-ui:message key="file-attach-url"/></span> <label><a href="<%=urlShowFile%>"><liferay-ui:message key="click-to-view-file"/></a></label></p>
		                            <a href="" class="fix"><liferay-ui:message key="edit"/></a>
		                        </div>
		                    </div>
		                    <div class="right">
		                        <div>
		                            <p><span><liferay-ui:message key="address"/></span> <label><%=address %></label></p>
		                            <a href="" class="fix"><liferay-ui:message key="edit"/></a>
		                        </div>
		                        <div>
		                            <p><span><liferay-ui:message key="city-name"/></span> <label><%=cityName %></label></p>
		                            <a href="" class="fix"><liferay-ui:message key="edit"/></a>
		                        </div>
		                        <div>
		                            <p><span><liferay-ui:message key="district-name"/></span> <label><%=districtName %></label></p>
		                            <a href="" class="add"><liferay-ui:message key="add"/></a>
		                        </div>
		                        <div>
		                            <p><span><liferay-ui:message key="ward-name"/></span> <label><%=wardName %></label></p>
		                            <a href="" class="add"><liferay-ui:message key="add"/></a>
		                        </div>
		                        <div>
		                            <p><span><liferay-ui:message key="email"/></span> <label><%=email %></label></p>
		                            <a href="" class="fix"><liferay-ui:message key="edit"/></a>
		                        </div>
		                        <div>
		                            <p><span><liferay-ui:message key="tellNo"/></span> <label><%=phoneNum %></label></p>
		                            <a href="" class="fix"><liferay-ui:message key="edit"/></a>
		                        </div>
		                        <div>
		                            <p><span><liferay-ui:message key="representative-Name"/></span> <label><%=representativeName %></label></p>
		                            <a href="" class="fix"><liferay-ui:message key="edit"/></a>
		                        </div>
		                        <div>
		                            <p><span><liferay-ui:message key="representative-Role"/></span> <label><%=representativeRole %></label></p>
		                            <a href="" class="fix"><liferay-ui:message key="edit"/></a>
		                        </div>
		                    </div>
		                </div>
           			 </div>
        		</div>
			</c:when>
			<c:otherwise>
				<div class="portlet-msg-alert"><liferay-ui:message key="you-have-not-a-citizen-or-business-role"/></div>
			</c:otherwise>
		</c:choose>
	</c:when>
	<c:otherwise>
		<div class="portlet-msg-alert"><liferay-ui:message key="please-sign-in-to-view-profile"/></div>
	</c:otherwise>
</c:choose>

<%!
	private Log _log = LogFactoryUtil.getLog(".html.portlets.accountmgt.citizenprofile.jsp");
%>