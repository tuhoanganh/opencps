
package org.opencps.accountmgt.portlet;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.opencps.accountmgt.OutOfLengthBusinessEnNameException;
import org.opencps.accountmgt.OutOfLengthBusinessNameException;
import org.opencps.accountmgt.OutOfLengthBusinessRepresentativeNameException;
import org.opencps.accountmgt.OutOfLengthBusinessRepresentativeRoleException;
import org.opencps.accountmgt.OutOfLengthBusinessShortNameException;
import org.opencps.accountmgt.OutOfLengthCitizenAddressException;
import org.opencps.accountmgt.OutOfLengthCitizenNameException;
import org.opencps.accountmgt.model.Business;
import org.opencps.accountmgt.model.Citizen;
import org.opencps.accountmgt.search.BusinessDisplayTerms;
import org.opencps.accountmgt.search.CitizenDisplayTerms;
import org.opencps.accountmgt.service.BusinessLocalServiceUtil;
import org.opencps.accountmgt.service.CitizenLocalServiceUtil;
import org.opencps.datamgt.model.DictItem;
import org.opencps.datamgt.service.DictItemLocalServiceUtil;
import org.opencps.usermgt.search.EmployeeDisplayTerm;
import org.opencps.util.MessageKeys;
import org.opencps.util.WebKeys;

