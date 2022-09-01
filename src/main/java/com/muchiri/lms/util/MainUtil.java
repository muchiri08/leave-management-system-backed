package com.muchiri.lms.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class MainUtil {

	/*
	 * Checks if the there are Sundays between the dates given
	 * 
	 * @return return the sundays' count
	 */
	public int getNoOfSundays(LocalDate startDate, LocalDate endDate) {
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
	public int getNoOfHolidays(List<LocalDate> holidays, LocalDate startDate, LocalDate endDate) {
		int count = 0;
		while (startDate.isBefore(endDate)) {
			for (LocalDate holiday : holidays) {
				if (startDate.equals(holiday)) {
					count++;
				}
			}
			startDate.plusDays(1);
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
