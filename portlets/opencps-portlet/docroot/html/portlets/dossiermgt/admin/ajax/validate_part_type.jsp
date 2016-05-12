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
<%@ include file="../../init.jsp"%>
<%@page import="org.opencps.dossiermgt.search.DossierPartDisplayTerms"%>
<%@page import="com.liferay.portal.kernel.util.ParamUtil"%>
<%
	int partType = ParamUtil.getInteger(request, "partType");
%>
<c:choose>
	<c:when test="<%=partType == 5 %>">
		<aui:col cssClass="input30">
			<aui:input name="<%=DossierPartDisplayTerms.DOSSIERPART_TEMPLATEFILENO %>" >
				<aui:validator name="required" />
				<aui:validator name="maxLength" >100</aui:validator>
			</aui:input>	
		</aui:col>
	</c:when>
	<c:otherwise>
		<aui:col cssClass="input30">
			<aui:input name="<%=DossierPartDisplayTerms.DOSSIERPART_TEMPLATEFILENO %>" />	
		</aui:col>
	</c:otherwise>
</c:choose>
