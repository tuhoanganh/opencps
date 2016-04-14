/**
 * OpenCPS is the open source Core Public Services software
 * Copyright (C) 2016-present OpenCPS community
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>
 */


package org.opencps.processmgt.util;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.RenderRequest;

import com.liferay.portal.model.Role;
import com.liferay.portal.model.RoleConstants;
import com.liferay.portal.service.RoleLocalServiceUtil;

/**
 * @author khoavd
 */
public class ProcessUtils {

	/**
	 * @param renderRequest
	 * @return
	 */
	public static List<Role> getRoles(RenderRequest renderRequest) {

		List<Role> roles = new ArrayList<Role>();
		try {
			roles =
			    RoleLocalServiceUtil.getTypeRoles(RoleConstants.TYPE_REGULAR);

		}
		catch (Exception e) {
			return new ArrayList<Role>();
		}

		return roles;
	}
}
