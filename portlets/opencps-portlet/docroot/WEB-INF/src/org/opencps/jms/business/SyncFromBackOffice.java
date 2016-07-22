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

package org.opencps.jms.business;

import org.opencps.dossiermgt.NoSuchDossierException;
import org.opencps.dossiermgt.service.DossierLocalServiceUtil;
import org.opencps.jms.message.body.SyncFromBackOfficeMsgBody;

import com.liferay.portal.kernel.exception.SystemException;

/**
 * @author trungnt
 */
public class SyncFromBackOffice {

	/**
	 * @param syncFromBackOfficeMsgBody
	 * @throws SystemException
	 * @throws NoSuchDossierException
	 */
	public void syncDossier(SyncFromBackOfficeMsgBody syncFromBackOfficeMsgBody)
		throws NoSuchDossierException, SystemException {

		/*DossierLocalServiceUtil.updateDossierStatus(
			syncFromBackOfficeMsgBody.getOid(),
			syncFromBackOfficeMsgBody.getFileGroupId(),
			syncFromBackOfficeMsgBody.getDossierStatus(),
			syncFromBackOfficeMsgBody.getReceptionNo(),
			syncFromBackOfficeMsgBody.getEstimateDatetime(),
			syncFromBackOfficeMsgBody.getReceiveDatetime(),
			syncFromBackOfficeMsgBody.getFinishDatetime(),
			syncFromBackOfficeMsgBody.getActor(),
			syncFromBackOfficeMsgBody.getRequestCommand(),
			syncFromBackOfficeMsgBody.getActionInfo(),
			syncFromBackOfficeMsgBody.getMessageInfo());*/
	}
}
