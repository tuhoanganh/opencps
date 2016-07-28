
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
<%@page import="org.opencps.processmgt.search.ProcessOrderSearchTerms"%>
<%@page import="org.opencps.processmgt.search.ProcessOrderSearch"%>
<%@page import="org.opencps.processmgt.model.ProcessOrder"%>
<%@page import="org.opencps.processmgt.util.ProcessUtils"%>
<%@page import="org.opencps.dossiermgt.bean.ProcessOrderBean"%>
<%@page import="com.liferay.portal.kernel.dao.search.RowChecker"%>
<%@page import="org.opencps.processmgt.service.ProcessOrderLocalServiceUtil"%>
<%@page import="org.opencps.processmgt.search.ProcessOrderDisplayTerms"%>
<%@ include file="../init.jsp"%>


<liferay-util:include page='<%=templatePath + "toptabs.jsp" %>' servletContext="<%=application %>" />
<liferay-util:include page='<%=templatePath + "toolbar.jsp" %>' servletContext="<%=application %>" />

<%
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "processordertodolist.jsp");
	iteratorURL.setParameter("tabs1", ProcessUtils.TOP_TABS_PROCESS_ORDER_WAITING_PROCESS);
	
	List<ProcessOrderBean> processOrders =  new ArrayList<ProcessOrderBean>();
	
	int totalCount = 0;
	
	RowChecker rowChecker = new RowChecker(liferayPortletResponse);
%>
<aui:form name="fm">
	<liferay-ui:search-container 
		searchContainer="<%= new ProcessOrderSearch(renderRequest, SearchContainer.DEFAULT_DELTA, iteratorURL) %>"
		rowChecker="<%=rowChecker%>"
	>
	
		<liferay-ui:search-container-results>
			<%
				ProcessOrderSearchTerms searchTerms = (ProcessOrderSearchTerms)searchContainer.getSearchTerms();
			
				long serviceInfoId = searchTerms.getServiceInfoId();
				
				long processStepId = searchTerms.getProcessStepId();
				
				long assignToUserId = themeDisplay.getUserId();
				try{
					
					%>
						<%@include file="/html/portlets/processmgt/processorder/process_order_search_results.jspf" %>
					<%
				}catch(Exception e){
					_log.error(e);
				}
			
				total = totalCount;
				results = processOrders;
				
				pageContext.setAttribute("results", results);
				pageContext.setAttribute("total", total);
			%>
		</liferay-ui:search-container-results>	
			<liferay-ui:search-container-row 
				className="org.opencps.dossiermgt.bean.ProcessOrderBean" 
				modelVar="processOrder" 
				keyProperty="processOrderId"
				rowVar="row"
				stringKey="<%=true%>"
				
			>
				<%
					PortletURL processURL = renderResponse.createRenderURL();
					processURL.setParameter("mvcPath", templatePath + "process_order_detail.jsp");
					processURL.setParameter(ProcessOrderDisplayTerms.PROCESS_ORDER_ID, String.valueOf(processOrder.getProcessOrderId()));
					processURL.setParameter("backURL", currentURL);
					processURL.setParameter("isEditDossier", (processOrder.isReadOnly() || (processOrder.getAssignToUsesrId() != 0 &&  processOrder.getAssignToUsesrId() != user.getUserId())) ? String.valueOf(false) : String.valueOf(true));
					
					
					String receptionNo = LanguageUtil.get(portletConfig, themeDisplay.getLocale(), "reception-no");
					String subjectName = LanguageUtil.get(portletConfig, themeDisplay.getLocale(), "subject-name");
					String serviceName = LanguageUtil.get(portletConfig, themeDisplay.getLocale(), "service-name");
					String processStep = LanguageUtil.get(portletConfig, themeDisplay.getLocale(), "process-step");
					String assignUser = LanguageUtil.get(portletConfig, themeDisplay.getLocale(), "assign-to-user");
					String deadline = LanguageUtil.get(portletConfig, themeDisplay.getLocale(), "dealine");
					String actionButt = LanguageUtil.get(portletConfig, themeDisplay.getLocale(), "action");
					
					String deadlineVal = Validator.isNotNull(processOrder.getDealine()) ? processOrder.getDealine() : StringPool.DASH;
					
					String hrefFix = "location.href='" + processURL.toString()+"'";
					
					String s1 = "<div class=\"ocps-searh-bound-data1\"><p class=\"ocps-searh-bound-data1-chirld-p1\"><span class=\"ocps-searh-bound-data1-chirld-span1\">" + receptionNo + "</span><a class=\"ocps-searh-bound-data1-chirld-label1\" href=\""+ processURL + " \"> "+ processOrder.getReceptionNo() + "</a></p>";
					s1 = s1 + "<p class=\"ocps-searh-bound-data1-chirld-p1\"><span class=\"ocps-searh-bound-data1-chirld-span1\">"+ serviceName +"</span><a class=\"ocps-searh-bound-data1-chirld-label1\" href=\""+processURL+"\"> "+ processOrder.getServiceName() + "</a></p></div>";
					
					
					String s2 = "<div class=\"ocps-searh-bound-data1\"><p class=\"ocps-searh-bound-data1-chirld-p1\"><span class=\"ocps-searh-bound-data1-chirld-span1\">" + subjectName + "</span><a class=\"ocps-searh-bound-data1-chirld-label1\" href=\""+ processURL + " \"> "+ processOrder.getSubjectName() + "</a></p>";
					s2 = s2 + "<p class=\"ocps-searh-bound-data1-chirld-p1\"><span class=\"ocps-searh-bound-data1-chirld-span1\">" + assignUser + "</span><a class=\"ocps-searh-bound-data1-chirld-label1\" href=\""+ processURL + "\"> "+ processOrder.getAssignToUserName() + "</a></p>";
					s2 = s2 + "<p class=\"ocps-searh-bound-data1-chirld-p1\"><span class=\"ocps-searh-bound-data1-chirld-span1\">" + processStep + "</span><a class=\"ocps-searh-bound-data1-chirld-label1\" href=\""+ processURL + "\"> "+ processOrder.getStepName() + "</a></p>";
					s2 = s2 + "<p class=\"ocps-searh-bound-data1-chirld-p1\"><span class=\"ocps-searh-bound-data1-chirld-span1\">"+ deadline +"</span><a class=\"ocps-searh-bound-data1-chirld-label1\" href=\""+processURL+"\"> "+ deadlineVal + "</a></p></div>";
					
					row.addText(s1);
					row.addText(s2);
					row.addButton(actionButt, hrefFix);
					/* row.addText(processOrder.getReceptionNo(), processURL);
					row.addText(processOrder.getSubjectName(), processURL);
					row.addText(processOrder.getServiceName(), processURL);	
					row.addText(processOrder.getStepName(), processURL);	
					row.addText(processOrder.getAssignToUserName(), processURL);

					row.addText(Validator.isNotNull(processOrder.getDealine()) ? processOrder.getDealine() : StringPool.DASH, processURL); */
					row.setClassName((processOrder.isReadOnly() || (processOrder.getAssignToUsesrId() != 0 &&  processOrder.getAssignToUsesrId() != user.getUserId())) ? "readonly" : StringPool.BLANK);
					
					//row.setClassHoverName("");
				%>	
			</liferay-ui:search-container-row> 
		
		<liferay-ui:search-iterator/>
	</liferay-ui:search-container>
</aui:form>
<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.processmgt.processorder.processordertodolist.jsp");
%>