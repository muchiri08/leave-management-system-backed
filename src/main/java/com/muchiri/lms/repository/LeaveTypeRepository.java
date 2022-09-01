package com.muchiri.lms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.muchiri.lms.entity.LeaveType;

@Repository
public interface LeaveTypeRepository extends JpaRepository<LeaveType, Long> {

	@Query("SELECT s FROM LeaveType s WHERE s.leaveName = ?1")
	Optional<LeaveType> findByLeaveTypeName(String name);

	@Query("SELECT SUM(lt.noOfDays) FROM LeaveType lt")
	Integer getSum();

}
