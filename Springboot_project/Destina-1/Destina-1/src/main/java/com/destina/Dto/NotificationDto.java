package com.destina.Dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@AllArgsConstructor
public class NotificationDto {

    
	private Long notificationId;
    private String subject;
    private String fromName;
    private String customerName;
    
	public NotificationDto(Long notificationId, String subject, String fromName, String customerName) {
		super();
		this.notificationId = notificationId;
		this.subject = subject;
		this.fromName = fromName;
		this.customerName = customerName;
	}

	public Long getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(Long notificationId) {
		this.notificationId = notificationId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
    
	
    
}
