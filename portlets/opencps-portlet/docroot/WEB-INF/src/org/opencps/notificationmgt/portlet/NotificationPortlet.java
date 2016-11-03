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

package org.opencps.notificationmgt.portlet;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;

import org.opencps.notificationmgt.engine.UserNotificationHandler;
import org.opencps.notificationmgt.utils.NotificationEventKeys;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.model.User;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.service.UserNotificationEventLocalServiceUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

public class NotificationPortlet extends MVCPortlet {

	private static Log _log = LogFactoryUtil.getLog(NotificationPortlet.class);

	public void sendUserNotification(ActionRequest actionRequest, ActionResponse actionResponse)
		throws IOException, PortletException, PortalException, SystemException {
		
		ServiceContext serviceContext =
						ServiceContextFactory.getInstance(actionRequest);
		
		Message message = new Message();
		
//		UserActionMsg actionMsg = new UserActionMsg();
//		
//		//actionMsg.setAction(WebKeys.ACTION_CANCEL_VALUE);
//
////		actionMsg.setDossierId(dossierId);
////
////		actionMsg.setLocale(serviceContext.getLocale());
////
////		actionMsg.setUserId(serviceContext.getUserId());
////
////		actionMsg.setGroupId(serviceContext.getScopeGroupId());
////
////		actionMsg.setGovAgencyCode(dossier.getGovAgencyCode());
////
////		actionMsg.setCompanyId(dossier.getCompanyId());
		
//		message.put(DestinationKeys.MessageName.NOTIFICATIONS, actionMsg);
//		
//		MessageBusUtil.sendMessage(
//			DestinationKeys.Destination.NOTIFICATIONS, message);

		try {

			List<User> users =
				UserLocalServiceUtil.getUsers(0, UserLocalServiceUtil.getUsersCount());
			
			//_log.info("users:"+users);
			
			String notificationText = ParamUtil.getString(actionRequest, "notifciationText");
			
			String[] notificationType = StringUtil.split(notificationText, ",");
			
			for(int i=0;i<notificationType.length;i++){
				//notificationType.
			}
			
			for (User user : users) {

				JSONObject payloadJSON = JSONFactoryUtil.createJSONObject();
				payloadJSON.put("title", "title");
				payloadJSON.put("notificationText", notificationText);
				payloadJSON.put("friendlyType", NotificationEventKeys.GROUP1);

				UserNotificationEventLocalServiceUtil.addUserNotificationEvent(
					user.getUserId(), UserNotificationHandler.PORTLET_ID,
					(new Date()).getTime(), user.getUserId(), payloadJSON.toString(), false,
					serviceContext);
			}
		}
		catch (Exception e) {
			e.printStackTrace();

		}
	}

}
