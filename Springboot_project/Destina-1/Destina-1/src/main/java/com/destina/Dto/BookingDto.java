package com.destina.Dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BookingDto {
	private Long bookingId;
	private LocalDateTime bookingDateTime;
	private int numberOfTravelers;
	private BigDecimal totalAmount;
	private Long packageId;
	private Long customerId;
	private Long agentId;
	private String packageTitle;
	private LocalDateTime packageStartDate;
	private LocalDateTime packageEndDate;
	private String packageLocation;
	private String customerFullName;
	private String customerEmail;
	private String agentFullName;
	private String agentEmail;
	private String bookingStatus;

	public String getBookingStatus() {
		return bookingStatus;
	}

	public void setBookingStatus(String bookingStatus) {
		this.bookingStatus = bookingStatus;
	}

	// Default constructor
	public BookingDto() {
	}

	// Constructor for basic fields
	public BookingDto(Long bookingId, LocalDateTime bookingDateTime, int numberOfTravelers, BigDecimal totalAmount,
			Long packageId, Long customerId, Long agentId, String bookingStatus) {
		this.bookingId = bookingId;
		this.bookingDateTime = bookingDateTime;
		this.numberOfTravelers = numberOfTravelers;
		this.totalAmount = totalAmount;
		this.packageId = packageId;
		this.customerId = customerId;
		this.agentId = agentId;
		this.bookingStatus = bookingStatus;
	}

	public String getAgentFullName() {
		return agentFullName;
	}

	public void setAgentFullName(String agentFullName) {
		this.agentFullName = agentFullName;
	}

	public String getAgentEmail() {
		return agentEmail;
	}

	public void setAgentEmail(String agentEmail) {
		this.agentEmail = agentEmail;
	}

	// Constructor for methods using additional package and customer details
	public BookingDto(Long bookingId, LocalDateTime bookingDateTime, int numberOfTravelers, BigDecimal totalAmount,
			Long packageId, Long customerId, Long agentId, String packageTitle, LocalDateTime packageStartDate,
			LocalDateTime packageEndDate, String packageLocation, String customerFullName, String customerEmail,
			String role,String bookingStatus) {
		this.bookingId = bookingId;
		this.bookingDateTime = bookingDateTime;
		this.numberOfTravelers = numberOfTravelers;
		this.totalAmount = totalAmount;
		this.packageId = packageId;
		this.customerId = customerId;
		this.agentId = agentId;
		this.packageTitle = packageTitle;
		this.packageStartDate = packageStartDate;
		this.packageEndDate = packageEndDate;
		this.packageLocation = packageLocation;
		this.bookingStatus = bookingStatus;
		
//		System.out.println(role);
//		System.out.println(role.equals("AGENT"));
		if (role.equals("AGENT")) {
			System.out.println("Yes");
			this.agentFullName = customerFullName;
			this.agentEmail = customerEmail;

		} else// if(role=="CUSTOMER")
		{
			this.customerFullName = customerFullName;
			this.customerEmail = customerEmail;
		}
	}

	// Getters and Setters
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

	public Long getPackageId() {
		return packageId;
	}

	public void setPackageId(Long packageId) {
		this.packageId = packageId;
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

	public String getPackageTitle() {
		return packageTitle;
	}

	public void setPackageTitle(String packageTitle) {
		this.packageTitle = packageTitle;
	}

	public LocalDateTime getPackageStartDate() {
		return packageStartDate;
	}

	public void setPackageStartDate(LocalDateTime packageStartDate) {
		this.packageStartDate = packageStartDate;
	}

	public LocalDateTime getPackageEndDate() {
		return packageEndDate;
	}

	public void setPackageEndDate(LocalDateTime packageEndDate) {
		this.packageEndDate = packageEndDate;
	}

	public String getPackageLocation() {
		return packageLocation;
	}

	public void setPackageLocation(String packageLocation) {
		this.packageLocation = packageLocation;
	}

	public String getCustomerFullName() {
		return customerFullName;
	}

	public void setCustomerFullName(String customerFullName) {
		this.customerFullName = customerFullName;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}
}
