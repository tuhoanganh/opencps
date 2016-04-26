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

<%@page import="org.opencps.dossiermgt.model.DossierFile"%>
<%@page import="org.opencps.dossiermgt.model.DossierPart"%>
<%@page import="org.opencps.dossiermgt.search.DossierFileDisplayTerms"%>
<%@page import="org.opencps.dossiermgt.service.DossierPartLocalServiceUtil"%>
<%@page import="org.opencps.util.PortletConstants"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@ include file="../init.jsp"%>

<%
	long dossierPartId = ParamUtil.getLong(request, DossierFileDisplayTerms.DOSSIER_PART_ID);

	int index = ParamUtil.getInteger(request, DossierFileDisplayTerms.INDEX);

	DossierPart dossierPart = null;
	
	if(dossierPartId > 0){
		try{
			dossierPart = DossierPartLocalServiceUtil.getDossierPart(dossierPartId);
		}catch(Exception e){
			
		}
	}
	String alpacaSchema = dossierPart != null && Validator.isNotNull(dossierPart.getFormScript()) ? 
			dossierPart.getFormScript() : PortletConstants.UNKNOW_ALPACA_SCHEMA;

%>

<aui:form name="fm" action="post">
	<aui:fieldset id="dynamicForm"></aui:fieldset>
	<aui:fieldset>
		<aui:button type="button" value="save" name="save" cssClass="saveForm"/>
	</aui:fieldset>
</aui:form>

<aui:script>
	AUI().ready(function(A){
		var alpacaSchema = <%=alpacaSchema%>;
		var index = '<%=index%>';
		var dossierPartId = '<%=dossierPartId%>';
		
		if(alpacaSchema.options != 'undefined' && alpacaSchema.schema != 'undefined'){
			//Overwrite function
			alpacaSchema.postRender = function(control){
				$(".saveForm").click(function(e) {
					var formData = control.getValue();
					var responseData = new Object();
					responseData.index = index;
					responseData.dossierPartId = dossierPartId;
					responseData.formData = formData;
					console.log(responseData);
					<portlet:namespace/>responseData(responseData);
			    });
			};
		}
		var el = $("#<portlet:namespace/>dynamicForm");
		
		Alpaca(el, alpacaSchema);
	});
	
	
	
	Liferay.provide(window, '<portlet:namespace/>responseData', function(schema) {
		console.log(schema);
		var Util = Liferay.Util;
		Util.getOpener().Liferay.fire('getDynamicFormDataSchema', {responseData:schema});
		<portlet:namespace/>closeDialog();
	});
	
	Liferay.provide(window, '<portlet:namespace/>closeDialog', function() {
		var dialog = Liferay.Util.getWindow('<portlet:namespace/>dynamicForm');
		dialog.destroy(); // You can try toggle/hide whate
	});
</aui:script>
