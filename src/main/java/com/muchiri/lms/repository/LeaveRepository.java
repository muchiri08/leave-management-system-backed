package com.muchiri.lms.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import com.muchiri.lms.entity.Employee;
import com.muchiri.lms.entity.Leave;

@Repository
public interface LeaveRepository extends JpaRepository<Leave, Long> {
	
	public  List<Leave> findByEmployee(Employee employee);
	
	@Query(value = "SELECT l FROM Leave l WHERE l.dateOfCreation BETWEEN :startDate AND :endDate")
	public List<Leave> getLeaveBetween2Dates(@PathVariable("startDate") LocalDateTime startDate, @PathVariable("endDate") LocalDateTime endDate);

}
