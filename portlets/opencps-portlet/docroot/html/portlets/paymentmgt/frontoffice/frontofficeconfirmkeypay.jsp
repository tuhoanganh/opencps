
<%@page import="org.opencps.paymentmgt.service.persistence.PaymentFileUtil"%>
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
	HttpServletRequest r = PortalUtil.getHttpServletRequest(renderRequest);

	long test = (long)r.getAttribute("paymentFileId");
	System.out.println("test:"+test);
	
	long paymentFileId = ParamUtil.getLong(r, "paymentFileId",0);
	long dossierId = ParamUtil.getLong(r, "dossierId",0);
	long serviceInfoId = ParamUtil.getLong(r, "serviceInfoId",0);
	
	PaymentFile paymentFile = null;
	Dossier dossier = null;
	ServiceInfo serviceInfo = null;
	
	if(paymentFileId >0){
		paymentFile =PaymentFileLocalServiceUtil.getPaymentFile(paymentFileId);
		
	}
	if(dossierId >0){
		dossier = DossierLocalServiceUtil.getDossier(dossierId);
		
	}
	if(serviceInfoId >0){
		serviceInfo = ServiceInfoLocalServiceUtil.getServiceInfo(serviceInfoId);
		
	}
	

%>

<portlet:renderURL var="backURL">
	<portlet:param 
		name="mvcPath"
		value="/html/portlets/paymentmgt/frontoffice/frontofficepaymentlist.jsp" 
	/>	
</portlet:renderURL>

<liferay-ui:header
	backURL="<%= backURL.toString() %>"
	title="payment-list"
/>

<c:choose>
	<c:when test="<%= Validator.isNotNull(paymentFile) && paymentFile.getPaymentStatus() == PaymentMgtUtil.PAYMENT_STATUS_APPROVED %>">
		<div class="alert alert-success">
			<liferay-ui:message key="keypay-success"></liferay-ui:message>
		</div>
		<c:if test="<%= Validator.isNotNull(dossier) && Validator.isNotNull(serviceInfo) %>">
			<div class="lookup-result">
				<table>
					<tr style="background: #fae5d3;">
						<td class="col-left"><liferay-ui:message key="reception-no"></liferay-ui:message></td>
						<td class="col-right"><%= dossier.getReceptionNo()%></td>
					</tr>
					<tr>
						<td class="col-left"><liferay-ui:message key="service-name"></liferay-ui:message></td>
						<td class="col-right"><%= serviceInfo.getServiceName() %></td>
					</tr>
					<tr>
						<td class="col-left"><liferay-ui:message key="administration-name"></liferay-ui:message></td>
						<td class="col-right"><%= dossier.getGovAgencyName() %></td>
					</tr>
					<tr>
						<td class="col-left"><liferay-ui:message key="payment-name"></liferay-ui:message></td>
						<td class="col-right"><%=paymentFile.getPaymentName()%></td>
					</tr>
					<tr>
						<td class="col-left"><liferay-ui:message key="amount"></liferay-ui:message></td>
						<td class="col-right"><%= NumberFormat.getInstance(new Locale("vi", "VN")).format(paymentFile.getAmount()) %></td>
					</tr>
					<tr>
						<td class="col-left"><liferay-ui:message key="trans_id"></liferay-ui:message></td>
						<td class="col-right"><%=paymentFile.getKeypayTransactionId() %></td>
					</tr>
					<tr>
						<td class="col-left"><liferay-ui:message key="trans_id"></liferay-ui:message></td>
						<td class="col-right"><%=paymentFile.getKeypayGoodCode() %></td>
					</tr>
				</table>	
			</div>	
		</c:if>
	</c:when>
	<c:otherwise>
		<div class="alert alert-error">
			<liferay-ui:error key="<%=paymentFile.getPaymentGateStatusCode()%>"></liferay-ui:error>
		</div>
		
	</c:otherwise>
</c:choose>