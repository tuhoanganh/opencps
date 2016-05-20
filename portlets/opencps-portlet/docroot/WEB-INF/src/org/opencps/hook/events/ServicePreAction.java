/**
* OpenCPS is the open source Core Public Services software
* Copyright (C) 2016-present OpenCPS community

* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU Affero General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* any later version.

* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU Affero General Public License for more details.
* You should have received a copy of the GNU Affero General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>
*/
package org.opencps.hook.events;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.opencps.accountmgt.model.Business;
import org.opencps.accountmgt.model.Citizen;
import org.opencps.accountmgt.service.BusinessLocalServiceUtil;
import org.opencps.accountmgt.service.CitizenLocalServiceUtil;
import org.opencps.usermgt.model.Employee;
import org.opencps.usermgt.service.EmployeeLocalServiceUtil;
import org.opencps.util.PortletPropsValues;
import org.opencps.util.WebKeys;

import com.liferay.portal.kernel.events.Action;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroup;
import com.liferay.portal.theme.ThemeDisplay;

/**
 * @author trungnt
 */
public class ServicePreAction extends Action {

	@Override
	public void run(HttpServletRequest request, HttpServletResponse response)
	    throws ActionException {

		getAccount(request, response);

	}

	protected void getAccount(
	    HttpServletRequest request, HttpServletResponse response) {

		try {
			initAccount(request, response);

		}
		catch (Exception e) {
			// TODO: handle exception
		}

	}

	public void initAccount(
	    HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay) request
		    .getAttribute(WebKeys.THEME_DISPLAY);
		String accountType = StringPool.BLANK;
		if (themeDisplay
		    .isSignedIn()) {

			List<UserGroup> userGroups = new ArrayList<UserGroup>();

			User user = themeDisplay
			    .getUser();
			userGroups = user
			    .getUserGroups();

			if (!userGroups
			    .isEmpty()) {
				for (UserGroup userGroup : userGroups) {
					if (userGroup
					    .getName().equals(
					        PortletPropsValues.USERMGT_USERGROUP_NAME_CITIZEN) ||
					    userGroup
					        .getName().equals(
					            PortletPropsValues.USERMGT_USERGROUP_NAME_BUSINESS) ||
					    userGroup
					        .getName().equals(
					            PortletPropsValues.USERMGT_USERGROUP_NAME_EMPLOYEE)) {
						accountType = userGroup
						    .getName();
						break;
					}

				}

				request
				    .setAttribute(WebKeys.ACCOUNT_TYPE, accountType);
			}

			if (accountType
			    .equals(PortletPropsValues.USERMGT_USERGROUP_NAME_CITIZEN)) {
				Citizen citizen = CitizenLocalServiceUtil
				    .getCitizen(user
				        .getUserId());
				request
				    .setAttribute(WebKeys.CITIZEN_ENTRY, citizen);
			}
			else if (accountType
			    .equals(PortletPropsValues.USERMGT_USERGROUP_NAME_BUSINESS)) {

				Business business = BusinessLocalServiceUtil
				    .getBusiness(user
				        .getUserId());
				request
				    .setAttribute(WebKeys.BUSINESS_ENTRY, business);
			}
			else if (accountType
			    .equals(PortletPropsValues.USERMGT_USERGROUP_NAME_EMPLOYEE)) {
				Employee employee = EmployeeLocalServiceUtil
				    .getEmployeeByMappingUserId(themeDisplay
				        .getScopeGroupId(), user
				            .getUserId());

				request
				    .setAttribute(WebKeys.EMPLOYEE_ENTRY, employee);
			}

		}

	}

}
