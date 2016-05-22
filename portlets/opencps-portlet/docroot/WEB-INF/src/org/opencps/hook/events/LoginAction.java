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

import org.opencps.accountmgt.service.BusinessLocalServiceUtil;
import org.opencps.accountmgt.service.CitizenLocalServiceUtil;
import org.opencps.usermgt.service.EmployeeLocalServiceUtil;
import org.opencps.util.PortletPropsValues;
import org.opencps.util.WebKeys;

import com.liferay.portal.kernel.events.Action;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroup;
import com.liferay.portal.theme.ThemeDisplay;

/**
 * @author trungnt
 *
 */
public class LoginAction extends Action {

	@Override
	public void run(HttpServletRequest request, HttpServletResponse response)
	    throws ActionException {

		ThemeDisplay themeDisplay = (ThemeDisplay) request
		    .getAttribute(WebKeys.THEME_DISPLAY);
		String accountType = GetterUtil
		    .getString(request
		        .getAttribute(WebKeys.ACCOUNT_TYPE));
		if (themeDisplay
		    .isSignedIn() && Validator
		        .isNull(accountType)) {

			List<UserGroup> userGroups = new ArrayList<UserGroup>();

			try {
				User user = themeDisplay
				    .getUser();
				userGroups = user
				    .getUserGroups();

				if (!userGroups
				    .isEmpty()) {
					for (UserGroup userGroup : userGroups) {
						System.out
						    .println(userGroup
						        .getName() +
						        "-------------------------------->");
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
				    .equals(
				        PortletPropsValues.USERMGT_USERGROUP_NAME_CITIZEN)) {
					Object object = CitizenLocalServiceUtil
					    .getCitizen(user
					        .getUserId());
					request
					    .setAttribute(WebKeys.CITIZEN_ENTRY, object);
				}
				else if (accountType
				    .equals(
				        PortletPropsValues.USERMGT_USERGROUP_NAME_BUSINESS)) {

					Object object = BusinessLocalServiceUtil
					    .getBusiness(user
					        .getUserId());
					request
					    .setAttribute(WebKeys.BUSINESS_ENTRY, object);
				}
				else if (accountType
				    .equals(
				        PortletPropsValues.USERMGT_USERGROUP_NAME_EMPLOYEE)) {
					Object object = EmployeeLocalServiceUtil
					    .getEmployeeByMappingUserId(themeDisplay
					        .getScopeGroupId(), user
					            .getUserId());

					request
					    .setAttribute(WebKeys.EMPLOYEE_ENTRY, object);
				}
			}
			catch (Exception e) {
				_log
				    .error(e);

			}

		}
	}

	private static Log _log = LogFactoryUtil
	    .getLog(LoginAction.class);
}
