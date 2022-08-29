package com.muchiri.lms.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

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

}
