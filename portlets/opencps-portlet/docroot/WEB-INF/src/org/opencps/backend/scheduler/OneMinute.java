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

package org.opencps.backend.scheduler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.opencps.backend.message.SendToEngineMsg;
import org.opencps.backend.util.BackendUtils;
import org.opencps.backend.util.PaymentRequestGenerator;
import org.opencps.dossiermgt.model.Dossier;
import org.opencps.dossiermgt.model.impl.DossierImpl;
import org.opencps.dossiermgt.service.DossierLocalServiceUtil;
import org.opencps.processmgt.model.ProcessOrder;
import org.opencps.processmgt.model.ProcessWorkflow;
import org.opencps.processmgt.model.SchedulerJobs;
import org.opencps.processmgt.model.impl.ProcessOrderImpl;
import org.opencps.processmgt.model.impl.ProcessWorkflowImpl;
import org.opencps.processmgt.service.ProcessOrderLocalServiceUtil;
import org.opencps.processmgt.service.ProcessWorkflowLocalServiceUtil;
import org.opencps.processmgt.service.SchedulerJobsLocalServiceUtil;

import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.messaging.MessageListener;
import com.liferay.portal.kernel.messaging.MessageListenerException;


/**
 * @author khoavd
 *
 */
public class OneMinute implements MessageListener{

	/* (non-Javadoc)
     * @see com.liferay.portal.kernel.messaging.MessageListener#receive(com.liferay.portal.kernel.messaging.Message)
     */
    @Override
    public void receive(Message message)
        throws MessageListenerException {
    	
    	List<SchedulerJobs> schedulerJobs = new ArrayList<SchedulerJobs>();
    	
    	try {
    		schedulerJobs = SchedulerJobsLocalServiceUtil.getSchedulerJobs(1);
        }
        catch (Exception e) {
	        // TODO: handle exception
        }
    					
		for (SchedulerJobs schJob : schedulerJobs) {
			boolean conditionCheck =
			    BackendUtils.checkPreCondition(
			        schJob.getShedulerPattern(), schJob.getDossierId());

			if (conditionCheck) {

				Date now = new Date();

				Dossier dossier = new DossierImpl(); 
				ProcessWorkflow processWorkflow = new ProcessWorkflowImpl();
				ProcessOrder processOrder = new ProcessOrderImpl();

				try {
					dossier = DossierLocalServiceUtil.getDossier(schJob.getDossierId());
					processWorkflow = ProcessWorkflowLocalServiceUtil.fetchProcessWorkflow(schJob.getProcessWorkflowId());
					processOrder = ProcessOrderLocalServiceUtil.getProcessOrder(schJob.getDossierId(), schJob.getFileGroupId());

                }
                catch (Exception e) {
                }


				SendToEngineMsg engineMsg = new SendToEngineMsg();

				engineMsg.setUserId(0);
				engineMsg.setGroupId(0);
				engineMsg.setCompanyId(0);
				engineMsg.setActionDatetime(now);
				engineMsg.setAction(BackendUtils.buildActionName(schJob.getShedulerPattern()));
				engineMsg.setSignature(0);
				engineMsg.setPaymentValue(PaymentRequestGenerator.getTotalPayment(processWorkflow.getPaymentFee()));
				engineMsg.setEstimateDatetime(now);
				engineMsg.setReceptionNo(dossier.getReceptionNo());
				engineMsg.setAssignToUserId(processWorkflow.getActionUserId());
				engineMsg.setActionNote("Auto event");
				engineMsg.setActionUserId(processWorkflow.getActionUserId());
				engineMsg.setProcessWorkflowId(processWorkflow.getProcessWorkflowId());
				engineMsg.setProcessOrderId(processOrder.getProcessOrderId());
				engineMsg.setDossierId(schJob.getDossierId());
				engineMsg.setFileGroupId(schJob.getFileGroupId());
				
				Message msg = new Message();
				
				msg.put("msgToEngine", engineMsg);
				
				MessageBusUtil.sendMessage("opencps/backoffice/engine/destination", msg);
			}
		}
		
		
	    
    }
    

}
