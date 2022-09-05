package com.muchiri.lms.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PublicHolidayModel {
	
	public Long holidayId;
	public String holidayDate;
	public String description;

}
