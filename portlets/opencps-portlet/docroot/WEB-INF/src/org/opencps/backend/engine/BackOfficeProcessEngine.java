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

package org.opencps.backend.engine;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.opencps.backend.util.BackendUtils;
import org.opencps.dossiermgt.model.Dossier;
import org.opencps.processmgt.model.ProcessOrder;
import org.opencps.processmgt.service.ProcessOrderLocalServiceUtil;
import org.opencps.util.PortletConstants;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageListener;
import com.liferay.portal.kernel.messaging.MessageListenerException;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;

/**
 * @author khoavd
 */
public class BackOfficeProcessEngine implements MessageListener {

	/*
	 * (non-Javadoc)
	 * @see
	 * com.liferay.portal.kernel.messaging.MessageListener#receive(com.liferay
	 * .portal.kernel.messaging.Message)
	 */
	@Override
	public void receive(Message message)
	    throws MessageListenerException {

		doReceive(message);

	}

	private void doReceive(Message message) {

		long dossierId = GetterUtil.getLong(message.get("dossierId"));
		
		
		long fileGroupId = GetterUtil.getLong(message.get("fileGroupId"));

		long processOrderId = GetterUtil.getLong("processOrderId");

		long processWorkflowId =
		    GetterUtil.getLong(message.get("processWorkflowId"));
		
		long processStepId = GetterUtil.getLong(message.get("processStepId"));
		long actionUserId = GetterUtil.getLong(message.get("actionUserId"));
		long assignToUserId = GetterUtil.getLong(message.get("assignToUserId"));
		Date actionDatetime = GetterUtil.getDate(message.get("actionDatetime"), new SimpleDateFormat("dd/MM/yyyy : HH/mm"));
		String actionNote = GetterUtil.getString(message.get("actionNote"));
		
		long userId = GetterUtil.getLong(message.get("userId"));
		long groupId = GetterUtil.getLong(message.get("groupId"));
		long companyId = GetterUtil.getLong(message.get("companyId"));
		
		ProcessOrder processOrder = null;

		// Check ProcessOder

		processOrder = BackendUtils.getProcessOrder(dossierId, fileGroupId);

		// Neu phieu xl ko ton tai thi tao phieu xu ly moi
		if (Validator.isNull(processOrder)) {
			try {
				
				Dossier dossier = BackendUtils.getDossier(dossierId);
				
				long serviceInfoId = 0;
				long dossierTemplateId = 0;
				String govAgencyCode = StringPool.BLANK;
				String govAgencyName = StringPool.BLANK;
				long govAgencyOrganizationId = 0;
				long serviceProcessId = 0;
				
				if (Validator.isNotNull(dossier)) {
					serviceInfoId = dossier.getServiceInfoId();
					dossierTemplateId = dossier.getDossierTemplateId();
					govAgencyCode = dossier.getGovAgencyCode();
					govAgencyName = dossier.getGovAgencyName();
					govAgencyOrganizationId = dossier.getGovAgencyOrganizationId();
				}
				
				//processOrder = ProcessOrderLocalServiceUtil.initProcessOrder();
				
				processOrder =
				    ProcessOrderLocalServiceUtil.initProcessOrder(
				        userId, companyId, groupId, serviceInfoId,
				        dossierTemplateId, govAgencyCode, govAgencyName,
				        govAgencyOrganizationId, serviceProcessId, dossierId,
				        fileGroupId);
				
				processOrderId = processOrder.getProcessOrderId();
				
				// Gui thong bao cho kenh "pencps/backoffice/out/destination"
				
				Message msgToBOFUD = new Message();
				
				msgToBOFUD.put("processOrderId", processOrderId);
            }
            catch (Exception e) {
	            _log.error(e);
            }
			
			
		}
		
		if (Validator.isNotNull(processOrder)) {
			try {
				// Cap nhat phieu xu ly co trang thai System

				ProcessOrderLocalServiceUtil.updateProcessOrderStatus(
				    processOrderId, PortletConstants.DOSSIER_STATUS_SYSTEM);
			
				// Cap nhat trung gia tri cua phieu xu ly
				ProcessOrderLocalServiceUtil.updateProcessOrder(
				    processOrderId, processStepId, actionUserId, actionDatetime,
				    actionNote, assignToUserId);
            }
            catch (Exception e) {
	            _log.error(e);
            }
			

		}
		
		

	}
	
	private Log _log = LogFactoryUtil.getLog(BackOfficeProcessEngine.class);

}
