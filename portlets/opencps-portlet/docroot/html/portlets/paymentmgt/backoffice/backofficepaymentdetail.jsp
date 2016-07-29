<%@page import="java.text.DecimalFormat"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="com.liferay.portlet.documentlibrary.util.DLUtil"%>
<%@page import="com.liferay.portal.kernel.repository.model.FileVersion"%>
<%@page import="com.liferay.portlet.documentlibrary.NoSuchFileEntryException"%>
<%@page import="com.liferay.portlet.documentlibrary.service.DLAppServiceUtil"%>
<%@page import="com.liferay.portal.kernel.repository.model.FileEntry"%>
<%@page import="com.liferay.util.PwdGenerator"%>
<%@page import="org.opencps.usermgt.service.WorkingUnitLocalServiceUtil"%>
<%@page import="org.opencps.usermgt.model.WorkingUnit"%>
<%@page import="com.liferay.portal.kernel.util.FastDateFormatFactoryUtil"%>
<%@page import="java.text.Format"%>
<%@page import="org.opencps.util.PortletUtil"%>
<%@page import="org.opencps.dossiermgt.service.DossierLocalServiceUtil"%>
<%@page import="com.liferay.portal.kernel.util.HtmlUtil"%>
<%@page import="org.opencps.paymentmgt.model.impl.PaymentFileImpl"%>
<%@page import="org.opencps.paymentmgt.service.PaymentFileLocalServiceUtil"%>
<%@page import="org.opencps.paymentmgt.model.PaymentFile"%>
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
    Format dateFormatDate = FastDateFormatFactoryUtil.getDate(locale, timeZone);
    String backRedirect = ParamUtil.getString(request, "redirect");
    long paymentFileId = ParamUtil.getLong(request, "paymentFileId");
    PaymentFile paymentFile = PaymentFileLocalServiceUtil.fetchPaymentFile(paymentFileId);
    String soHoSo = StringPool.BLANK;
    String coQuanQuanLyHoaDon = StringPool.BLANK;
    Dossier dossier = null;
    ServiceInfo serviceInfo = null;
    if(Validator.isNull(paymentFile)){
        paymentFile = new PaymentFileImpl();
    }else{
        soHoSo = DossierLocalServiceUtil.fetchDossier(paymentFile.getDossierId()).getReceptionNo();
        try {
        	 WorkingUnit wunit = WorkingUnitLocalServiceUtil.fetchByMappingOrganisationId(themeDisplay.getScopeGroupId(), paymentFile.getGovAgencyOrganizationId());
             if (wunit != null)
                 coQuanQuanLyHoaDon = wunit.getName();
        } catch (Exception e) {

        }
        dossier = DossierLocalServiceUtil.fetchDossier(paymentFile.getDossierId());
        serviceInfo = ServiceInfoLocalServiceUtil.fetchServiceInfo(dossier.getServiceInfoId());
    }
    DecimalFormat df2 = new DecimalFormat( "#,###,###,##0.00" );
%>
<liferay-ui:header
    backURL="<%= backRedirect %>"
    title="payment-detail"
    backLabel="back"
