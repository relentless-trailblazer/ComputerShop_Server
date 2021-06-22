package com.computershop.dto;

public class ForgotPasswordDTO {
	private Long userId;
	private String username;

	public ForgotPasswordDTO(Long userId, String username) {
		super();
		this.userId = userId;
		this.username = username;
	}

	public ForgotPasswordDTO() {
		super();
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
