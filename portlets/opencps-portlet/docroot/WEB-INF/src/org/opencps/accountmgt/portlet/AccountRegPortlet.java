
package org.opencps.accountmgt.portlet;

import java.io.IOException;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.opencps.accountmgt.model.Business;
import org.opencps.accountmgt.model.Citizen;
import org.opencps.accountmgt.search.BusinessDisplayTerms;
import org.opencps.accountmgt.search.CitizenDisplayTerms;
import org.opencps.accountmgt.service.BusinessLocalServiceUtil;
import org.opencps.accountmgt.service.CitizenLocalServiceUtil;
import org.opencps.util.WebKeys;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

public class AccountRegPortlet extends MVCPortlet {

	@Override
<<<<<<< HEAD
	public void render(RenderRequest renderRequest,
			RenderResponse renderResponse) throws PortletException, IOException {
		long citizenId = ParamUtil.getLong(
			renderRequest, CitizenDisplayTerms.CITIZEN_ID);
		
		long businessId = ParamUtil.getLong(renderRequest, 
			BusinessDisplayTerms.BUSINESS_BUSINESSID);
		
		try {
	        if(citizenId > 0) {
	        	Citizen citizen = CitizenLocalServiceUtil
	        					.fetchCitizen(citizenId);
	        	renderRequest.setAttribute(WebKeys.CITIZEN_ENTRY, citizen);
	        }
	        
	        if(businessId > 0) {
	        	Business business = BusinessLocalServiceUtil
	        					.fetchBusiness(businessId);
	        	renderRequest.setAttribute(WebKeys.BUSINESS_ENTRY, business);
	        }
        }
		
		
        catch (Exception e) {
	        _log.error(e);
        }
		
		super.render(renderRequest, renderResponse);
	}
	
	
	
=======
	public void render(
	    RenderRequest renderRequest, RenderResponse renderResponse)
	    throws PortletException, IOException {

		long citizenId = ParamUtil
		    .getLong(renderRequest, CitizenDisplayTerms.CITIZEN_ID);

		try {
			if (citizenId > 0) {
				Citizen citizen = CitizenLocalServiceUtil
				    .fetchCitizen(citizenId);
				renderRequest
				    .setAttribute(WebKeys.CITIZEN_ENTRY, citizen);
			}
		}
		catch (Exception e) {
			_log
			    .error(e);
		}

		super.render(renderRequest, renderResponse);
	}

>>>>>>> FETCH_HEAD
	private Log _log = LogFactoryUtil
	    .getLog(AccountRegPortlet.class
	        .getName());
}
