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

package org.opencps.backend.util;

import org.opencps.processmgt.model.ProcessOrder;
import org.opencps.processmgt.service.ProcessOrderLocalServiceUtil;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;


/**
 * @author khoavd
 *
 */
public class BackendUtils {
	

	/**
	 * Get ProcessOrder
	 * 
	 * @param dossierId
	 * @param fileGroupId
	 * @return
	 */
	public static ProcessOrder getProcessOrder(long dossierId, long fileGroupId) {

		ProcessOrder order = null;

		try {
			order =
			    ProcessOrderLocalServiceUtil.getProcessOrder(
			        dossierId, fileGroupId);
		}
		catch (Exception e) {
			_log.error(e);
		}

		return order;
	}
	
	private static Log _log = LogFactoryUtil.getLog(BackendUtils.class);
}
