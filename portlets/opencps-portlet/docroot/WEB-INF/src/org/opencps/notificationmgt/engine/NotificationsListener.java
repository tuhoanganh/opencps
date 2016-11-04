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

import java.util.List;

import org.opencps.notificationmgt.message.SendNotificationMessage;
import org.opencps.notificationmgt.message.SendNotificationMessage.InfoList;
import org.opencps.notificationmgt.utils.NotificationEventKeys;
import org.opencps.notificationmgt.utils.NotificationUtils;
import org.opencps.util.MessageBusKeys;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageListener;
import com.liferay.portal.kernel.messaging.MessageListenerException;
import com.liferay.portal.kernel.util.StringPool;

/**
 * @author nhanhoang
 */

public class NotificationsListener implements MessageListener {

	private static Log _log = LogFactoryUtil.getLog(NotificationsListener.class);

	private static int YES = 1;
	private static int NO = 0;

	@Override
	public void receive(Message message)
		throws MessageListenerException {

		_doRecevie(message);

	}

	private void _doRecevie(Message message) {

		try {

			List<SendNotificationMessage> notifications =
				(List<SendNotificationMessage>) message.get(MessageBusKeys.Message.NOTIFICATIONS);

			String sendType = StringPool.BLANK;
			String event = StringPool.BLANK;
			String group = StringPool.BLANK;

			for (SendNotificationMessage item : notifications) {

				event = item.getNotificationEventName();
				group = item.getGroup();

				if (event.equals(NotificationEventKeys.OFFICIALS.EVENT1)) {

					List<InfoList> infoList = item.getInfoList();

					for (InfoList info : infoList) {

						int userId = Integer.parseInt(info.getUserId());

						JSONArray payloadJSON =
							NotificationUtils.createNotification(item, event, group, userId, true);

						if (sendType.equals(NotificationEventKeys.EMAIL)) {

						}
						if (sendType.equals(NotificationEventKeys.INBOX)) {

							NotificationUtils.addUserNotificationEvent(item, payloadJSON, userId);
						}
					}

				}

				if (event.equals(NotificationEventKeys.USERS_AND_ENTERPRISE.EVENT1)) {

					// JSONArray payloadJSON =
					// NotificationUtils.createNotification(notification,event,group,false);

					if (sendType.equals(NotificationEventKeys.EMAIL)) {

					}
					if (sendType.equals(NotificationEventKeys.INBOX)) {

						// NotificationUtils.addUserNotificationEvent(item,
						// payloadJSON,userId);
					}
				}

			}

		}
		catch (Exception e) {

		}
	}

}
