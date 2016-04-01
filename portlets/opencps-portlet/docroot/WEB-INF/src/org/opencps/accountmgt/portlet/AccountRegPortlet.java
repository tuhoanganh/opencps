
package org.opencps.accountmgt.portlet;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
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
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.File;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

public class AccountRegPortlet extends MVCPortlet {

	@Override
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
	
	public void updateBusiness(ActionRequest actionRequest, ActionResponse actionResponse) {
		long businessId = ParamUtil.getLong(actionRequest, 
				BusinessDisplayTerms.BUSINESS_BUSINESSID);
		String name = ParamUtil.getString(actionRequest, 
			BusinessDisplayTerms.BUSINESS_NAME);
		String enName = ParamUtil.getString(actionRequest, 
			BusinessDisplayTerms.BUSINESS_ENNAME);
		String idNumber = ParamUtil.getString(actionRequest, 
			BusinessDisplayTerms.BUSINESS_IDNUMBER);
		String shortName = ParamUtil.getString(actionRequest, 
			BusinessDisplayTerms.BUSINESS_SHORTNAME);
		String type = ParamUtil.getString(actionRequest, 
			BusinessDisplayTerms.BUSINESS_BUSINESSTYPE);
		String address = ParamUtil.getString(actionRequest, 
			BusinessDisplayTerms.BUSINESS_ADDRESS); 
		String cityCode = ParamUtil.getString(actionRequest, 
			BusinessDisplayTerms.BUSINESS_CITYCODE); 
		String districtCode = ParamUtil.getString(actionRequest, 
			BusinessDisplayTerms.BUSINESS_DISTRICTCODE);
		String wardCode = ParamUtil.getString(actionRequest, 
			BusinessDisplayTerms.BUSINESS_WARDCODE);				
		String email = ParamUtil.getString(actionRequest, 
			BusinessDisplayTerms.BUSINESS_EMAIL);
		String telNo = ParamUtil.getString(actionRequest, 
			BusinessDisplayTerms.BUSINESS_TELNO);
		String representativeName = ParamUtil.getString(actionRequest, 
			BusinessDisplayTerms.BUSINESS_REPRESENTATIVENAME);
		String representativeRole = ParamUtil.getString(actionRequest, 
			BusinessDisplayTerms.BUSINESS_REPRESENTATIVEROLE);
		
		String [] domain = ParamUtil.getParameterValues(actionRequest, 
			BusinessDisplayTerms.BUSINESS_DOMAIN);
		
		UploadPortletRequest uploadPortletRequest = PortalUtil
						.getUploadPortletRequest(actionRequest); 
		File attachFile = (File) uploadPortletRequest.getFile("attachFile");
	}
	
	private Log _log = LogFactoryUtil
	    .getLog(AccountRegPortlet.class
	        .getName());
}
