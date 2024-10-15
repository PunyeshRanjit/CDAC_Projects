package com.destina.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    private LocalDateTime bookingDateTime;
    private int numberOfTravelers;
    private BigDecimal totalAmount;
    

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id", referencedColumnName = "packageId")
    private Package aPackage;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private User customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id", referencedColumnName = "id")
    private User agent;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "booking")
    private Payment payment;
    
    private String bookingStatus = "Pending";
    

	public Booking(Long bookingId, LocalDateTime bookingDateTime, int numberOfTravelers, BigDecimal totalAmount,
			Package aPackage, User customer, User agent, Payment payment) {
		super();
		this.bookingId = bookingId;
		this.bookingDateTime = bookingDateTime;
		this.numberOfTravelers = numberOfTravelers;
		this.totalAmount = totalAmount;
		this.aPackage = aPackage;
		this.customer = customer;
		this.agent = agent;
		this.payment = payment;
	}

	
	public String getBookingStatus() {
		return bookingStatus;
	}


	public void setBookingStatus(String bookingStatus) {
		this.bookingStatus = bookingStatus;
	}


	public Booking() {
		// TODO Auto-generated constructor stub
	}

	public Long getBookingId() {
		return bookingId;
	}

	public void setBookingId(Long bookingId) {
		this.bookingId = bookingId;
	}

	public LocalDateTime getBookingDateTime() {
		return bookingDateTime;
	}

	public void setBookingDateTime(LocalDateTime bookingDateTime) {
		this.bookingDateTime = bookingDateTime;
	}

	public int getNumberOfTravelers() {
		return numberOfTravelers;
	}

	public void setNumberOfTravelers(int numberOfTravelers) {
		this.numberOfTravelers = numberOfTravelers;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Package getaPackage() {
		return aPackage;
	}

	public void setaPackage(Package aPackage) {
		this.aPackage = aPackage;
	}

	public User getCustomer() {
		return customer;
	}

	public void setCustomer(User customer) {
		this.customer = customer;
	}

	public User getAgent() {
		return agent;
	}

	public void setAgent(User agent) {
		this.agent = agent;
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}
    
   
}