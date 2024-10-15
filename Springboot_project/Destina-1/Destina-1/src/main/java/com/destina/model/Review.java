package com.destina.model;

import java.time.LocalDateTime;
import java.util.Optional;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    private LocalDateTime postTime;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private User customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id", referencedColumnName = "packageId")
    private Package aPackage;
    
    

	public Review() {
		
	}

	public Review(Long reviewId, LocalDateTime postTime, String content, User customer, Package aPackage) {
		
		this.reviewId = reviewId;
		this.postTime = postTime;
		this.content = content;
		this.customer = customer;
		this.aPackage = aPackage;
	}

	public Long getReviewId() {
		return reviewId;
	}

	public void setReviewId(Long reviewId) {
		this.reviewId = reviewId;
	}

	public LocalDateTime getPostTime() {
		return postTime;
	}

	public void setPostTime(LocalDateTime postTime) {
		this.postTime = postTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public User getCustomer() {
		return customer;
	}

	public void setCustomer(User user) {
		this.customer = user;
	}

	public Package getaPackage() {
		return aPackage;
	}

	public void setaPackage(Package pkg) {
		this.aPackage = pkg;
	}
    
}
