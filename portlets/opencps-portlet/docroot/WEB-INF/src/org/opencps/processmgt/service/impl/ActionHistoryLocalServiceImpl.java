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

package org.opencps.processmgt.service.impl;

import java.util.Date;

import org.opencps.processmgt.model.ActionHistory;
import org.opencps.processmgt.service.base.ActionHistoryLocalServiceBaseImpl;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.service.ServiceContext;

/**
 * The implementation of the action history local service. <p> All custom
 * service methods should be put in this class. Whenever methods are added,
 * rerun ServiceBuilder to copy their definitions into the
 * {@link org.opencps.processmgt.service.ActionHistoryLocalService} interface.
 * <p> This is a local service. Methods of this service will not have security
 * checks based on the propagated JAAS credentials because this service can only
 * be accessed from within the same VM. </p>
 *
 * @author khoavd
 * @author trungnt
 * @see org.opencps.processmgt.service.base.ActionHistoryLocalServiceBaseImpl
 * @see org.opencps.processmgt.service.ActionHistoryLocalServiceUtil
 */
public class ActionHistoryLocalServiceImpl
    extends ActionHistoryLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS: Never reference this interface directly. Always use
	 * {@link org.opencps.processmgt.service.ActionHistoryLocalServiceUtil} to
	 * access the action history local service.
	 */

	public ActionHistory addActionHistory(
	    long processOrderId, long processWorkflowId, Date actionDatetime,
	    String stepName, String actionName, String actionNote,
	    long actionUserId, int daysDoing, int daysDelay,
	    ServiceContext serviceContext)
	    throws SystemException {

		long actionHistoryId = counterLocalService
		    .increment(ActionHistory.class
		        .getName());
		ActionHistory actionHistory = actionHistoryPersistence
		    .create(actionHistoryId);

		Date now = new Date();

		actionHistory
		    .setUserId(serviceContext
		        .getUserId());
		actionHistory
		    .setGroupId(serviceContext
		        .getScopeGroupId());
		actionHistory
		    .setCompanyId(actionHistory
		        .getCompanyId());
		actionHistory
		    .setCreateDate(now);
		actionHistory
		    .setModifiedDate(now);

		actionHistory
		    .setProcessOrderId(processOrderId);
		actionHistory
		    .setProcessWorkflowId(processWorkflowId);
		actionHistory
		    .setActionDatetime(actionDatetime);
		actionHistory
		    .setStepName(stepName);
		actionHistory
		    .setActionName(actionName);
		actionHistory
		    .setActionNote(actionNote);
		actionHistory
		    .setActionUserId(actionUserId);
		actionHistory
		    .setDaysDoing(daysDoing);
		actionHistory
		    .setDaysDelay(daysDelay);
		return actionHistoryPersistence.update(actionHistory);
	}

}
