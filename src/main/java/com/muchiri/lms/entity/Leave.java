package com.muchiri.lms.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "leave_request_table")
public class Leave implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(
			name = "leave_request_sequence",
			sequenceName = "leave_request_sequence",
			allocationSize = 1
		)
	@GeneratedValue(
			strategy = GenerationType.SEQUENCE,
			generator = "leave_request_sequence"
		)
	private Long leaveId;
	@OneToOne(cascade = CascadeType.PERSIST)
	@JoinColumns({
			@JoinColumn(name = "firstName", referencedColumnName = "firstName"),
			@JoinColumn(name = "lastName", referencedColumnName = "lastName"),
	})
	private Employee employee;
	@OneToOne
	@JoinColumn(
			name = "leaveType", 
			referencedColumnName = "leaveName"
		)
	private LeaveType leaveType;
	private LocalDate startDate;
	private LocalDate endDate;
	private String reason;
	@Column(name = "leave_status")
	private String status;
	private LocalDateTime dateOfCreation;
	private String approvedBy;

}
