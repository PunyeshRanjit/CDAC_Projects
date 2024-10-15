package com.destina.Dto;



import java.time.LocalDateTime;

public class ReviewDto {

  
	public ReviewDto(Long reviewId2, LocalDateTime postTime2, String content2, Long id, String string, String title,
			String location, LocalDateTime startDate, LocalDateTime endDate) {
		// TODO Auto-generated constructor stub
	}
	private Long reviewId;
    private LocalDateTime postTime;
    private String content;
    private Long customerId;
    private String customerName;
    private Long packageId;
    private String packageTitle;
    
    
    
	public ReviewDto() {
		
	}
	public ReviewDto(Long reviewId, LocalDateTime postTime, String content, Long customerId, String customerName,
			Long packageId, String packageTitle) {
		super();
		this.reviewId = reviewId;
		this.postTime = postTime;
		this.content = content;
		this.customerId = customerId;
		this.customerName = customerName;
		this.packageId = packageId;
		this.packageTitle = packageTitle;
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
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public Long getPackageId() {
		return packageId;
	}
	public void setPackageId(Long packageId) {
		this.packageId = packageId;
	}
	public String getPackageTitle() {
		return packageTitle;
	}
	public void setPackageTitle(String packageTitle) {
		this.packageTitle = packageTitle;
	}
    
    

    }

