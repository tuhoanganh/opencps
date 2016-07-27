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
<%@page import="org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil"%>
<%@page import="org.opencps.servicemgt.model.ServiceInfo"%>
<%@page import="com.liferay.portlet.documentlibrary.util.DLUtil"%>
<%@page import="org.opencps.util.DLFileEntryUtil"%>
<%@page import="com.liferay.portal.kernel.repository.model.FileEntry"%>
<%@page import="org.opencps.dossiermgt.service.DossierFileLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.model.DossierFile"%>
<%@page import="org.opencps.dossiermgt.util.DossierMgtUtil"%>
<%@page import="org.opencps.dossiermgt.service.DossierPartLocalServiceUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.opencps.dossiermgt.model.DossierPart"%>
<%@page import="org.opencps.dossiermgt.model.DossierTemplate"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="org.opencps.dossiermgt.model.Dossier"%>
<%@page import="org.opencps.dossiermgt.service.DossierLogLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.model.DossierLog"%>
<%@page import="java.util.List"%>
<%@page import="org.opencps.util.DateTimeUtil"%>
<%@page import="org.opencps.util.PortletConstants"%>
<%@ include file="../../init.jsp"%>

<%
	Dossier dossier = (Dossier) request.getAttribute(WebKeys.DOSSIER_ENTRY);
	DossierTemplate dossierTemplate = (DossierTemplate) request.getAttribute(WebKeys.DOSSIER_TEMPLATE_ENTRY);
%>

