/**
 * OpenCPS is the open source Core Public Services software Copyright (C)
 * 2016-present OpenCPS community This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU Affero General
 * Public License as published by the Free Software Foundation, either version 3
 * of the License, or any later version. This program is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Affero General Public License for more details. You should have received a
 * copy of the GNU Affero General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>
 */

package org.opencps.struts.action;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.opencps.accountmgt.model.Business;
import org.opencps.accountmgt.model.Citizen;
import org.opencps.accountmgt.service.BusinessLocalServiceUtil;
import org.opencps.accountmgt.service.CitizenLocalServiceUtil;
import org.opencps.util.PortletConstants;
import org.opencps.util.PortletPropsValues;

import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.security.auth.AuthTokenUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;

/**
 * @author trungnt
 */
public class VerifyEmailAddressAction extends Action {

	@Override
	public ActionForward execute(
	    ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response)
	    throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay) request
		    .getAttribute(org.opencps.util.WebKeys.THEME_DISPLAY);

		try {
			verifyEmailAddress(request, response, themeDisplay);
			String token = request
			    .getParameter("token");
			String userType = request
			    .getParameter("type");
			if (userType
			    .equals(PortletPropsValues.USERMGT_USERGROUP_NAME_CITIZEN)) {
				Citizen citizen = CitizenLocalServiceUtil
				    .getCitizenByUUID(token);
				if (citizen != null && citizen
				    .getAccountStatus() == PortletConstants.ACCOUNT_STATUS_REGISTERED) {
					citizen
					    .setAccountStatus(
					        PortletConstants.ACCOUNT_STATUS_CONFIRMED);
					citizen
					    .setModifiedDate(new Date());
					CitizenLocalServiceUtil
					    .updateCitizen(citizen);
				}
				else {
					return mapping
					    .findForward("verify.email.error");
				}
			}
			else if (userType
			    .equals(PortletPropsValues.USERMGT_USERGROUP_NAME_BUSINESS)) {
				Business business = BusinessLocalServiceUtil
				    .getBusinessByUUID(token);
				if (business != null && business
				    .getAccountStatus() == PortletConstants.ACCOUNT_STATUS_REGISTERED) {
					business
					    .setAccountStatus(
					        PortletConstants.ACCOUNT_STATUS_CONFIRMED);
					business
					    .setModifiedDate(new Date());
					BusinessLocalServiceUtil
					    .updateBusiness(business);
				}
				else {
					return mapping
					    .findForward("verify.email.error");
				}
			}
			else {
				return mapping
				    .findForward("verify.email.error");
			}
			return mapping
			    .findForward("verify.email.success");
		}
		catch (Exception e) {
			return mapping
			    .findForward("verify.email.error");

		}

	}

	protected void verifyEmailAddress(
	    HttpServletRequest request, HttpServletResponse response,
	    ThemeDisplay themeDisplay)
	    throws Exception {

		/*
		 * AuthTokenUtil .checkCSRFToken(request, VerifyEmailAddressAction.class
		 * .getName());
		 */

		String ticketKey = ParamUtil
		    .getString(request, "ticketKey");

		UserLocalServiceUtil
		    .verifyEmailAddress(ticketKey);
	}
}
