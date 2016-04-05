
package org.opencps.accountmgt.portlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import org.opencps.datamgt.model.DictItem;
import org.opencps.datamgt.service.DictItemLocalServiceUtil;
import org.opencps.usermgt.search.EmployeeDisplayTerm;
import org.opencps.util.PortletPropsValues;
import org.opencps.util.WebKeys;

import com.liferay.portal.UserPasswordException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroup;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.util.bridges.mvc.MVCPortlet;

public class AccountProfilePortlet extends MVCPortlet {

	@Override
	public void render(
	    RenderRequest renderRequest, RenderResponse renderResponse)
	    throws PortletException, IOException {

		ThemeDisplay themeDisplay =
		    (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
		String accountType = StringPool.BLANK;

		if (themeDisplay.isSignedIn()) {
			List<UserGroup> userGroups = new ArrayList<UserGroup>();

			try {

				User user = themeDisplay.getUser();
				userGroups = user.getUserGroups();

				if (!userGroups.isEmpty()) {
					for (UserGroup userGroup : userGroups) {
						if (userGroup.getName().equals(
						    PortletPropsValues.USERMGT_USERGROUP_NAME_CITIZEN) ||
						    userGroup.getName().equals(
						        PortletPropsValues.USERMGT_USERGROUP_NAME_BUSINESS)) {
							accountType = userGroup.getName();
							break;
						}

					}
				}

				renderRequest.setAttribute(WebKeys.ACCOUNT_TYPE, accountType);

				if (accountType.equals(PortletPropsValues.USERMGT_USERGROUP_NAME_CITIZEN)) {
					Citizen citizen =
					    CitizenLocalServiceUtil.getCitizen(user.getUserId());
					renderRequest.setAttribute(WebKeys.CITIZEN_ENTRY, citizen);
				}
				else if (accountType.equals(PortletPropsValues.USERMGT_USERGROUP_NAME_BUSINESS)) {

					Business business =
					    BusinessLocalServiceUtil.getBusiness(user.getUserId());
					renderRequest.setAttribute(WebKeys.BUSINESS_ENTRY, business);
				}

				renderRequest.setAttribute(
				    WebKeys.ACCOUNTMGT_VIEW_PROFILE, true);
			}
			catch (Exception e) {
				_log.error(e);
			}
		}

		super.render(renderRequest, renderResponse);
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

		boolean isChangePassWord = curPass != null ? true : false;

		try {
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
			else {
				_log.info(e);
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
		boolean isChangePassWord = curPass != null ? true : false;

		DictItem city = null;

		DictItem district = null;

		DictItem ward = null;

		try {
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
			else {
				_log.info(e);
			}
		}
	}
	private Log _log =
	    LogFactoryUtil.getLog(AccountProfilePortlet.class.getName());
}
