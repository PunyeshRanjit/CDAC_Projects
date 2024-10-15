package com.destina.Dto;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Lob;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PackageDto {
//    public PackageDto(Long packageId2, String title2, byte[] image2, String description2, String location2,
//			LocalDateTime startDate2, LocalDateTime endDate2, BigDecimal pricePerPerson2, int numberOfSeatsAvailable2,
//			Long id, String agentName2) {
//		// TODO Auto-generated constructor stub
//	}
	private Long packageId;
    private String title;
    @Lob
    private byte[] image;
    private String description;
    private String location;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDate;
    private BigDecimal pricePerPerson;
    private int numberOfSeatsAvailable;
    private Long agentId;
    private String agentName;
    
    public PackageDto() {
    	
    }
    
	public PackageDto(Long packageId, String title, byte[] image, String description, String location,
			LocalDateTime startDate, LocalDateTime endDate, BigDecimal pricePerPerson, int numberOfSeatsAvailable,
			Long agentId, String agentName) {
		super();
		this.packageId = packageId;
		this.title = title;
		this.image = image;
		this.description = description;
		this.location = location;
		this.startDate = startDate;
		this.endDate = endDate;
		this.pricePerPerson = pricePerPerson;
		this.numberOfSeatsAvailable = numberOfSeatsAvailable;
		this.agentId = agentId;
		this.agentName = agentName;
	}
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
	public Long getAgentId() {
		return agentId;
	}
	public void setAgentId(Long agentId) {
		this.agentId = agentId;
	}
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
    
}

