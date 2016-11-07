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

	@Override
	public void receive(Message message)
		throws MessageListenerException {

		_doRecevie(message);

	}

	private void _doRecevie(Message message) {

		try {

			List<SendNotificationMessage> notifications =
				(List<SendNotificationMessage>) message.get(MessageBusKeys.Message.NOTIFICATIONS);

			String event = StringPool.BLANK;
			String group = StringPool.BLANK;
			String email = StringPool.BLANK;
			String phone = StringPool.BLANK;

			/*
			 * 1 notification message co the gui cho nhieu user, 1 user co the
			 * nhan notice theo nhieu kenh
			 */

			for (SendNotificationMessage item : notifications) {

				String sendType = item.getType(); 

				event = item.getNotificationEventName();

				List<InfoList> infoList = item.getInfoList();

				for (InfoList info : infoList) {

						if (sendType.contains(NotificationEventKeys.EMAIL)) {
							email = info.getUserMail();

							NotificationUtils.sendEmailNotification(item, email);

						}
						if (sendType.contains(NotificationEventKeys.INBOX)) {

							group = info.getGroup();

							int userId = Integer.parseInt(info.getUserId());

							JSONArray payloadJSON =
								NotificationUtils.createNotification(
									item, event, group, userId, true);

							NotificationUtils.addUserNotificationEvent(item, payloadJSON, userId);
						}
						if (sendType.contains(NotificationEventKeys.SMS)) {
							phone = info.getUserPhone();

						}
					
				}

			}

		}
		catch (Exception e) {
			_log.error(e);
		}
	}

}
