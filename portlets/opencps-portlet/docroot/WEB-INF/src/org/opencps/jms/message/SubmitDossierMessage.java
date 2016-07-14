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

package org.opencps.jms.message;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.naming.NamingException;

import org.opencps.dossiermgt.model.Dossier;
import org.opencps.jms.context.JMSContext;
import org.opencps.jms.message.body.DossierMsgBody;
import org.opencps.jms.util.JMSMessageBodyUtil;
import org.opencps.jms.util.JMSMessageUtil;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

/**
 * @author trungnt
 */
public class SubmitDossierMessage {

	public SubmitDossierMessage(JMSContext context, Dossier dossier) {

		this.setContext(context);

	}

	public void sendMessage(long dossierId)
		throws JMSException, NamingException {

		try {
			BytesMessage bytesMessage =
				JMSMessageUtil.createByteMessage(_context);
			DossierMsgBody dossierMsgBody =
				JMSMessageBodyUtil.getDossierMsgBody(dossierId);
			byte[] sender =
				JMSMessageUtil.convertObjectToByteArray(dossierMsgBody);

			bytesMessage.writeBytes(sender);

			_context.getMessageProducer().send(bytesMessage);

		}
		catch (Exception e) {
			_log.error(e);
		}
		finally {
			_context.destroy();
		}
	}

	/**
	 * @param dossier
	 * @throws NamingException
	 * @throws JMSException
	 */
	public void sendMessage(Dossier dossier)
		throws JMSException, NamingException {

		try {
			BytesMessage bytesMessage =
				JMSMessageUtil.createByteMessage(_context);
			DossierMsgBody dossierMsgBody =
				JMSMessageBodyUtil.getDossierMsgBody(dossier);
			byte[] sender =
				JMSMessageUtil.convertObjectToByteArray(dossierMsgBody);

			bytesMessage.writeBytes(sender);

			_context.getMessageProducer().send(bytesMessage);

			_context.destroy();

		}
		catch (Exception e) {
			_log.error(e);
		}
		finally {
			_context.destroy();
		}
	}

	/**
	 * @throws JMSException
	 * @throws NamingException
	 */
	public void receiveMessage()
		throws JMSException, NamingException {

		try {
			BytesMessage bytesMessage =
				(BytesMessage) _context.getMessageConsumer().receive();

			byte[] result = new byte[(int) bytesMessage.getBodyLength()];

			bytesMessage.readBytes(result);

			Object object = JMSMessageUtil.convertByteArrayToObject(result);

			DossierMsgBody dossierMsgBody = (DossierMsgBody) object;
			setDossierMsgBody(dossierMsgBody);

		}
		catch (Exception e) {
			_log.error(e);
		}
		finally {
			_context.destroy();
		}
	}

	public JMSContext getContext() {

		return _context;
	}

	public void setContext(JMSContext context) {

		this._context = context;
	}

	public DossierMsgBody getDossierMsgBody() {

		return _dossierMsgBody;
	}

	public void setDossierMsgBody(DossierMsgBody dossierMsgBody) {

		this._dossierMsgBody = dossierMsgBody;
	}

	private JMSContext _context;
	private DossierMsgBody _dossierMsgBody;

	private Log _log =
		LogFactoryUtil.getLog(SubmitDossierMessage.class.getName());
}
