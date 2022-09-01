package com.muchiri.lms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.muchiri.lms.entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	@Query(value = "SELECT * FROM employee WHERE department =?1 ", nativeQuery = true)
	List<Employee> findAllByDepartment(String depatment);
	
	Optional<Employee> findByEmail(String email);
	

}
