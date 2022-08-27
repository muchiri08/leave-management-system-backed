package com.muchiri.lms.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(
			name = "emaidId_unique",
			columnNames = "email" 
		))
public class Employee implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(
			name = "employee_sequence",
			sequenceName = "employee_sequence",
			allocationSize = 1
		)
	@GeneratedValue(
			strategy = GenerationType.SEQUENCE,
			generator = "employee_sequence"
		)
	private Long employeeId;
	private String firstName;
	private String lastName;
	@Column(nullable = false)
	private String email;
	private String password;
	private String gender;
	@ManyToOne
	@JoinColumn(
			name = "department", referencedColumnName = "departmentName"
		)
	private Department department;
	@ManyToOne
	@JoinColumn(name = "position", referencedColumnName = "roleName")
	private Role role;
	
	public Employee(String firstName, String lastName, String email, Department department, Role role) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.department = department;
		this.role = role;
	}
	
	

}
