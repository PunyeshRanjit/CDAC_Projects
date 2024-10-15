package com.destina.Dto;



public class ResetPasswordDto {
    private String token;
    private String newPassword;

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

	public ResetPasswordDto(String token, String newPassword) {
		
		this.token = token;
		this.newPassword = newPassword;
	}

	public ResetPasswordDto() {
		
	}
    
	
    
}

