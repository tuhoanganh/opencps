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
package org.opencps.notificationmgt.engine;

import net.sf.jasperreports.engine.util.MessageUtil;

import org.opencps.notificationmgt.message.SendNotificationMessage;
import org.opencps.notificationmgt.utils.NotificationUtils;
import org.opencps.util.MessageBusKeys;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageListener;
import com.liferay.portal.kernel.messaging.MessageListenerException;

public class NotificationsListener implements MessageListener {

	private static Log _log = LogFactoryUtil.getLog(NotificationsListener.class);

	@Override
	public void receive(Message message)
		throws MessageListenerException {

		_doRecevie(message);

	}

	private void _doRecevie(Message message) {

		try {
			
			SendNotificationMessage sendNotification =
				(SendNotificationMessage) message.get(MessageBusKeys.Message.NOTIFICATIONS);
			
			NotificationUtils.pushNotification(message);

//			User sendToUser = UserLocalServiceUtil.getUser(sendNotification.getUserId());
//
//			JSONObject payloadJSON = JSONFactoryUtil.createJSONObject();
//			payloadJSON.put("userId", sendNotification.getUserId());
//			payloadJSON.put("notificationText", sendNotification.getContent());
//
//			UserNotificationEventLocalServiceUtil.addUserNotificationEvent(
//				sendToUser.getUserId(), "0",
//				(new Date()).getTime(), sendNotification.getUserId(), payloadJSON.toString(),
//				false, null);
		}
		catch (Exception e) {

		}
	}

}
