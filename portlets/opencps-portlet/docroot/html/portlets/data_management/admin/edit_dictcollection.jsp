<%@page import="org.opencps.datamgt.search.DictCollectionDisplayTerms"%>
<%@page import="org.opencps.datamgt.model.impl.DictCollectionImpl"%>
<%@page import="org.opencps.datamgt.util.WebKeys"%>
<%@page import="org.opencps.datamgt.model.DictCollection"%>
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

<portlet:actionURL var="addDictCollectionURL" name="addDictCollection" ></portlet:actionURL>

<%
	DictCollection dictCollection = (DictCollection)request.getAttribute(WebKeys.DICT_COLLECTION_ENTRY);
	long collectionId = dictCollection != null ? dictCollection.getDictCollectionId() : 0L;
%>

<aui:form action="<%=addDictCollectionURL.toString() %>" method="post" name="addColl">
	
	<aui:model-context bean="<%=dictCollection %>" model="<%=DictCollection.class %>" />
	
	<aui:fieldset>
		<aui:input name="<%=DictCollectionDisplayTerms.COLLECTION_NAME %>" value='<%=(dictCollection != null) ? dictCollection.getCollectionName() : ""%>'>
			<aui:validator name=""></aui:validator>
		</aui:input>
	
		<aui:input name="<%=DictCollectionDisplayTerms.COLLECTION_CODE %>" type="text" value='<%=(dictCollection != null) ? dictCollection.getCollectionCode() : "" %>'>
			<aui:validator name=""></aui:validator>
		</aui:input>
		
		<aui:input name="<%=DictCollectionDisplayTerms.DESCRIPTION %>" type="textarea" value='<%=(dictCollection != null) ? dictCollection.getDescription() : ""%>'></aui:input>
		<aui:input name="<%=DictCollectionDisplayTerms.DICTCOLLECTION_ID %>" type="hidden"></aui:input>
	</aui:fieldset>
	<aui:fieldset>
		<%-- <aui:button type="cancel" value="Limpiar" onclick="this.form.reset()" /> --%>
		<aui:button type="reset" value="clear"/>
		<aui:button type="submit" name="submit" value="submit"/> 
	</aui:fieldset>	
</aui:form>