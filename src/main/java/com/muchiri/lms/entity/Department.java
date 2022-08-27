package com.muchiri.lms.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Department implements Serializable{
	
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(
			name = "department_sequence",
			sequenceName = "department_sequence",
			allocationSize = 1
		)
	@GeneratedValue(
			strategy = GenerationType.SEQUENCE,
			generator = "department_sequence"
		)
	private Long departnmentId;
	private String departmentName;
	
}
