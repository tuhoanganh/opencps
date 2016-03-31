package org.opencps.accountmgt.portlet;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.opencps.accountmgt.model.Citizen;
import org.opencps.accountmgt.search.CitizenDisplayTerms;
import org.opencps.accountmgt.service.CitizenLocalServiceUtil;
import org.opencps.util.WebKeys;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;


public class AccountRegPortlet extends MVCPortlet{
	@Override
	public void render(RenderRequest renderRequest,
			RenderResponse renderResponse) {
		long citizenId = ParamUtil.getLong(
			renderRequest, CitizenDisplayTerms.CITIZEN_ID);
		
		try {
	        if(citizenId > 0) {
	        	Citizen citizen = CitizenLocalServiceUtil.fetchCitizen(citizenId);
	        	renderRequest.setAttribute(WebKeys.CITIZEN_ENTRY, citizen);
	        }
        }
        catch (Exception e) {
	        _log.error(e);
        }
	}
	
	private Log _log = LogFactoryUtil
					.getLog(AccountRegPortlet.class.getName());
}
