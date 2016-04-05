
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
import org.opencps.util.MessageKeys;
import org.opencps.util.WebKeys;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.util.bridges.mvc.MVCPortlet;

public class AccountMgtPortlet extends MVCPortlet {

	@Override
	public void render(
	    RenderRequest renderRequest, RenderResponse renderResponse)
	    throws PortletException, IOException {

		long citizenId =
		    ParamUtil.getLong(renderRequest, CitizenDisplayTerms.CITIZEN_ID);

		long businessId =
		    ParamUtil.getLong(
		        renderRequest, BusinessDisplayTerms.BUSINESS_BUSINESSID);

		try {
			
			
			if (citizenId > 0) {
				Citizen citizen =
				    CitizenLocalServiceUtil.fetchCitizen(citizenId);
				renderRequest.setAttribute(WebKeys.CITIZEN_ENTRY, citizen);
			}

			if (businessId > 0) {
				Business business =
				    BusinessLocalServiceUtil.fetchBusiness(businessId);
				renderRequest.setAttribute(
				    WebKeys.BUSINESS_ENTRY, business);
			}
			
			renderRequest.setAttribute(WebKeys.ACCOUNTMGT_ADMIN_PROFILE, true);
		}

		catch (Exception e) {
			_log.error(e);
		}

		super.render(renderRequest, renderResponse);
	}

	public void changeAccountStatusURL(
	    ActionRequest actionRequest, ActionResponse actionResponse) {

		int accountStatus =
		    ParamUtil.getInteger(
		        actionRequest, CitizenDisplayTerms.CITIZEN_ACCOUNTSTATUS);
		long citizenId =
		    ParamUtil.getLong(actionRequest, CitizenDisplayTerms.CITIZEN_ID);

		Citizen citizen = null;
		try {
			ServiceContext serviceContext =
			    ServiceContextFactory.getInstance(actionRequest);
			citizen = CitizenLocalServiceUtil.fetchCitizen(citizenId);
			if (citizen != null) {
				if (accountStatus == 0) {
					CitizenLocalServiceUtil.deleteCitizen(citizen);
				}
				else if (accountStatus == 1) {
					CitizenLocalServiceUtil.updateStatus(
					    citizenId, serviceContext.getUserId(), 2);
				}
				else if (accountStatus == 2) {
					CitizenLocalServiceUtil.updateStatus(
					    citizenId, serviceContext.getUserId(), 3);
				}
				else if (accountStatus == 3) {
					CitizenLocalServiceUtil.updateStatus(
					    citizenId, serviceContext.getUserId(), 2);
				}
				else {
					SessionErrors.add(
					    actionRequest, MessageKeys.NO_ACCOUNT_STATUS_FOUND);
				}
			}
		}
		catch (Exception e) {
			// SessionErrors.add(actionRequest,
			// MessageKeys.NO_ACCOUNT_STATUS_FOUND);
		}
	}

	public void searchCitizen(
	    ActionRequest actionRequest, ActionResponse actionResponse) {

		String fullName =
		    ParamUtil.getString(
		        actionRequest, CitizenDisplayTerms.CITIZEN_FULLNAME);
		int accountStatus =
		    ParamUtil.getInteger(
		        actionRequest, CitizenDisplayTerms.CITIZEN_ACCOUNTSTATUS);

		actionResponse.setRenderParameter(
		    CitizenDisplayTerms.CITIZEN_FULLNAME, fullName);
		actionResponse.setRenderParameter(
		    CitizenDisplayTerms.CITIZEN_ACCOUNTSTATUS,
		    String.valueOf(accountStatus));
	}

	private Log _log = LogFactoryUtil.getLog(AccountMgtPortlet.class.getName());
}
