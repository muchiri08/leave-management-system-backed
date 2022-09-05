package com.muchiri.lms.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.muchiri.lms.exception.LeaveSystemException;
import com.muchiri.lms.model.DepartmentModel;
import com.muchiri.lms.model.EmployeeModel;
import com.muchiri.lms.model.PublicHolidayModel;
import com.muchiri.lms.service.DepartmentService;
import com.muchiri.lms.service.EmployeeService;
import com.muchiri.lms.service.PublicHolidayService;
import com.muchiri.lms.service.RoleService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class MainController {
	
	private final EmployeeService employeeService;
	private final DepartmentService departmentService;
	private final RoleService roleService;
	PublicHolidayService publicHolidayService;
	
	@PostMapping("/newEmployee")
	@PreAuthorize("hasAnyRole('ADMIN', 'HR')")
	public EmployeeModel createEmployee(@RequestBody EmployeeModel employee) {
		if (employeePostIsEmpty(employee)) {
			new LeaveSystemException("The post contains empty fields");
		} 		
		employeeService.createNewEmployee(employee);
		
		return employee;
	}
	
	@PostMapping("/newDepartment")
	@PreAuthorize("hasAnyRole('ADMIN', 'HR')")
	public DepartmentModel createNewDepartment(@RequestBody DepartmentModel department) {
		departmentService.createNewDepartment(department);
		
		return department;
	}
	
	@GetMapping("/departments")
	@PreAuthorize("hasAnyRole('ADMIN', 'HR', 'HIGHER_LEVEL')")
	public List<DepartmentModel> getAllDepartments() {
		return departmentService.getAllDepartments();
	}
	
	
//	@PostConstruct
//	public void initializeRoles() {
//		roleService.initialiseRoles();
//	}
//	
//	@PostConstruct
//	public void initializeAdmin() {
//		departmentService.initAdminDepartment();
//		employeeService.initAdmin();
//	}
	
//	@PostConstruct
//	public void initializeHolidays() {
//		publicHolidayService.initializeHolidays();
//	}
	
	@GetMapping("/employees")
	@PreAuthorize("hasAnyRole('ADMIN', 'HR', 'HIGHER_LEVEL')")
	public List<EmployeeModel> employees(){
		return employeeService.getAllEmployees();
	}
	
	@PreAuthorize("hasRole('HOD')")
	@GetMapping("/employees-by-department/{id}")
	public List<EmployeeModel> getEmployeesByDepartment(@PathVariable("id") Long id){
		return employeeService.getEmployeesByDepartment(id);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'HR')")
	@GetMapping("/employee/{id}")
	public ResponseEntity<EmployeeModel> getEmployeeById(@PathVariable("id") Long id) {
		EmployeeModel employee = employeeService.getEmployeeById(id);
		
		return ResponseEntity.ok(employee);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping("/department/{id}")
	public ResponseEntity<DepartmentModel> getDepartmentById(@PathVariable("id") Long id){
		DepartmentModel department = departmentService.getDepartmentById(id);
		
		return ResponseEntity.ok(department);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'HR')")
	@PutMapping("/update-employee/{id}")
	public ResponseEntity<EmployeeModel> updateEmployee(@PathVariable("id") Long id, @RequestBody EmployeeModel employee) {
		 employeeService.updateEmployee(id, employee);
		 
		 return ResponseEntity.ok(employee);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PutMapping("/update-department/{id}")
	public ResponseEntity<DepartmentModel> updateDepartment(@PathVariable("id") Long id, @RequestBody DepartmentModel department){
		departmentService.updateDepartment(id, department);
		
		return ResponseEntity.ok(department);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@DeleteMapping("/delete-employee/{id}")
	public ResponseEntity<Map<String, Boolean>> deleteEmployee(@PathVariable("id") Long id) {
		boolean deleted = false;
		deleted = employeeService.deleteEmployee(id);
		
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", deleted);
		System.out.println(id);
		
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete-department/{id}")
	public ResponseEntity<Map<String, Boolean>> deleteDepartment(@PathVariable("id") Long id){
		boolean deleted = false;
		deleted = departmentService.deleteDepartment(id);
		
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", deleted);
		
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'HR')")
	@PostMapping("/create-holiday")
	public PublicHolidayModel createHoliday(@RequestBody PublicHolidayModel holidayModel) {
		return publicHolidayService.createPublicHoliday(holidayModel);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'HR', 'HIGHER_LEVEL', 'HOD', 'CASUAL_EMPLOYEE')")
	@GetMapping("/holidays")
	public List<PublicHolidayModel> getAllHolidays(){
		return publicHolidayService.getAllHolidays();
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete-holiday/{id}")
	public ResponseEntity<Map<String, Boolean>> deletePublicHoliday(@PathVariable("id") Long id){
		boolean deleted = false;
		deleted = publicHolidayService.deleteHoliday(id);
		
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", deleted);
		
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize("hasAnyRole('HR', 'HOD', 'CASUAL_EMPLOYEE')")
	@GetMapping("/leave-balance/{id}")
	public Integer getLeaveBalanceByEmployeeId(@PathVariable("id") Long id) {
		return employeeService.getLeaveBalance(id);
	}
		
	
	//checks if response body of employee is empty or not
	private boolean employeePostIsEmpty(EmployeeModel employee) {
		if (employee.getFirstName() == "" ||
				employee.getLastName() == "" || 
				employee.getEmail() == "" ||
				employee.getPassword() == "" ||
				employee.getGender() == "" || 
				employee.getDepartment() == "" || 
				employee.getRole() == "") {
			return true;
		}
		return false;
	}
	

}
