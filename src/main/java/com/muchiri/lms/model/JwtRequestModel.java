package com.muchiri.lms.model;

import lombok.Data;

@Data
public class JwtRequestModel {
	
	private String email;
	private String password;

}
