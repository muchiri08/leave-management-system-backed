package com.muchiri.lms.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MainUtil {

	/*
	 * Checks if the there are Sundays between the dates given
	 * 
	 * @return return the sundays' count
	 */
	public Integer getNoOfSundays(LocalDate startDate, LocalDate endDate) {
		int count = 0;
		// changing LocalDate to Calendar
		Calendar cStartDate = Calendar.getInstance();
		cStartDate.set(startDate.getYear(), startDate.getMonthValue() - 1, startDate.getDayOfMonth());
		Calendar cEndDate = Calendar.getInstance();
		cEndDate.set(endDate.getYear(), endDate.getMonthValue() - 1, endDate.getDayOfMonth());

		while (cStartDate.before(cEndDate)) {
			if (cStartDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				count++;
				cStartDate.add(Calendar.DATE, 7);
			} else {
				cStartDate.add(Calendar.DATE, 1);
			}
		}
		return count;
	}

	/*
	 * Checks if the there are holidays between the dates given
	 * 
	 * @return return the holidays' count
	 */
	public Integer getNoOfHolidays(List<LocalDate> holidays, LocalDate startDate, LocalDate endDate) {
		int count = 0;
		log.info("Method start");
		while (startDate.isBefore(endDate)) {
			log.info("started while loop");
			for (LocalDate holiday : holidays) {
				log.info("started for loop");
				if (startDate.equals(holiday)) {
					log.info("checking the condition");
					count++;
				}
			}
			log.info("incrementing day by 1");
			startDate = startDate.plusDays(1);
		}
		return count;
	}

	/*
	 * @return return the no of days between the two dates
	 */
	public Long differenceBetweenDates(LocalDate startDate, LocalDate endDate) {
		return Math.abs(ChronoUnit.DAYS.between(startDate, endDate) + 1);
	}

}
