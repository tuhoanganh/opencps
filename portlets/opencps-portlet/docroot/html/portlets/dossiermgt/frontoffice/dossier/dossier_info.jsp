
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
<%@page import="org.opencps.dossiermgt.search.DossierDisplayTerms"%>
<%@page import="org.opencps.util.PortletPropsValues"%>
<%@ include file="../../init.jsp"%>


<aui:row>
	<aui:col width="30">
		<aui:input name="<%=DossierDisplayTerms.SERVICE_NAME %>" cssClass="input100" type="text"/>	
	</aui:col>
	<aui:col width="30">
		<aui:input name="<%=DossierDisplayTerms.SERVICE_NAME %>" cssClass="input100" type="text"/>	
	</aui:col>
	
	<aui:col width="30">
		<aui:input name="<%=DossierDisplayTerms.SERVICE_NO %>" cssClass="input100" type="text"/>	
	</aui:col>
</aui:row>

<aui:row>
	<datamgt:ddr 
		depthLevel="3" 
		dictCollectionCode="ADMINISTRATIVE_REGION"
		itemNames="cityCode,districtCode,wardCode"
		itemsEmptyOption="true,true,true"
		cssClass="input100"
	/>
</aui:row>