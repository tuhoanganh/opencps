
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


<%@ include file="../init.jsp" %>

<portlet:renderURL var="registerCitizenURL">
	<portlet:param name="mvcPath" value='<%=templatePath + "citizenregistration.jsp" %>'/>
</portlet:renderURL>

<portlet:renderURL var="registerBusinessURL">
	<portlet:param name="mvcPath" value='<%=templatePath + "businessregistration.jsp" %>'/>
</portlet:renderURL>

<div class="portlet-msg-info">
	Click here to register <a href="<%= registerCitizenURL%>">Citizen</a> or <a href="<%= registerBusinessURL%>">Business</a>
</div>