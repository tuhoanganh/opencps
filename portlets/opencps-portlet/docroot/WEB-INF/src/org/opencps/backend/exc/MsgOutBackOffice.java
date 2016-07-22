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

package org.opencps.backend.exc;

import java.util.ArrayList;
import java.util.List;

import org.opencps.backend.message.SendToBackOfficeMsg;
import org.opencps.dossiermgt.model.Dossier;
import org.opencps.dossiermgt.model.DossierFile;
import org.opencps.dossiermgt.model.DossierPart;
import org.opencps.dossiermgt.service.DossierFileLocalServiceUtil;
import org.opencps.dossiermgt.service.DossierLocalServiceUtil;
import org.opencps.dossiermgt.service.DossierPartLocalServiceUtil;
import org.opencps.jms.context.JMSContext;
import org.opencps.jms.message.SubmitDossierMessage;
import org.opencps.jms.message.SyncFromBackOfficeMessage;
import org.opencps.jms.message.body.DossierFileMsgBody;
import org.opencps.jms.message.body.SyncFromBackOfficeMsgBody;
import org.opencps.jms.util.JMSMessageBodyUtil;
import org.opencps.jms.util.JMSMessageUtil;
import org.opencps.processmgt.NoSuchWorkflowOutputException;
import org.opencps.processmgt.model.WorkflowOutput;
import org.opencps.processmgt.service.WorkflowOutputLocalServiceUtil;
import org.opencps.util.WebKeys;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageListener;
import com.liferay.portal.kernel.messaging.MessageListenerException;


/**
 * @author khoavd
 *
 */
public class MsgOutBackOffice implements MessageListener{

	/* (non-Javadoc)
     * @see com.liferay.portal.kernel.messaging.MessageListener#receive(com.liferay.portal.kernel.messaging.Message)
     */
    @Override
	public void receive(Message message)
	    throws MessageListenerException {

		try {
			System.out.println("DONE MSGOUT_BO");

			SendToBackOfficeMsg toBackOffice =
			    (SendToBackOfficeMsg) message.get("toBackOffice");
			
			Dossier dossier =
			    DossierLocalServiceUtil.fetchDossier(toBackOffice.getDossierId());
			
			List<WorkflowOutput> workflowOutputs =
						    WorkflowOutputLocalServiceUtil.getByProcessWFPostback(toBackOffice.getProcessWorkflowId(), true);
			
			List<DossierFile> dossierFiles = new ArrayList<DossierFile>();
			
			//Check file return
			if (workflowOutputs != null && !workflowOutputs.isEmpty()) {
				for (WorkflowOutput workflowOutput : workflowOutputs) {
					if (workflowOutput.getRequired()) {

						DossierFile dossierFile = null;
						try {
							DossierPart dossierPart =
								DossierPartLocalServiceUtil.getDossierPart(workflowOutput.getDossierPartId());
							dossierFile =
								DossierFileLocalServiceUtil.getDossierFileInUse(
									toBackOffice.getDossierId(), dossierPart.getDossierpartId());
							
							dossierFiles.add(dossierFile);
						}
						catch (Exception e) {
						}

					}
				}
			}
			else {
				throw new NoSuchWorkflowOutputException();
			}
			
			List<DossierFileMsgBody> lstDossierFileMsgBody =
			    JMSMessageBodyUtil.getDossierFileMsgBody(dossierFiles);

			JMSContext context =
			    JMSMessageUtil.createProducer(
			        toBackOffice.getCompanyId(),
			        toBackOffice.getGovAgencyCode(), true,
			        WebKeys.JMS_QUEUE_OPENCPS.toLowerCase(), "remote");

			SyncFromBackOfficeMessage syncFromBackoffice =
			    new SyncFromBackOfficeMessage(context);

			SyncFromBackOfficeMsgBody msgBody = new SyncFromBackOfficeMsgBody();

			msgBody.setOid(dossier.getOid());
			msgBody.setDossierStatus(toBackOffice.getDossierStatus());
			msgBody.setLstDossierFileMsgBody(lstDossierFileMsgBody);
			msgBody.setPaymentFile(toBackOffice.getPaymentFile());

			syncFromBackoffice.sendMessage(msgBody);

		}
		catch (Exception e) {
			_log.error(e);
		}

    }
    
    private Log _log = LogFactoryUtil.getLog(MsgOutBackOffice.class);

}
