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

import javax.portlet.ActionRequest;
import javax.portlet.PortletURL;
import javax.portlet.WindowState;

import org.opencps.notificationmgt.utils.NotificationEventKeys;
import org.opencps.util.WebKeys;

import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.notifications.BaseUserNotificationHandler;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.LiferayPortletURL;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.model.UserNotificationEvent;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;

public class UserNotificationHandler extends BaseUserNotificationHandler {

	private static Log _log = LogFactoryUtil.getLog(UserNotificationHandler.class);

	public static final String PORTLET_ID = "2_WAR_notificationsportlet";
	

	public UserNotificationHandler() {

		setPortletId(UserNotificationHandler.PORTLET_ID);

	}

	@Override
	protected String getBody(
		UserNotificationEvent userNotificationEvent, ServiceContext serviceContext)
		throws Exception {

		JSONObject jsonObject =
			JSONFactoryUtil.createJSONObject(userNotificationEvent.getPayload());

		String title = jsonObject.getString("title");
		String bodyText = jsonObject.getString("notificationText");

		LiferayPortletResponse liferayPortletResponse = serviceContext.getLiferayPortletResponse();

		PortletURL redirectUrl =
			liferayPortletResponse.createActionURL(UserNotificationHandler.PORTLET_ID);

		redirectUrl.setParameter(ActionRequest.ACTION_NAME, "doSomethingGood");
		redirectUrl.setParameter("dossierId", jsonObject.getString("dossierId"));
		redirectUrl.setParameter("userNotificationEventId",String.valueOf(userNotificationEvent.getUserNotificationEventId()));
		redirectUrl.setWindowState(WindowState.NORMAL);


		String body = StringUtil.replace(getBodyTemplate(), new String[] {
			"[$TITLE$]", "[$BODY_TEXT$]"
		}, new String[] {
			title, bodyText
		});

		return body;
	}

	protected String getBodyTemplate() throws Exception {
		StringBundler sb = new StringBundler(5);
		sb.append("<div class=\"title\">[$TITLE$]</div> ");
		sb.append("<div class=\"body\">[$BODY_TEXT$]</div>");
		return sb.toString();
	}
	
	@Override
	protected String getLink(UserNotificationEvent userNotificationEvent,
			ServiceContext serviceContext) throws Exception {
 
		JSONObject jsonObject = JSONFactoryUtil
				.createJSONObject(userNotificationEvent.getPayload());
		
		LiferayPortletResponse liferayPortletResponse = serviceContext
				.getLiferayPortletResponse();
		
		LiferayPortletURL viewURL = null;
		
		String friendlyType = jsonObject.getString("friendlyType");
		long plId = 0;
		
		if(friendlyType.equals(NotificationEventKeys.GROUP1)){
			
			plId = LayoutLocalServiceUtil.getFriendlyURLLayout(20182, true, friendlyType).getPlid();
			
			_log.info("plId:"+plId);
			
//			long processOrderId = 17003;
			
			
			viewURL = liferayPortletResponse.createRenderURL(WebKeys.PROCESS_ORDER_PORTLET);;
			viewURL.setParameter("mvcPath", "/html/portlets/processmgt/processorder/process_order_detail.jsp");
			viewURL.setParameter("processOrderId", jsonObject.getString("processOrderId"));
			viewURL.setParameter("backURL", "/group/guest/"+String.valueOf(friendlyType));
			viewURL.setParameter("isEditDossier", String.valueOf(true));
			viewURL.setPlid(plId);
			viewURL.setWindowState(WindowState.NORMAL);
			
		}else if(friendlyType.equals(NotificationEventKeys.GROUP2)){
			
		}
		return viewURL.toString();
	}
}
