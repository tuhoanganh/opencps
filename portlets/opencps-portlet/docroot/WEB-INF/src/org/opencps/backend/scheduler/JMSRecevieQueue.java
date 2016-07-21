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

import java.util.Map;

import javax.jms.BytesMessage;
import javax.jms.ObjectMessage;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;

import org.opencps.jms.context.JMSContext;
import org.opencps.jms.message.SubmitDossierMessage;
import org.opencps.jms.message.body.DossierMsgBody;
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
public class JMSRecevieQueue implements MessageListener {

	@Override
	public void receive(Message message)
		throws MessageListenerException {

		long[] companyIds = PortalUtil.getCompanyIds();

		_log.info("********************************************************CompanyIds Length*********************************************** " +
			companyIds.length);

		long companyId = 0;

		if (companyIds != null && companyIds.length > 0) {
			for (int i = 0; i < companyIds.length; i++) {
				if (PortletUtil.checkJMSConfig(companyIds[i])) {
					companyId = companyIds[i];
					_log.info("********************************************************companyId*********************************************** " +
						companyId);
					break;
				}
			}
		}

		if (companyId > 0) {
			_log.info("Start create connection to JMS Queue..................");
			JMSContext context =
				JMSMessageUtil.createConsumer(
					companyId, StringPool.BLANK, true,
					WebKeys.JMS_QUEUE_OPENCPS.toLowerCase(), "local");
			try {
				int messageInQueue = context.countMessageInQueue();
				int receiveNumber = messageInQueue <= 50 ? messageInQueue : 50;

				_log.info("********************************************************Queue Size*********************************************** " +
					messageInQueue);

				int count = 1;
				while (count <= receiveNumber) {

					javax.jms.Message jsmMessage =
						context.getMessageConsumer().receive();
					if (jsmMessage != null) {
						if (jsmMessage instanceof TextMessage) {
							_log.info("*******************TextMessage*******************");
							_log.info(((TextMessage) jsmMessage).getText());
						}
						else if (jsmMessage instanceof ObjectMessage) {
							_log.info("*******************ObjectMessage*******************");
							_log.info(((ObjectMessage) jsmMessage).getClass().getName());
						}
						else if (jsmMessage instanceof BytesMessage) {
							BytesMessage bytesMessage =
								(BytesMessage) jsmMessage;
							_log.info("*******************BytesMessage*******************");
							_log.info(((BytesMessage) jsmMessage).getBodyLength());
							byte[] result =
								new byte[(int) bytesMessage.getBodyLength()];
							bytesMessage.readBytes(result);
							Object object =
								JMSMessageUtil.convertByteArrayToObject(result);
							if (object instanceof DossierMsgBody) {
								DossierMsgBody dossierMsgBody =
									(DossierMsgBody) object;
								SubmitDossierMessage submitDossierMessage =
									new SubmitDossierMessage(context);
								submitDossierMessage.receiveLocalMessage(dossierMsgBody);
							}
						}
						else if (jsmMessage instanceof StreamMessage) {
							_log.info("*******************StreamMessage*******************");
						}
					}
					else {
						_log.info("*******************Null Message*******************");
					}

					count++;
				}
			}
			catch (Exception e) {
				_log.error(e);
			}
			finally {
				try {
					context.destroy();
				}
				catch (Exception e) {
					_log.error(e);
				}

			}
		}
		else {
			_log.info("Cannot create connection to JMS Queue..................");
		}
	}

	private Log _log = LogFactoryUtil.getLog(JMSRecevieQueue.class.getName());
	
}
