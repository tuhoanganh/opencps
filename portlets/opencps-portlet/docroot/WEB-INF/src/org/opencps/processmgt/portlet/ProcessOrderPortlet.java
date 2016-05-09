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

package org.opencps.processmgt.portlet;

import java.io.IOException;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.opencps.dossiermgt.model.Dossier;
import org.opencps.dossiermgt.model.DossierTemplate;
import org.opencps.dossiermgt.model.ServiceConfig;
import org.opencps.dossiermgt.service.DossierLocalServiceUtil;
import org.opencps.dossiermgt.service.DossierTemplateLocalServiceUtil;
import org.opencps.dossiermgt.service.ServiceConfigLocalServiceUtil;
import org.opencps.processmgt.model.ProcessOrder;
import org.opencps.processmgt.model.ProcessStep;
import org.opencps.processmgt.model.ServiceProcess;
import org.opencps.processmgt.search.ProcessOrderDisplayTerms;
import org.opencps.processmgt.service.ProcessOrderLocalServiceUtil;
import org.opencps.processmgt.service.ProcessStepLocalServiceUtil;
import org.opencps.processmgt.service.ServiceProcessLocalServiceUtil;
import org.opencps.servicemgt.model.ServiceInfo;
import org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil;
import org.opencps.util.WebKeys;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * @author trungnt
 */
public class ProcessOrderPortlet extends MVCPortlet {

	@Override
	public void render(
	    RenderRequest renderRequest, RenderResponse renderResponse)
	    throws PortletException, IOException {

		long processOrderId = ParamUtil
		    .getLong(renderRequest, ProcessOrderDisplayTerms.PROCESS_ORDER_ID);

		if (processOrderId > 0) {
			try {
				ProcessOrder processOrder = ProcessOrderLocalServiceUtil
				    .getProcessOrder(processOrderId);
				ProcessStep processStep = ProcessStepLocalServiceUtil
				    .getProcessStep(processOrder
				        .getProcessStepId());
				Dossier dossier = DossierLocalServiceUtil
				    .getDossier(processOrder
				        .getDossierId());
				ServiceProcess serviceProcess = ServiceProcessLocalServiceUtil
				    .getServiceProcess(processOrder
				        .getServiceProcessId());
				ServiceInfo serviceInfo = ServiceInfoLocalServiceUtil
				    .getServiceInfo(processOrder
				        .getServiceInfoId());
				ServiceConfig serviceConfig = ServiceConfigLocalServiceUtil
				    .getServiceConfig(dossier
				        .getServiceConfigId());
				
				DossierTemplate dossierTemplate = DossierTemplateLocalServiceUtil
					.getDossierTemplate(dossier.getDossierTemplateId());

				renderRequest
				    .setAttribute(WebKeys.PROCESS_ORDER_ENTRY, processOrder);
				renderRequest
				    .setAttribute(WebKeys.PROCESS_STEP_ENTRY, processStep);
				renderRequest
				    .setAttribute(WebKeys.DOSSIER_ENTRY, dossier);
				renderRequest
				    .setAttribute(
				        WebKeys.SERVICE_PROCESS_ENTRY, serviceProcess);
				renderRequest
				    .setAttribute(WebKeys.SERVICE_INFO_ENTRY, serviceInfo);
				renderRequest
				    .setAttribute(WebKeys.SERVICE_CONFIG_ENTRY, serviceConfig);
				
				renderRequest
			    	.setAttribute(WebKeys.DOSSIER_TEMPLATE_ENTRY, dossierTemplate);
			}
			catch (Exception e) {
				_log
				    .error(e);
			}
		}
		super.render(renderRequest, renderResponse);
	}

	private Log _log = LogFactoryUtil
	    .getLog(ProcessOrderPortlet.class
	        .getName());
}
