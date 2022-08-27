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
	
	
	
}
