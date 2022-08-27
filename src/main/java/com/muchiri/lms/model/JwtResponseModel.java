package com.muchiri.lms.model;

import com.muchiri.lms.entity.Employee;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponseModel {
	
	private Employee employee;
	private String jwtToken;

}
