package com.destina.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Package {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long packageId;

    private String title;

    @Lob
    private byte[] image;

    private String description;
    private String location;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private BigDecimal pricePerPerson;
    private int numberOfSeatsAvailable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id",referencedColumnName = "id")
    private User agent;

    @OneToMany(mappedBy = "aPackage", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews;

    @OneToMany(mappedBy = "aPackage", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Booking> bookings;

	public Long getPackageId() {
		return packageId;
	}

	public void setPackageId(Long packageId) {
		this.packageId = packageId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}

	public BigDecimal getPricePerPerson() {
		return pricePerPerson;
	}

	public void setPricePerPerson(BigDecimal pricePerPerson) {
		this.pricePerPerson = pricePerPerson;
	}

	public int getNumberOfSeatsAvailable() {
		return numberOfSeatsAvailable;
	}

	public void setNumberOfSeatsAvailable(int numberOfSeatsAvailable) {
		this.numberOfSeatsAvailable = numberOfSeatsAvailable;
	}

	public User getAgent() {
		return agent;
	}

	public void setAgent(User agent) {
		this.agent = agent;
	}

	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	public List<Booking> getBookings() {
		return bookings;
	}

	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}

    
}