<c:choose>
	<c:when test="<%=dossier != null && dossier.getDossierStatus() != PortletConstants.DOSSIER_STATUS_NEW %>">
		<%
			String[] actors = new String[]{StringPool.APOSTROPHE + WebKeys.ACTOR_ACTION_EMPLOYEE + StringPool.APOSTROPHE};
			String[] requestCommands = new String[]{StringPool.APOSTROPHE + WebKeys.DOSSIER_LOG_RESUBMIT_REQUEST + StringPool.APOSTROPHE, StringPool.APOSTROPHE + WebKeys.DOSSIER_LOG_PAYMENT_REQUEST + StringPool.APOSTROPHE};
			List<DossierLog> dossierLogs = DossierLogLocalServiceUtil.findRequiredProcessDossier(dossier.getDossierId(), actors, requestCommands);
			List<DossierPart> dossierPartsLevel1 = new ArrayList<DossierPart>();
			
			ServiceInfo info = null;
			String serviceInfoName = StringPool.BLANK;
			try {
				info = ServiceInfoLocalServiceUtil.getServiceInfo(dossier.getServiceInfoId());
				serviceInfoName = info.getServiceName();
				
				
			} catch (Exception e) {
				
			}
				
			if(dossierTemplate != null){
				
				try{
					List<DossierPart> lstTmp1 = DossierPartLocalServiceUtil.getDossierPartsByT_P_PT(dossierTemplate.getDossierTemplateId(), 0, PortletConstants.DOSSIER_PART_TYPE_RESULT);
					if(lstTmp1 != null){
						dossierPartsLevel1.addAll(lstTmp1);
					}
				}catch(Exception e){}
				
				try{
					List<DossierPart> lstTmp2 = DossierPartLocalServiceUtil.getDossierPartsByT_P_PT(dossierTemplate.getDossierTemplateId(), 0, PortletConstants.DOSSIER_PART_TYPE_MULTIPLE_RESULT);
					if(lstTmp2 != null){
						dossierPartsLevel1.addAll(lstTmp2);
					}
				}catch(Exception e){}
				
				
			}
		%>
		
		<div class="portlet_13_result">
		
		<aui:row>
			<aui:col width="15" cssClass="portlet_13_lb">
				<liferay-ui:message key="dossier-reception-no"/>
			</aui:col>
			<aui:col width="15">
				<%=dossier.getReceptionNo() %>
			</aui:col>
		</aui:row>
		<aui:row>
			<aui:col width="15" cssClass="portlet_13_lb">
				<liferay-ui:message key="dossier-service-name"/>
			</aui:col>
			<aui:col width="75">
				<%=serviceInfoName %>
			</aui:col>
		</aui:row>
		
		
		<aui:row>
			<aui:col width="15" cssClass="portlet_13_lb">
				<liferay-ui:message key="dossier-create-date"/>
			</aui:col>
			<aui:col width="25">
				<%=DateTimeUtil.convertDateToString(dossier.getCreateDate(), DateTimeUtil._VN_DATE_TIME_FORMAT) %>
			</aui:col>
			<aui:col width="15" cssClass="portlet_13_lb" >
				<liferay-ui:message key="dossier-submit-date"/>
			</aui:col>
			<aui:col width="25">
				<%=DateTimeUtil.convertDateToString(dossier.getSubmitDatetime(), DateTimeUtil._VN_DATE_TIME_FORMAT) %>
			</aui:col>
		</aui:row>
		<aui:row>
			<aui:col width="15" cssClass="portlet_13_lb">
				<liferay-ui:message key="dossier-receive-date"/>
			</aui:col>
			<aui:col width="25">
				<%=DateTimeUtil.convertDateToString(dossier.getReceiveDatetime(), DateTimeUtil._VN_DATE_TIME_FORMAT) %>
			</aui:col>
			<aui:col width="15" cssClass="portlet_13_lb">
				<liferay-ui:message key="dossier-reception-no"/>
			</aui:col>
			<aui:col width="25">
				<%=dossier.getReceptionNo() %>
			</aui:col>
		</aui:row>
		<aui:row>
			<aui:col width="15" cssClass="portlet_13_lb">
				<liferay-ui:message key="dossier-estimate-date"/>
			</aui:col>
			<aui:col width="25">
				<%=DateTimeUtil.convertDateToString(dossier.getEstimateDatetime(), DateTimeUtil._VN_DATE_TIME_FORMAT) %>
			</aui:col>
			<aui:col width="15" cssClass="portlet_13_lb">
				<liferay-ui:message key="dossier-finish-date"/>
			</aui:col>
			<aui:col width="25">
				<%=DateTimeUtil.convertDateToString(dossier.getFinishDatetime(), DateTimeUtil._VN_DATE_TIME_FORMAT) %>
			</aui:col>
		</aui:row>
		<aui:row>
			<aui:col width="15" cssClass="portlet_13_lb">
				<liferay-ui:message key="dossier-update-date"/>
			</aui:col>
			<aui:col width="25"></aui:col>
			<aui:col width="15" cssClass="portlet_13_lb">
				<liferay-ui:message key="log-status"/>
			</aui:col>
			<aui:col width="25"><%=dossier.getDossierStatus() %></aui:col>
		</aui:row>
		
		<aui:row>
			<aui:col width="100" cssClass="portlet_13_line"/>
		</aui:row>
		
		<c:if test="<%=dossierLogs != null && !dossierLogs.isEmpty() %>">
			<aui:row>
				<label class="portlet_13_txtitle portlet_13_ML15">
					<b>
						<liferay-ui:message key="required-process"/>
					</b>
				</label>
				<table width="100%" border="0" class="portlet_13_table">
					<!-- 
					<tr>
						<td width="10%"><liferay-ui:message key="number-order"/></td>
						<td width="30%"><liferay-ui:message key="datetime"/></td>
						<td width="30%"><liferay-ui:message key="request-command"/></td>
						<td width="30%"><liferay-ui:message key="message-info"/></td>
					</tr>
					 -->
					 
					<%
						int dem = 1;
					
						for(DossierLog dossierLog : dossierLogs){
							String cssClass = dem==1? "class=\"bor-top0\"":"";
							String cssClassLb = dem==1? "class=\"bor-top0 portlet_13_lb\"":"class=\"portlet_13_lb\"";
							%>
								<tr>
									<td <%=cssClass %> width="5px">
										<i class="fa fa-circle blue sx10"></i>
									</td>
									<td <%=cssClassLb %> width="20px" align="center">>
										<%=dem %>
									</td>
									<td <%=cssClass %>>
										<%=dossierLog.getUserUuid() != null ? 
												DateTimeUtil.convertDateToString(dossierLog.getUpdateDatetime(), DateTimeUtil._VN_DATE_TIME_FORMAT) : 
												StringPool.BLANK 
										%>
									</td>
									
									<td <%=cssClassLb %> width="95px">
										<liferay-ui:message key="request-command"/>
									</td>
									<td <%=cssClass %>>
										<liferay-ui:message key="<%=dossierLog.getRequestCommand() %>"/>
									</td>
									
									<td <%=cssClassLb %> width="95px">
										<liferay-ui:message key="message-info"/>
									</td>
									<td <%=cssClass %>>
										<%=dossierLog.getMessageInfo() %>
									</td>
								</tr>
							<%
							dem++;
						}
					%>
				</table>
			</aui:row>
		</c:if>
		<%
		if(dossierPartsLevel1 != null){
			%>
			<aui:row>
				<label class="portlet_13_txtitle portlet_13_ML15">
					<b>
						<liferay-ui:message key="dossier-file-result"/>
					</b>
				</label>
				<table width="100%" border="0" class="portlet_13_table">
					<!-- 
					<tr>
						<td width="10%" bordercolor="#ccc" class="bor-top0"><liferay-ui:message key="number-order"/></td>
						<td width="30%" bordercolor="#ccc" class="bor-top0"><liferay-ui:message key="dossier-file-date"/></td>
						<td width="30%" bordercolor="#ccc" class="bor-top0"><liferay-ui:message key="dossier-file-no"/></td>
						<td width="30%" bordercolor="#ccc" class="bor-top0"><liferay-ui:message key="dossier-file-name"/></td>
					</tr>
					-->
					<%
					int count = 1;
					for (DossierPart dossierPartLevel1 : dossierPartsLevel1){
						
						int partType = dossierPartLevel1.getPartType();
					
						List<DossierPart> dossierParts = DossierMgtUtil.getTreeDossierPart(dossierPartLevel1.getDossierpartId());
						
						if(dossierParts != null){
							for(DossierPart dossierPart : dossierParts){
								DossierFile dossierFile = null;
								try{
									dossierFile = DossierFileLocalServiceUtil.getDossierFileInUse(dossier.getDossierId(), dossierPart.getDossierpartId());
								}catch(Exception e){
									continue;
								}
								
								if(dossierFile.getFileEntryId() <= 0 || dossierFile.getSyncStatus() != PortletConstants.DOSSIER_FILE_SYNC_STATUS_SYNCSUCCESS){
									continue;
								}
								
								
								String fileURL = StringPool.BLANK;
								
								try{
									FileEntry fileEntry = DLFileEntryUtil.getFileEntry(dossierFile.getFileEntryId());
									if(fileEntry != null){
										fileURL = DLUtil.getPreviewURL(fileEntry, fileEntry.getFileVersion(), 
												themeDisplay, StringPool.BLANK);
									}
								}catch(Exception e){
									continue;
									
								}
								
								String cssClass = count==1? "class=\"bor-top0\"":"";
								String cssClassLb = count==1? "class=\"bor-top0 portlet_13_lb\"":"class=\"portlet_13_lb\"";
								
								%>
									<tr>
										<td <%=cssClass %> width="5px">
											<i class="fa fa-circle blue sx10"></i>
										</td>
										<td <%=cssClassLb %> width="20px" align="center"><%=count %>.</td>
										<td <%=cssClass %>>
											<%=Validator.isNotNull(dossierFile.getDossierFileDate()) ? DateTimeUtil.convertDateToString(dossierFile.getDossierFileDate(), DateTimeUtil._VN_DATE_TIME_FORMAT) : StringPool.BLANK %>
										</td>
										<td <%=cssClassLb %> width="95px">
											<liferay-ui:message key="dossier-file-no"/>
										</td>
										<td <%=cssClass %>>
											<%=Validator.isNotNull(dossierFile.getDossierFileNo()) ? dossierFile.getDossierFileNo() : StringPool.BLANK %>
										</td>
										
										
										<td <%=cssClassLb %> width="95px">
											<liferay-ui:message key="dossier-file-name"/>
										</td>
										
										<td <%=cssClass %>>
											<a class="blue" href="<%=fileURL%>" target="_blank">
												<%=Validator.isNotNull(dossierFile.getDisplayName()) ? dossierFile.getDisplayName() : StringPool.BLANK  %>
											</a>
										</td>
									</tr>
								<%
								
								count++;
							}
						}
					}
					%>
				</table>
			</aui:row>
			<%
		}
		%>
		<aui:row>
			<aui:col width="100" cssClass="portlet_13_line"/>
		</aui:row>
		</div>
	</c:when>
	<c:otherwise>
		<div class="portlet-msg-info">
			<liferay-ui:message key="no-dossier-result-info"/>
		</div>
	</c:otherwise>
</c:choose>