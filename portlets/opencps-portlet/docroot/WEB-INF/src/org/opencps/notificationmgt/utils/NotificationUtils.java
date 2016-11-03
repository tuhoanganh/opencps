package org.opencps.notificationmgt.utils;

import org.opencps.dossiermgt.model.Dossier;
import org.opencps.dossiermgt.service.DossierLocalServiceUtil;
import org.opencps.notificationmgt.message.SendNotificationMessage;
import org.opencps.util.MessageBusKeys;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.util.StringPool;

public class NotificationUtils {
	
	private static Log _log = LogFactoryUtil.getLog(NotificationUtils.class);
	
	public static void pushNotification(Message message){
		
		try{
		
		SendNotificationMessage sendNotification =
						(SendNotificationMessage) message.get(MessageBusKeys.Message.NOTIFICATIONS);
		
		sendNotification.getDossiderId();
		
		Dossier dossier = DossierLocalServiceUtil.getDossier(sendNotification.getDossiderId());
		
		String dossiserStatus = StringPool.BLANK;
		
		//if(dossiserStatus.endsWith(suffix));
		
		}catch(Exception e){
			_log.error(e);
		}
	};
}