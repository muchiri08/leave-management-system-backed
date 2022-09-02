package com.muchiri.lms.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.muchiri.lms.entity.PublicHoliday;
import com.muchiri.lms.model.PublicHolidayModel;
import com.muchiri.lms.repository.PublicHolidayRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PublicHolidayService {

	private PublicHolidayRepository publicHolidayRepository;

	public PublicHolidayModel createPublicHoliday(PublicHolidayModel publicHoliday) {
		PublicHoliday holiday = new PublicHoliday();
		holiday.setHolidayDate(LocalDate.parse(publicHoliday.holidayDate));
		holiday.setDescription(publicHoliday.getDescription());

		publicHolidayRepository.save(holiday);

		return publicHoliday;
	}

	public void initializeHolidays() {
		List<PublicHoliday> holidays = new ArrayList<>();
		holidays.add(new PublicHoliday(null, LocalDate.parse("2022-04-15"), "Good Friday"));
		holidays.add(new PublicHoliday(null, LocalDate.parse("2022-04-18"), "Easter Monday"));
		holidays.add(new PublicHoliday(null, LocalDate.parse("2022-05-01"), "Labour Day"));
		holidays.add(new PublicHoliday(null, LocalDate.parse("2022-06-15"), "Madaraka Day"));
		holidays.add(new PublicHoliday(null, LocalDate.parse("2022-10-20"), "Mashujaa Day"));
		holidays.add(new PublicHoliday(null, LocalDate.parse("2022-12-12"), "Jamhuri Day"));
		holidays.add(new PublicHoliday(null, LocalDate.parse("2022-04-15"), "Good Friday"));
		holidays.add(new PublicHoliday(null, LocalDate.parse("2022-12-25"), "Christmass Day"));
		holidays.add(new PublicHoliday(null, LocalDate.parse("2022-12-26"), "Boxing Day"));
		
		publicHolidayRepository.saveAll(holidays);
	}

	@Transactional(readOnly = true)
	public List<PublicHolidayModel> getAllHolidays() {
		List<PublicHoliday> holidays = publicHolidayRepository.findAll();
		List<PublicHolidayModel> mHolidays = holidays.stream()
				.map(holiday -> new PublicHolidayModel(holiday.getHolidayDate().toString(), holiday.getDescription()))
				.collect(Collectors.toList());

		return mHolidays;
	}

	public boolean deleteHoliday(Long id) {
		PublicHoliday holiday = publicHolidayRepository.findById(id).get();
		publicHolidayRepository.delete(holiday);

		return true;
	}

	/*
	 * Trigered every 23:59:00hrs of last day of every month. If year has ended,
	 * holiday dates are inremented by 1 year
	 */
	@Scheduled(cron = "00 59 23 L * ?")
	public void getHolidayDates() {

		List<PublicHoliday> holidays = publicHolidayRepository.findAll();
		LocalDate date = holidays.get(0).getHolidayDate();

		LocalDate endOfTheCurrentYear = date.with(TemporalAdjusters.lastDayOfYear());
		LocalDateTime endOfCurrentYearWithEndTimeOfTheLastDayMinusOneMin = endOfTheCurrentYear
				.atTime(LocalTime.MAX.minusMinutes(1));

		if (LocalDateTime.now().isAfter(endOfCurrentYearWithEndTimeOfTheLastDayMinusOneMin)) {
			holidays.forEach(holiday -> holiday.setHolidayDate(holiday.getHolidayDate().plusYears(1)));
			publicHolidayRepository.saveAll(holidays);
		}

	}

}
