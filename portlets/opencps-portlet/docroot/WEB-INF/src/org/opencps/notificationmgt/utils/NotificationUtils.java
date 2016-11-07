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

package org.opencps.notificationmgt.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.portlet.WindowState;

import org.opencps.backend.message.SendToBackOfficeMsg;
import org.opencps.dossiermgt.search.DossierDisplayTerms;
import org.opencps.notificationmgt.engine.UserNotificationHandler;
import org.opencps.notificationmgt.message.SendNotificationMessage;
import org.opencps.paymentmgt.model.PaymentFile;
import org.opencps.paymentmgt.search.PaymentFileDisplayTerms;
import org.opencps.paymentmgt.service.PaymentFileLocalServiceUtil;
import org.opencps.processmgt.model.ProcessOrder;
import org.opencps.processmgt.search.ProcessOrderDisplayTerms;
import org.opencps.processmgt.service.ProcessOrderLocalServiceUtil;
import org.opencps.util.MessageBusKeys;
import org.opencps.util.PortletConstants;
import org.opencps.util.PortletPropsValues;
import org.opencps.util.SendMailUtils;
import org.opencps.util.WebKeys;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.LiferayPortletURL;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserNotificationEventLocalServiceUtil;

/**
 * @author nhanhoang
 */

public class NotificationUtils {

	private static Log _log = LogFactoryUtil.getLog(NotificationUtils.class);

	public static void addUserNotificationEvent(
		SendNotificationMessage message, JSONArray payloadJSON, long userIdDelivery) {

		try {

			ServiceContext serviceContext = new ServiceContext();

			UserNotificationEventLocalServiceUtil.addUserNotificationEvent(
				userIdDelivery, UserNotificationHandler.PORTLET_ID, (new Date()).getTime(), 0,
				payloadJSON.toString(), false, serviceContext);

		}
		catch (Exception e) {
			_log.error(e);
		}
	}

	public static JSONArray createNotification(
		SendNotificationMessage message, String event, String group, long userIdDelivery,
		boolean privatePage) {

		JSONArray payloadJSONArray = JSONFactoryUtil.createJSONArray();
		JSONObject payloadJSONObject = JSONFactoryUtil.createJSONObject();
		Locale locale = new Locale("vi", "VN");

		try {

			ProcessOrder processOrder =
				ProcessOrderLocalServiceUtil.getProcessOrder(message.getProcessOrderId());

			String title = StringPool.BLANK;
			title = LanguageUtil.get(locale, event) + "[" + processOrder.getDossierId() + "]";

			long plId = 0;

			ServiceContext serviceContext = new ServiceContext();
			LiferayPortletResponse liferayPortletResponse =
				serviceContext.getLiferayPortletResponse();

			if (group.equals(NotificationEventKeys.GROUP1)) {

				plId = LayoutLocalServiceUtil.getFriendlyURLLayout(20182, true, group).getPlid();

				LiferayPortletURL viewURL =
					liferayPortletResponse.createRenderURL(WebKeys.PROCESS_ORDER_PORTLET);
				viewURL.setParameter(
					"mvcPath", "/html/portlets/processmgt/processorder/process_order_detail.jsp");
				viewURL.setParameter(
					ProcessOrderDisplayTerms.PROCESS_ORDER_ID,
					String.valueOf(message.getProcessOrderId()));
				viewURL.setParameter(WebKeys.BACK_URL, "/group/guest" + group);
				viewURL.setParameter("isEditDossier", String.valueOf(true));
				viewURL.setPlid(plId);
				viewURL.setWindowState(WindowState.NORMAL);

				payloadJSONObject.put("processOrderId", message.getProcessOrderId());
				payloadJSONObject.put("linkTo", viewURL.toString());

			}
			else if (group.equals(NotificationEventKeys.GROUP2)) {
				plId = LayoutLocalServiceUtil.getFriendlyURLLayout(20182, false, group).getPlid();

				LiferayPortletURL viewURL =
					liferayPortletResponse.createRenderURL(WebKeys.DOSSIER_MGT_PORTLET);
				viewURL.setParameter(
					"mvcPath", "/html/portlets/dossiermgt/frontoffice/edit_dossier.jsp");
				viewURL.setParameter(
					DossierDisplayTerms.DOSSIER_ID, String.valueOf(processOrder.getDossierId()));
				viewURL.setParameter(Constants.CMD, Constants.VIEW);
				viewURL.setParameter(WebKeys.BACK_URL, "/group/guest/");
				viewURL.setParameter(WebKeys.REDIRECT_URL, "/group/guest/");
				viewURL.setParameter("isEditDossier", String.valueOf(false));
				viewURL.setPlid(plId);
				viewURL.setWindowState(WindowState.NORMAL);

				payloadJSONObject.put("dossierId", message.getDossierId());
				payloadJSONObject.put("linkTo", viewURL.toString());

			}
			else if (group.equals(NotificationEventKeys.GROUP3)) {

				plId = LayoutLocalServiceUtil.getFriendlyURLLayout(20182, true, group).getPlid();

				PaymentFile paymentFile =
					PaymentFileLocalServiceUtil.getPaymentFile(processOrder.getDossierId());

				LiferayPortletURL viewURL =
					liferayPortletResponse.createRenderURL(WebKeys.PAYMENT_MGT_PORTLET);
				viewURL.setParameter(
					"mvcPath", "/html/portlets/paymentmgt/frontoffice/frontofficeconfirmbank.jsp");
				viewURL.setParameter(
					PaymentFileDisplayTerms.PAYMENT_FILE_ID,
					String.valueOf(paymentFile.getPaymentFileId()));
				viewURL.setParameter(Constants.CMD, Constants.VIEW);
				viewURL.setParameter(WebKeys.BACK_URL, "/group/guest" + group);
				viewURL.setParameter(WebKeys.REDIRECT_URL, "/group/guest" + group);
				viewURL.setPlid(plId);
				viewURL.setWindowState(WindowState.NORMAL);

				payloadJSONObject.put("paymentFileId", message.getPaymentFileId());
				payloadJSONObject.put("linkTo", viewURL.toString());

			}
			else if (group.equals(NotificationEventKeys.GROUP4)) {

				plId = LayoutLocalServiceUtil.getFriendlyURLLayout(20182, true, group).getPlid();

				PaymentFile paymentFile =
					PaymentFileLocalServiceUtil.getPaymentFile(processOrder.getDossierId());

				LiferayPortletURL viewURL =
					liferayPortletResponse.createRenderURL(WebKeys.PAYMENT_MANAGER_PORTLET);
				viewURL.setParameter(
					"mvcPath", "/html/portlets/paymentmgt/backoffice/backofficepaymentdetail.jsp");
				viewURL.setParameter(
					PaymentFileDisplayTerms.PAYMENT_FILE_ID,
					String.valueOf(paymentFile.getPaymentFileId()));
				viewURL.setParameter(Constants.CMD, Constants.VIEW);
				viewURL.setParameter(WebKeys.REDIRECT_URL, "/group/guest");
				viewURL.setPlid(plId);
				viewURL.setWindowState(WindowState.NORMAL);

				payloadJSONObject.put("paymentFileId", message.getPaymentFileId());
				payloadJSONObject.put("linkTo", viewURL.toString());

			}
			payloadJSONObject.put("userIdDelivery", userIdDelivery);
			payloadJSONObject.put("title", title);
			payloadJSONObject.put("notificationText", message.getNotificationContent());
			payloadJSONObject.put("plId", plId);
			payloadJSONObject.put("friendlyUrl", group);

			payloadJSONArray.put(payloadJSONArray);

		}
		catch (Exception e) {
			_log.error(e);
		}

		return payloadJSONArray;
	}

