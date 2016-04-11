package org.opencps.dossiermgt.portlet;

import java.io.IOException;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.opencps.dossiermgt.model.DossierPart;
import org.opencps.dossiermgt.model.DossierTemplate;
import org.opencps.dossiermgt.search.DossierPartDisplayTerms;
import org.opencps.dossiermgt.search.DossierTemplateDisplayTerms;
import org.opencps.dossiermgt.service.DossierPartLocalServiceUtil;
import org.opencps.dossiermgt.service.DossierTemplateLocalServiceUtil;
import org.opencps.util.WebKeys;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * Portlet implementation class ServiceConfigMgtPortlet
 */
public class DossierMgtAdminPortlet extends MVCPortlet {
	@Override
	public void render(
	    RenderRequest renderRequest, RenderResponse renderResponse)
	    throws PortletException, IOException {

		long dossierTemplateId = ParamUtil
		    .getLong(renderRequest, DossierTemplateDisplayTerms
		    	.DOSSIERTEMPLATE_DOSSIERTEMPLATEID);

		long dossierPartId = ParamUtil
		    .getLong(renderRequest, DossierPartDisplayTerms.DOSSIERPART_DOSSIERPARTID);

		try {

			if (dossierTemplateId > 0) {
				DossierTemplate dossierTemplate = DossierTemplateLocalServiceUtil
				    .fetchDossierTemplate(dossierTemplateId);
				renderRequest
				    .setAttribute(WebKeys.DOSSIER_TEMPLATE_ENTRY, dossierTemplate);
			}

			if (dossierPartId > 0) {
				DossierPart dossierPart = DossierPartLocalServiceUtil
				    .fetchDossierPart(dossierPartId);
				renderRequest
				    .setAttribute(WebKeys.BUSINESS_ENTRY, dossierPart);
			}

		}

		catch (Exception e) {
			_log
			    .error(e);
		}

		super.render(renderRequest, renderResponse);
	}
	
	private Log _log = LogFactoryUtil
				    .getLog(DossierMgtAdminPortlet.class
				        .getName());
}