/>
<div class="payment-ld">
<div class="content">
                <div class="box50">
                    <div>
                        <p><span><liferay-ui:message key="so-ho-so"/>:</span> <%=HtmlUtil.escape(soHoSo) %></p>
                    </div>
                    <div>
                        <p><span><liferay-ui:message key="thu-tuc-hanh-chinh"/>:</span> <span><%=HtmlUtil.escape(serviceInfo.getServiceName()) %></span></p>
                    </div>
                    <div>
                        <p><span><liferay-ui:message key="co-quan-thuc-hien"/>:</span> <%=HtmlUtil.escape(coQuanQuanLyHoaDon) %></p>
                    </div>
                    <div>
                        <p><span><liferay-ui:message key="ten-phi-thanh-toan"/>:</span> <%=HtmlUtil.escape(paymentFile.getPaymentName()) %></p>
                    </div>
                    <div>
                        <p><span><liferay-ui:message key="ngay-yeu-cau"/>:</span> <%=Validator.isNotNull(paymentFile.getRequestDatetime())?HtmlUtil.escape(dateFormatDate.format(paymentFile.getRequestDatetime())):StringPool.BLANK %></p>
                    </div>
                    <div>
                        <p><span><liferay-ui:message key="so-tien"/>:</span> <span class="red"><%=HtmlUtil.escape(df2.format(Double.valueOf(paymentFile.getAmount())).toString()) %> <liferay-ui:message key="vnd"/></span></p>
                    </div>
                    <div>
                        <p><span><liferay-ui:message key="tinh-trang-thanh-toan"/>:</span> <%=LanguageUtil.get(pageContext, PortletUtil.getPaymentStatusLabel(paymentFile.getPaymentStatus(), locale)) %></p>
                    </div>                
                </div>
                <div class="box50">
                    <div>
                        <p><span><liferay-ui:message key="ngay-da-bao-nop"/>:</span> <%=Validator.isNotNull(paymentFile.getConfirmDatetime())?HtmlUtil.escape(dateFormatDate.format(paymentFile.getConfirmDatetime())):StringPool.BLANK %></p>
                    </div>
                    <div>
                        <p><span><liferay-ui:message key="hinh-thuc-thuc-hien"/>:</span> 
	                        <c:choose>
			                    <c:when test="<%=paymentFile.getPaymentStatus() == 3 %>"><%= LanguageUtil.get(pageContext, PortletUtil.getPaymentMethodLabel(paymentFile.getPaymentMethod(), locale)) %></c:when>
			                    <c:otherwise></c:otherwise>
			                </c:choose>
	                	</p>
                    </div>
                    <div>
                        <p><span><liferay-ui:message key="chung-tu-kem-theo"/>:</span> 
							<%
		                        FileEntry fileEntry = null;
		                        try {
		                            fileEntry = DLAppServiceUtil.getFileEntry(paymentFile.getConfirmFileEntryId());
		                        }
		                        catch (NoSuchFileEntryException e) {
		                            
		                        }
		                        String dlURL = null;
		                        if (fileEntry != null) {
		                            FileVersion fileVersion = fileEntry.getFileVersion();
		                             
		                            String queryString = "";                             
		                            boolean appendFileEntryVersion = true;
		                             
		                            boolean useAbsoluteURL = true;
		                             
		                            dlURL = DLUtil.getPreviewURL(fileEntry, fileVersion, themeDisplay, queryString, appendFileEntryVersion, useAbsoluteURL);                            
		                        }
		                    %>
		                    <c:if test="<%= dlURL != null %>">
		                        <a target="_blank" href="<%= dlURL %>"><liferay-ui:message key="view-confirm-file-entry"></liferay-ui:message></a>
		                    </c:if>
						</p>
                    </div>
                    <div>
                        <p><span><liferay-ui:message key="ghi-chu-kem-theo"/>:</span> <%=HtmlUtil.escape(paymentFile.getRequestNote()) %></p>
                    </div>
                    <div>
                        <p><span><liferay-ui:message key="ngay-xac-nhan-thu-phi"/>:</span> <%=Validator.isNotNull(paymentFile.getApproveDatetime())?HtmlUtil.escape(dateFormatDate.format(paymentFile.getApproveDatetime())):StringPool.BLANK %></p>
                    </div>
                    <div>
                        <p><span><liferay-ui:message key="noi-dung-xac-nhan"/>:</span> <%=HtmlUtil.escape(paymentFile.getApproveNote()) %></p>
                    </div>
                    <div>
                        <p><span><liferay-ui:message key="nguoi-thuc-hien"/>:</span> <%=HtmlUtil.escape(paymentFile.getAccountUserName()) %></p>
                    </div>                
                </div>
            </div>
            </div>
