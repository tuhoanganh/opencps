
package org.opencps.accountmgt.portlet;

import java.io.IOException;
import java.util.Date;

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
import org.opencps.usermgt.search.EmployeeDisplayTerm;
import org.opencps.util.DateTimeUtil;
import org.opencps.util.WebKeys;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.model.User;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

public class AccountProfilePortlet extends MVCPortlet {

	public void updateCitizenProfile(
	    ActionRequest actionRequest, ActionResponse actionResponse) {

		long mappingUserId =
		    ParamUtil.getLong(
		        actionRequest, CitizenDisplayTerms.CITIZEN_MAPPINGUSERID);

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
		int birthDateDay =
		    ParamUtil.getInteger(
		        actionRequest, EmployeeDisplayTerm.BIRTH_DATE_DAY);
		int birthDateMonth =
		    ParamUtil.getInteger(
		        actionRequest, EmployeeDisplayTerm.BIRTH_DATE_MONTH);
		int birthDateYear =
		    ParamUtil.getInteger(
		        actionRequest, EmployeeDisplayTerm.BIRTH_DATE_YEAR);
		Date birthDate = DateTimeUtil
					    .getDate(birthDateDay, birthDateMonth, birthDateYear);
		if (mappingUserId > 0) {
			Citizen citizen =
			    CitizenLocalServiceUtil.getCitizenByMapUserId(mappingUserId);
				User user = UserLocalServiceUtil.getUser(mappingUserId);
			if(curPass != null) {
				if(newPass!=null && rePass!=null) {
					if(newPass.equals(rePass)) {
						citizen.setAddress(address);
						citizen.setBirthdate(birthDate);
						citizen.setTelNo(telNo);
						
						user.setPassword(newPass);
						UserLocalServiceUtil.updateUser(user);
						CitizenLocalServiceUtil.updateCitizen(citizen);
					}
				}
			}
		}
	}
	
	private Log _log = LogFactoryUtil.getLog(AccountProfilePortlet.class.getName());
}