import com.liferay.portal.UserPasswordException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
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
				renderRequest.setAttribute(WebKeys.BUSINESS_ENTRY, business);
			}

			renderRequest.setAttribute(WebKeys.ACCOUNTMGT_ADMIN_PROFILE, true);
		}

		catch (Exception e) {
			_log.error(e);
		}

		super.render(renderRequest, renderResponse);
	}

	public void changeAccountStatusCitizen(
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

	public void updateCitizenProfile(
	    ActionRequest actionRequest, ActionResponse actionResponse) {

		long citizenId =
		    ParamUtil.getLong(actionRequest, CitizenDisplayTerms.CITIZEN_ID);

		String address =
		    ParamUtil.getString(
		        actionRequest, CitizenDisplayTerms.CITIZEN_ADDRESS);
		String telNo =
		    ParamUtil.getString(actionRequest, EmployeeDisplayTerm.TEL_NO);
		String curPass =
		    ParamUtil.getString(
		        actionRequest, CitizenDisplayTerms.CURRENT_PASSWORD);
		String newPass =
		    ParamUtil.getString(actionRequest, CitizenDisplayTerms.NEW_PASSWORD);
		String rePass =
		    ParamUtil.getString(actionRequest, CitizenDisplayTerms.RE_PASSWORD);
		long cityId =
		    ParamUtil.getLong(
		        actionRequest, CitizenDisplayTerms.CITIZEN_CITY_ID);
		long districtId =
		    ParamUtil.getLong(
		        actionRequest, CitizenDisplayTerms.CITIZEN_DISTRICT_ID);
		long wardId =
		    ParamUtil.getLong(
		        actionRequest, CitizenDisplayTerms.CITIZEN_WARD_ID);

		DictItem city = null;

		DictItem district = null;

		DictItem ward = null;

		boolean isChangePassWord =
		    curPass.equals(StringPool.BLANK) ? true : false;

		try {
			AccountRegPortlet.ValidateCitizen(
			    citizenId, StringPool.BLANK, StringPool.BLANK, address,
			    StringPool.BLANK, telNo, 0, StringPool.BLANK);

			ServiceContext serviceContext =
			    ServiceContextFactory.getInstance(actionRequest);

			city = DictItemLocalServiceUtil.getDictItem(cityId);

			district = DictItemLocalServiceUtil.getDictItem(districtId);

			ward = DictItemLocalServiceUtil.getDictItem(wardId);

			if (citizenId > 0) {
				CitizenLocalServiceUtil.updateCitizen(
				    citizenId, address, city.getItemCode(),
				    district.getItemCode(), ward.getItemCode(),
				    city.getItemName(serviceContext.getLocale(), true),
				    district.getItemName(serviceContext.getLocale(), true),
				    ward.getItemName(serviceContext.getLocale(), true), telNo,
				    isChangePassWord, newPass, rePass,
				    serviceContext.getUserId(), serviceContext);

			}
		}
		catch (Exception e) {
			if (e instanceof UserPasswordException) {
				SessionErrors.add(actionRequest, UserPasswordException.class);
			}
			else if (e instanceof OutOfLengthCitizenAddressException) {
				SessionErrors.add(
				    actionRequest, OutOfLengthCitizenAddressException.class);
			}
			else if (e instanceof OutOfLengthCitizenNameException) {
				SessionErrors.add(
				    actionRequest, OutOfLengthCitizenNameException.class);
			}
			else {
				SessionErrors.add(
				    actionRequest,
				    MessageKeys.DATAMGT_SYSTEM_EXCEPTION_OCCURRED);
			}
		}
	}

	public void updateBusinessProfile(
	    ActionRequest actionRequest, ActionResponse actionResponse) {

		long businessId =
		    ParamUtil.getLong(
		        actionRequest, BusinessDisplayTerms.BUSINESS_BUSINESSID);

		long cityId =
		    ParamUtil.getLong(
		        actionRequest, BusinessDisplayTerms.BUSINESS_CITY_ID);
		long districtId =
		    ParamUtil.getLong(
		        actionRequest, BusinessDisplayTerms.BUSINESS_DISTRICT_ID);
		long wardId =
		    ParamUtil.getLong(
		        actionRequest, BusinessDisplayTerms.BUSINESS_WARD_ID);
		String name =
		    ParamUtil.getString(
		        actionRequest, BusinessDisplayTerms.BUSINESS_NAME);
		String enName =
		    ParamUtil.getString(
		        actionRequest, BusinessDisplayTerms.BUSINESS_ENNAME);
		String idNumber =
		    ParamUtil.getString(
		        actionRequest, BusinessDisplayTerms.BUSINESS_IDNUMBER);
		String shortName =
		    ParamUtil.getString(
		        actionRequest, BusinessDisplayTerms.BUSINESS_SHORTNAME);
		String type =
		    ParamUtil.getString(
		        actionRequest, BusinessDisplayTerms.BUSINESS_BUSINESSTYPE);
		String address =
		    ParamUtil.getString(
		        actionRequest, BusinessDisplayTerms.BUSINESS_ADDRESS);
		String email =
		    ParamUtil.getString(
		        actionRequest, BusinessDisplayTerms.BUSINESS_EMAIL);
		String telNo =
		    ParamUtil.getString(
		        actionRequest, BusinessDisplayTerms.BUSINESS_TELNO);
		String representativeName =
		    ParamUtil.getString(
		        actionRequest, BusinessDisplayTerms.BUSINESS_REPRESENTATIVENAME);
		String representativeRole =
		    ParamUtil.getString(
		        actionRequest, BusinessDisplayTerms.BUSINESS_REPRESENTATIVEROLE);
		String[] domain =
		    ParamUtil.getParameterValues(
		        actionRequest, BusinessDisplayTerms.BUSINESS_DOMAIN);
		String curPass =
		    ParamUtil.getString(
		        actionRequest, BusinessDisplayTerms.CURRENT_PASSWORD);
		String newPass =
		    ParamUtil.getString(
		        actionRequest, BusinessDisplayTerms.NEW_PASSWORD);
		String rePass =
		    ParamUtil.getString(actionRequest, BusinessDisplayTerms.RE_PASSWORD);
		boolean isChangePassWord =
		    curPass.equals(StringPool.BLANK) ? true : false;

		DictItem city = null;

		DictItem district = null;

		DictItem ward = null;

		try {

			AccountRegPortlet.ValidateBusiness(
			    businessId, email, StringPool.BLANK, enName, shortName,
			    address, representativeName, representativeRole);

			city = DictItemLocalServiceUtil.getDictItem(cityId);

			district = DictItemLocalServiceUtil.getDictItem(districtId);

			ward = DictItemLocalServiceUtil.getDictItem(wardId);
			ServiceContext serviceContext =
			    ServiceContextFactory.getInstance(actionRequest);

			if (businessId > 0) {
				district.getItemName(serviceContext.getLocale(), true);
				BusinessLocalServiceUtil.updateBusiness(
				    businessId, name, enName, shortName, type, idNumber,
				    address, city.getItemCode(), district.getItemCode(),
				    ward.getItemCode(),
				    city.getItemName(serviceContext.getLocale(), true),
				    district.getItemName(serviceContext.getLocale(), true),
				    ward.getItemName(serviceContext.getLocale(), true), telNo,
				    representativeName, representativeRole, domain,
				    isChangePassWord, curPass, rePass,
				    serviceContext.getUserId(), serviceContext);

			}

		}
		catch (Exception e) {
			if (e instanceof UserPasswordException) {
				SessionErrors.add(actionRequest, UserPasswordException.class);
			}
			else if (e instanceof OutOfLengthBusinessNameException) {
				SessionErrors.add(
				    actionRequest, OutOfLengthBusinessNameException.class);
			}
			else if (e instanceof OutOfLengthBusinessEnNameException) {
				SessionErrors.add(
				    actionRequest, OutOfLengthBusinessEnNameException.class);
			}
			else if (e instanceof OutOfLengthBusinessShortNameException) {
				SessionErrors.add(
				    actionRequest, OutOfLengthBusinessShortNameException.class);
			}
			else if (e instanceof OutOfLengthBusinessRepresentativeNameException) {
				SessionErrors.add(
				    actionRequest,
				    OutOfLengthBusinessRepresentativeNameException.class);
			}
			else if (e instanceof OutOfLengthBusinessRepresentativeRoleException) {
				SessionErrors.add(
				    actionRequest,
				    OutOfLengthBusinessRepresentativeRoleException.class);
			}
			else {
				SessionErrors.add(
				    actionRequest,
				    MessageKeys.DATAMGT_SYSTEM_EXCEPTION_OCCURRED);
			}

		}
	}
	private Log _log = LogFactoryUtil.getLog(AccountMgtPortlet.class.getName());
}
