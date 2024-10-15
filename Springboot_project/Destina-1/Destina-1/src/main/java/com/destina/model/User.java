package com.destina.model;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import jakarta.persistence.*;



import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;
    private String mobileNumber;
    private String role;
    private String address;
    

    private LocalDateTime createdOn;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus = AccountStatus.UNAPROOVED;
    private String resetToken;
    
    @OneToMany(mappedBy = "agent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Package> packages;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviewsAsCustomer;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Booking> bookingsAsCustomer;

    @OneToMany(mappedBy = "agent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Booking> bookingsAsAgent;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Payment> paymentsAsCustomer;

    @OneToMany(mappedBy = "agent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Payment> paymentsAsAgent;

    @OneToMany(mappedBy = "from", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Notification> sentNotifications;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Notification> receivedNotifications;


	public String getResetToken() {
		return resetToken;
	}

	public void setResetToken(String resetToken) {
		this.resetToken = resetToken;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}

	public AccountStatus getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(AccountStatus accountStatus) {
		this.accountStatus = accountStatus;
	}

	public List<Package> getPackages() {
		return packages;
	}

	public void setPackages(List<Package> packages) {
		this.packages = packages;
	}

	public List<Review> getReviewsAsCustomer() {
		return reviewsAsCustomer;
	}

	public void setReviewsAsCustomer(List<Review> reviewsAsCustomer) {
		this.reviewsAsCustomer = reviewsAsCustomer;
	}

	public List<Booking> getBookingsAsCustomer() {
		return bookingsAsCustomer;
	}

	public void setBookingsAsCustomer(List<Booking> bookingsAsCustomer) {
		this.bookingsAsCustomer = bookingsAsCustomer;
	}

	public List<Booking> getBookingsAsAgent() {
		return bookingsAsAgent;
	}

	public void setBookingsAsAgent(List<Booking> bookingsAsAgent) {
		this.bookingsAsAgent = bookingsAsAgent;
	}

	public List<Payment> getPaymentsAsCustomer() {
		return paymentsAsCustomer;
	}

	public void setPaymentsAsCustomer(List<Payment> paymentsAsCustomer) {
		this.paymentsAsCustomer = paymentsAsCustomer;
	}

	public List<Payment> getPaymentsAsAgent() {
		return paymentsAsAgent;
	}

	public void setPaymentsAsAgent(List<Payment> paymentsAsAgent) {
		this.paymentsAsAgent = paymentsAsAgent;
	}

	public List<Notification> getSentNotifications() {
		return sentNotifications;
	}

	public void setSentNotifications(List<Notification> sentNotifications) {
		this.sentNotifications = sentNotifications;
	}

	public List<Notification> getReceivedNotifications() {
		return receivedNotifications;
	}

	public void setReceivedNotifications(List<Notification> receivedNotifications) {
		this.receivedNotifications = receivedNotifications;
	}
    
    
}


