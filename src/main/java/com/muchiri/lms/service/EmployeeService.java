package com.muchiri.lms.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.muchiri.lms.entity.Department;
import com.muchiri.lms.entity.Employee;
import com.muchiri.lms.entity.Role;
import com.muchiri.lms.exception.EntityNotFoundException;
import com.muchiri.lms.exception.ExistingEmployeeException;
import com.muchiri.lms.exception.ExistingHODException;
import com.muchiri.lms.model.EmployeeModel;
import com.muchiri.lms.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService {

	private final EmployeeRepository employeeRepository;
	private final DepartmentService departmentService;
	private final RoleService roleService;
	private final PasswordEncoder passwordEncoder;

	// Creates a new employee
	// Accessible by hr and admin
	public void createNewEmployee(EmployeeModel employeeModel) {
		Employee employee = new Employee();
		Department department = new Department();
		Role role = new Role();
		String dpName = employeeModel.getDepartment();
		String roleName = employeeModel.getRole();

		// Checks if the department exists.
		// Saves the department if it exists
		if (departmentService.findByDepartmentName(dpName) && roleService.findByRoleName(roleName)) {
			department = departmentService.getByDepartmentName(dpName);
			role = roleService.getByRoleName(roleName);
		} else {
			throw new EntityNotFoundException("That department does not exist");
		}

		employee.setFirstName(employeeModel.getFirstName());
		employee.setLastName(employeeModel.getLastName());
		if (employeeRepository.findByEmail(employeeModel.getEmail()).isPresent()) {
			throw new ExistingEmployeeException("There is an existing employee with the same email");
		} else {
			employee.setEmail(employeeModel.getEmail());
		}
		employee.setPassword(passwordEncoder.encode(employeeModel.getPassword()));
		employee.setGender(employeeModel.getGender());
		employee.setDepartment(department);
		employee.setRole(role);

		if (checkIfEmployeeRoleHod(employee) && checkIfHodExists(employee)) {
			throw new ExistingHODException("Already existing HOD in that department");
		} else {
			employeeRepository.save(employee);
		}

	}

	// initializes admin
	public void initAdmin() {
		Role adminRole = roleService.getByRoleName("ADMIN");
		Department adminDepartment = departmentService.getByDepartmentName("Admin");
		Employee admin = new Employee(null, "admin", "admin", "admin@compulynx.co.ke",
				passwordEncoder.encode("admin@pass"), "Gender", adminDepartment, adminRole);

		employeeRepository.save(admin);
	}

	// Gets a list of all employee
	// Accessible by hr, admin and higher level
	@Transactional(readOnly = true)
	public List<EmployeeModel> getAllEmployees() {
		List<Employee> employees = employeeRepository.findAll();

		List<EmployeeModel> employeesList = employees.stream()
				.filter(empl -> !empl.getRole().getRoleName().equals("ADMIN"))
				.map(empl -> new EmployeeModel(empl.getEmployeeId(), empl.getFirstName(), empl.getLastName(),
						empl.getEmail(), empl.getPassword(), empl.getGender(), empl.getDepartment().getDepartmentName(),
						empl.getRole().getRoleName()))
				.collect(Collectors.toList());

		return employeesList;
	}

	// Gets employee by id
	@Transactional(readOnly = true)
	public EmployeeModel getEmployeeById(Long id) {
		Employee employee = employeeRepository.findById(id).get();
		EmployeeModel employeeModel = new EmployeeModel();
//		if (employee != null) {
//			BeanUtils.copyProperties(employee, employeeModel);
//		}
		employeeModel.setId(employee.getEmployeeId());
		employeeModel.setFirstName(employee.getFirstName());
		employeeModel.setLastName(employee.getLastName());
		employeeModel.setEmail(employee.getEmail());
		employeeModel.setGender(employee.getGender());
		employeeModel.setPassword(employee.getPassword());
		employeeModel.setDepartment(employee.getDepartment().getDepartmentName());
		employeeModel.setRole(employee.getRole().getRoleName());

		return employeeModel;

	}

	// Gets a list of employees from specific department
	// Accessible by hods' only
	@Transactional(readOnly = true)
	public List<EmployeeModel> getEmployeesByDepartment(Long id) {
		Employee employee = employeeRepository.findById(id).get();
		String department = employee.getDepartment().getDepartmentName();
		List<Employee> employeesByDepartment = employeeRepository.findAllByDepartment(department);
		List<EmployeeModel> employeesList = new ArrayList<EmployeeModel>();

		employeesList = employeesByDepartment.stream().filter(empl -> !empl.getRole().getRoleName().equals("HOD")).map(
				empl -> new EmployeeModel(empl.getFirstName(), empl.getLastName(), empl.getEmail(), empl.getGender()))
				.collect(Collectors.toList());

		return employeesList;
	}

	// Updates employee details
	public EmployeeModel updateEmployee(Long id, EmployeeModel employeeModel) {
		Employee employee = employeeRepository.findById(id).get();
		Department department;
		Role role;

		employee.setFirstName(employeeModel.getFirstName());
		employee.setLastName(employeeModel.getLastName());
		employee.setEmail(employeeModel.getEmail());
		if (employeeModel.getPassword() == null) {
			employee.setPassword(employee.getPassword());
		} else {
			employee.setPassword(passwordEncoder.encode(employeeModel.getPassword()));
		}
		if (!departmentService.findByDepartmentName(employeeModel.getDepartment())) {
			throw new EntityNotFoundException("No existing department with name " + employeeModel.getDepartment());
		} else if (departmentService.findByDepartmentName(employeeModel.getDepartment())
				&& roleService.findByRoleName(employeeModel.getRole())) {
			department = departmentService.getByDepartmentName(employeeModel.getDepartment());
			role = roleService.getByRoleName(employeeModel.getRole());

			employee.setDepartment(department);
			employee.setRole(role);
		}

//		if (checkIfEmployeeRoleHod(employee) && checkIfHodExists(employee)) {
//			throw new ExistingHODException("Already existing HOD in that department");
//		} else {
//			employeeRepository.save(employee);
//		}
		employeeRepository.save(employee);

		return employeeModel;
	}

	// Deletes an employee
	public boolean deleteEmployee(Long id) {
		Employee employee = employeeRepository.findById(id).get();
		employeeRepository.delete(employee);

		return true;
	}

	// check if hod already exists in a department
	private boolean checkIfHodExists(Employee employee) {
		List<Employee> employees = employeeRepository.findAll();
		List<Employee> hodEmployees = employees.stream()
				.filter(hEmpl -> hEmpl.getDepartment().getDepartnmentId() == employee.getDepartment().getDepartnmentId()
						&& hEmpl.getRole().getRoleName().equals("HOD"))
				.map(hEmpl -> new Employee(hEmpl.getEmployeeId(), hEmpl.getFirstName(), hEmpl.getLastName(),
						hEmpl.getEmail(), hEmpl.getGender(), hEmpl.getPassword(), hEmpl.getDepartment(),
						hEmpl.getRole()))
				.collect(Collectors.toList());
		log.info("Check Hod Existence");

		if (hodEmployees.size() > 0) {
			return true;
		}

		return false;
	}

	private boolean checkIfEmployeeRoleHod(Employee employee) {
		if (employee.getRole().getRoleName().equals("HOD")) {
			return true;
		} else {
			return false;
		}
	}

}
