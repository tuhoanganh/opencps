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

import javax.jms.BytesMessage;
import javax.jms.ObjectMessage;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;

import org.opencps.jms.context.JMSContext;
import org.opencps.jms.message.SyncFromBackOfficeMessage;
import org.opencps.jms.message.body.SyncFromBackOfficeMsgBody;
import org.opencps.jms.util.JMSMessageUtil;
import org.opencps.util.PortletUtil;
import org.opencps.util.WebKeys;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageListener;
import com.liferay.portal.kernel.messaging.MessageListenerException;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.util.PortalUtil;

/**
 * @author khoavd
 */
public class MsgInFrontOffice implements MessageListener {

	/*
	 * (non-Javadoc)
	 * @see
	 * com.liferay.portal.kernel.messaging.MessageListener#receive(com.liferay
	 * .portal.kernel.messaging.Message)
	 */
	@Override
	public void receive(Message message)
		throws MessageListenerException {

		_doReceive(message);
	}

	private void _doReceive(Message message) {

		System.out.println("**DO msgInFrontOffice");

		long[] companyIds = PortalUtil.getCompanyIds();


		long companyId = 0;

		if (companyIds != null && companyIds.length > 0) {
			for (int i = 0; i < companyIds.length; i++) {
				if (PortletUtil.checkJMSConfig(companyIds[i])) {
					companyId = companyIds[i];
					break;
				}
			}
		}

		if (companyId > 0) {
			JMSContext context =
				JMSMessageUtil.createConsumer(
					companyId, StringPool.BLANK, true,
					WebKeys.JMS_QUEUE_OPENCPS_FRONTOFFICE.toLowerCase(), "local");
			try {
				//int messageInQueue = context.countMessageInQueue();
				//int receiveNumber = messageInQueue <= 50 ? messageInQueue : 50;
				
				int receiveNumber = 50;

				/*_log.info("********************************************************Queue Size*********************************************** " +
					messageInQueue);*/

				int count = 1;
				while (count <= receiveNumber) {

					javax.jms.Message jsmMessage =
						context.getMessageConsumer().receive(1000);
					if (jsmMessage != null) {
						if (jsmMessage instanceof TextMessage) {
						}
						else if (jsmMessage instanceof ObjectMessage) {
						}
						else if (jsmMessage instanceof BytesMessage) {
							BytesMessage bytesMessage =
								(BytesMessage) jsmMessage;
							_log.info("*******************BytesMessage*******************");
							byte[] result =
								new byte[(int) bytesMessage.getBodyLength()];
							bytesMessage.readBytes(result);
							Object object =
								JMSMessageUtil.convertByteArrayToObject(result);
							if (object instanceof SyncFromBackOfficeMsgBody) {
								SyncFromBackOfficeMsgBody syncFromBackOfficeMsgBody =
									(SyncFromBackOfficeMsgBody) object;
								SyncFromBackOfficeMessage syncFromBackOfficeMessage =
									new SyncFromBackOfficeMessage(context);
								syncFromBackOfficeMessage.receiveLocalMessage(syncFromBackOfficeMsgBody);

							}
						}
						else if (jsmMessage instanceof StreamMessage) {
						}
					}
					else {
					}

					count++;
				}
			}
			catch (Exception e) {
				_log.error(e);
			}
			
		}
		else {
			_log.info("Cannot create connection to JMS Queue..................");
		}

	}
	private Log _log = LogFactoryUtil.getLog(MsgInFrontOffice.class);
}