	public static void sendEmailNotification(SendNotificationMessage message, String email) {

		String from = StringPool.BLANK;
		String to = StringPool.BLANK;
		String subject = StringPool.BLANK;
		String body = StringPool.BLANK;
		boolean htmlFormat = true;

		Locale locale = new Locale("vi", "VN");

		from = PortletPropsValues.SYSTEM_EMAIL;
		to = email;
		subject = PortletPropsValues.SUBJECT_TO_CUSTOMER;
		body = PortletPropsValues.CONTENT_TO_CUSTOMER;

		body = StringUtil.replace(body, "{receptionNo}", String.valueOf(message.getDossierId()));
		body =
			StringUtil.replace(
				body, "{event}", LanguageUtil.get(locale, message.getNotificationEventName()));

		SendMailUtils.sendEmail(from, to, subject, body, htmlFormat);
	}

	public static void triggerNotfication(SendToBackOfficeMsg message) {

		String event = message.getDossierStatus();
		Message commonMessage = new Message();
		List<SendNotificationMessage> notificationList = new ArrayList<SendNotificationMessage>();

		SendNotificationMessage notification = new SendNotificationMessage();

		if (event.equals(PortletConstants.DOSSIER_STATUS_NEW)) {

		}
		if (event.equals(PortletConstants.DOSSIER_STATUS_RECEIVING)) {

			notification.setNotificationEventName(NotificationEventKeys.OFFICIALS.EVENT1);

		}
		if (event.equals(PortletConstants.DOSSIER_STATUS_PAYING)) {

		}
		if (event.equals(PortletConstants.DOSSIER_STATUS_DENIED)) {

		}
		if (event.equals(PortletConstants.DOSSIER_STATUS_RECEIVED)) {

		}
		if (event.equals(PortletConstants.DOSSIER_STATUS_PROCESSING)) {

		}
		if (event.equals(PortletConstants.DOSSIER_STATUS_CANCELED)) {

		}
		if (event.equals(PortletConstants.DOSSIER_STATUS_DONE)) {

		}
		if (event.equals(PortletConstants.DOSSIER_STATUS_ARCHIVED)) {

		}
		if (event.equals(PortletConstants.DOSSIER_STATUS_ENDED)) {

		}
		if (event.equals(PortletConstants.DOSSIER_STATUS_SYSTEM)) {

		}
		if (event.equals(PortletConstants.DOSSIER_STATUS_ERROR)) {
		}

		notificationList.add(notification);

		commonMessage.put(MessageBusKeys.Message.NOTIFICATIONS, notificationList);

		MessageBusUtil.sendMessage(MessageBusKeys.Destination.NOTIFICATIONS, commonMessage);

	}

}
