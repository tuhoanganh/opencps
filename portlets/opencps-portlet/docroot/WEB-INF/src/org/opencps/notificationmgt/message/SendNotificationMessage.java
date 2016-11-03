package org.opencps.notificationmgt.message;

public class SendNotificationMessage {
	
	protected long dossiderId;
	protected String NotificationType;

	
	
	public String getNotificationType() {
	
		return NotificationType;
	}


	
	public void setNotificationType(String notificationType) {
	
		NotificationType = notificationType;
	}


	public long getDossiderId() {
	
		return dossiderId;
	}

	
	public void setDossiderId(long dossiderId) {
	
		this.dossiderId = dossiderId;
	}
}