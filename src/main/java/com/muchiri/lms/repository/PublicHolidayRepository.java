package com.muchiri.lms.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.muchiri.lms.entity.PublicHoliday;

@Repository
public interface PublicHolidayRepository extends JpaRepository<PublicHoliday, Long> {
	
	@Query("SELECT h.holidayDate FROM PublicHoliday h")
	List<LocalDate> getHolidaysDates();

}
