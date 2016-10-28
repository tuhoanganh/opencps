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
package org.opencps.holidayconfig.util;

import java.util.Calendar;
import java.util.Date;

import org.opencps.processmgt.model.ActionHistory;
import org.opencps.processmgt.model.ProcessStep;
import org.opencps.processmgt.model.ProcessWorkflow;
import org.opencps.processmgt.model.impl.ActionHistoryImpl;
import org.opencps.processmgt.model.impl.ProcessStepImpl;
import org.opencps.processmgt.model.impl.ProcessWorkflowImpl;
import org.opencps.processmgt.service.ActionHistoryLocalServiceUtil;
import org.opencps.processmgt.service.ProcessStepLocalServiceUtil;
import org.opencps.processmgt.service.ProcessWorkflowLocalServiceUtil;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Validator;

public class HolidayCheckUtils {

	private static Log _log = LogFactoryUtil.getLog(HolidayCheckUtils.class);

	/**
	 * @param startDate
	 * @param endDate
	 * @param daysDuration
	 * @return
	 */
	public static int checkActionDateOver(Date startDate, Date endDate,
			int daysDuration) {

		int dateOverNumbers = 0;

		if (daysDuration > 0) {

			Calendar endDayCal = Calendar.getInstance();
			endDayCal.setTime(endDate);

			Calendar endDateMax = HolidayUtils.getEndDate(startDate,
					daysDuration);

			int endDay = endDayCal.get(Calendar.DATE);
			int endDayMax = endDateMax.get(Calendar.DATE);

			dateOverNumbers = endDayMax - endDay;

			if (dateOverNumbers > 0) {
				return 0;
			} else {
				return Math.abs(dateOverNumbers);
			}
		}
		return dateOverNumbers;
	}

	/**
	 * @param processOrderId
	 * @param latestProcessWorkflowId
	 * @param preProcessWorkflowId
	 * @return
	 * @throws PortalException
	 */
	public static int getDayDelay(long processOrderId,
			long latestProcessWorkflowId, long preProcessWorkflowId)
			throws PortalException,SystemException {

		ActionHistory preActionHistory = new ActionHistoryImpl();

		ActionHistory latestActionHistory = new ActionHistoryImpl();

		ProcessWorkflow processWorkflow = new ProcessWorkflowImpl();

		ProcessStep processStep = new ProcessStepImpl();

		int dayDelay = 0;

		try {
			if (processOrderId > 0 && latestProcessWorkflowId > 0
					&& preProcessWorkflowId > 0) {

				preActionHistory = ActionHistoryLocalServiceUtil
						.getLatestActionHistory(processOrderId,
								preProcessWorkflowId, false);

				latestActionHistory = ActionHistoryLocalServiceUtil
						.getLatestActionHistory(processOrderId,
								latestProcessWorkflowId, false);

				if (Validator.isNotNull(preActionHistory.getCreateDate())
						&& Validator.isNotNull(latestActionHistory
								.getCreateDate())) {

					processWorkflow = ProcessWorkflowLocalServiceUtil
							.getProcessWorkflow(latestActionHistory
									.getProcessWorkflowId());

					if (processWorkflow.getPostProcessStepId() > 0) {
						processStep = ProcessStepLocalServiceUtil
								.getProcessStep(processWorkflow
										.getPostProcessStepId());

						if (processStep.getDaysDuration() > 0) {
							dayDelay = checkActionDateOver(
									preActionHistory.getCreateDate(),
									latestActionHistory.getCreateDate(),
									processStep.getDaysDuration());
						}
					}
				}
			}
		} catch (PortalException e) {
			// TODO Auto-generated catch block
			_log.error(e);
		}

		return dayDelay;

	}

}