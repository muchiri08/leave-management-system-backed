package com.muchiri.lms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeModel {
	
	private Long id;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String gender;
	private String department;
	private String role;
	
	
	public EmployeeModel(String firstName, String lastName, String email, String gender) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.gender = gender;
	}


	public EmployeeModel(Long id, String firstName, String lastName, String email, String gender, String department) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.gender = gender;
		this.department = department;
	}
	
	
}
