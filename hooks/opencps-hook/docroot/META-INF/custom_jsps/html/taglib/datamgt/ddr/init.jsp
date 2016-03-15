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

<%@ include file="/html/taglib/taglib-init.jsp" %>

<%

String dictCollectionCode = (String)request.getAttribute("opencps-datamgt:ddr:dictCollectionCode");
long initDictItemId = (Long)request.getAttribute("opencps-datamgt:ddr:initDictItemId");
int depthLevel = (Integer)request.getAttribute("opencps-datamgt:ddr:depthLevel");
List<String> labelItems = (List<String>)request.getAttribute("opencps-datamgt:ddr:labelItems");
List<Long> selectedItems = (List<Long>)request.getAttribute("opencps-datamgt:ddr:selectedItems");
String renderMode = (String)request.getAttribute("opencps-datamgt:ddr:renderMode");
String name = (String)request.getAttribute("opencps-datamgt:ddr:name");
String cssClass = (String)request.getAttribute("opencps-datamgt:ddr:cssClass");

System.out.println("=======================>: " + name);
%>

<%!
private static final String _NAMESPACE = "datamgt:ddr:";
%>