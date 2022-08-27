package com.muchiri.lms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveTypeModel {
	
	private Long leaveId;
	private String leaveName;
	private Integer numOfDays;

}
