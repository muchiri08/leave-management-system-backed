package com.muchiri.lms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveModel {
	
	private Long leaveId;
	private String firstName;
	private String lastName;
	private String departmentName;
	private String leaveType;
	private String startDate;
	private String endDate;
	private String reason;
	private String status;
	private String dateOfCreation;
	private String approvedBy;
	
	public LeaveModel(String leaveType, String startDate, String endDate, String reason) {
		super();
		this.leaveType = leaveType;
		this.startDate = startDate;
		this.endDate = endDate;
		this.reason = reason;
	}

	public LeaveModel(Long leaveId ,String leaveType, String startDate, String endDate, String reason, String status) {
		super();
		this.leaveId = leaveId;
		this.leaveType = leaveType;
		this.startDate = startDate;
		this.endDate = endDate;
		this.reason = reason;
		this.status = status;
	}

	public LeaveModel(Long leaveId, String firstName, String lastName, String leaveType, String startDate,
			String endDate, String reason, String status, String dateOfCreation, String approvedBy) {
		super();
		this.leaveId = leaveId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.leaveType = leaveType;
		this.startDate = startDate;
		this.endDate = endDate;
		this.reason = reason;
		this.status = status;
		this.dateOfCreation = dateOfCreation;
		this.approvedBy = approvedBy;
	}
	
	
	
}
