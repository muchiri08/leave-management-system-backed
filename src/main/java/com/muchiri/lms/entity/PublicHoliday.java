package com.muchiri.lms.entity;

import java.time.LocalDate;

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
public class PublicHoliday {
	
	@Id
	@SequenceGenerator(
			name = "holiday_sequence",
			sequenceName = "holiday_sequence",
			allocationSize = 1
		)
	@GeneratedValue(
			strategy = GenerationType.SEQUENCE,
			generator = "holiday_sequence"
		)
	private Long id;
	private LocalDate holidayDate;
	private String description;

}
