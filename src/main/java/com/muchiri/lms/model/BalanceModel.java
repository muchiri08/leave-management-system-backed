package com.muchiri.lms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceModel {
	
	private Long employeeId;
	private int leaveBalance;

}
