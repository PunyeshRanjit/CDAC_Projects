package com.destina.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmailDto {

	private String email;
	
	

	

	public EmailDto() {
		
	}

	@JsonProperty("email")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public EmailDto(String email) {
		
		this.email = email;
	}
	
}