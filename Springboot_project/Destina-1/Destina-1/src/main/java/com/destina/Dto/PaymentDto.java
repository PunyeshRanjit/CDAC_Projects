package com.destina.Dto;


import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentDto {
    private Long paymentId;
    private BigDecimal amountPaid;
    private String paymentStatus;
    private LocalDateTime paymentDate;
    private Long customerId;
    private Long agentId;
    private Long bookingId;

    

    
    public PaymentDto() {
		
	}

	public PaymentDto(Long paymentId, BigDecimal amountPaid, String paymentStatus, LocalDateTime paymentDate,
			Long customerId, Long agentId, Long bookingId) {
		
		this.paymentId = paymentId;
		this.amountPaid = amountPaid;
		this.paymentStatus = paymentStatus;
		this.paymentDate = paymentDate;
		this.customerId = customerId;
		this.agentId = agentId;
		this.bookingId = bookingId;
	}

	public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(BigDecimal amountPaid) {
        this.amountPaid = amountPaid;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public Long getBookingId() {
        return bookingId;
    }

	public void setBookingId(Long bookingId) {
		this.bookingId = bookingId;
	}
    
    

//    public void setBookingId(Long bookingId) {
//        this.bookingId = bookingId;
//    }
}
