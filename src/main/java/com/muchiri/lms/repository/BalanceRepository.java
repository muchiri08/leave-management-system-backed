package com.muchiri.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.muchiri.lms.entity.Balance;

public interface BalanceRepository extends JpaRepository<Balance, Long> {
	
	@Query("SELECT b.leaveBalance FROM Balance b WHERE b.employeeId = :employeeId")
	Integer getBalByEmployeeId(@Param("employeeId") Long employeeId);
	
	@Modifying
	@Query("UPDATE Balance b SET b.leaveBalance = :updatedBal WHERE b.employeeId = :employeeId")
	void updateEmployeeBalance(@Param("updatedBal") Integer updatedBal, @Param("employeeId") Long employeeId);

}
