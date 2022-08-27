package com.muchiri.lms.entity;

import java.io.Serializable;

import javax.persistence.Column;
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
public class LeaveType implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(
			name = "leaveType_sequence",
			sequenceName = "leaveType_sequence",
			allocationSize = 1
		)
	@GeneratedValue(
			strategy = GenerationType.SEQUENCE,
			generator = "leaveType_sequence"
		)
	private Long leaveTypeId;
	@Column(nullable = false)
	private String leaveName;
	@Column(nullable = false)
	private Integer noOfDays;

}
