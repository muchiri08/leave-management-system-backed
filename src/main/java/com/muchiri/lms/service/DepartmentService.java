package com.muchiri.lms.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.muchiri.lms.entity.Department;
import com.muchiri.lms.model.DepartmentModel;
import com.muchiri.lms.repository.DepartmentRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DepartmentService {

	private final DepartmentRepository departmentRepository;

	// initializes admin department
	public void initAdminDepartment() {
		Department admin = new Department(null, "Admin");
		departmentRepository.save(admin);
	}

	// Creates new department
	// Accessible by admin and hr
	public void createNewDepartment(DepartmentModel departmentModel) {

		Department department = new Department();

		department.setDepartmentName(departmentModel.getDepartmentName());

		departmentRepository.save(department);

	}

	// Gets all departments
	public List<DepartmentModel> getAllDepartments() {
		List<Department> departments = departmentRepository.findAll();
		List<DepartmentModel> departmentList = departments.stream()
				.filter(dept -> !dept.getDepartmentName().equals("Admin"))
				.map(dept -> new DepartmentModel(dept.getDepartnmentId() ,dept.getDepartmentName())).collect(Collectors.toList());
		return departmentList;
	}

	// edits department
	public DepartmentModel updateDepartment(Long id, DepartmentModel departmentModel) {
		Department department = departmentRepository.findById(id).get();

		department.setDepartmentName(departmentModel.getDepartmentName());

		return departmentModel;
	}

	// Deletes department
	public boolean deleteDepartment(Long id) {
		Department department = departmentRepository.findById(id).get();
		departmentRepository.delete(department);

		return true;
	}
	
	public DepartmentModel getDepartmentById(long id) {
		Department department = departmentRepository.findById(id).get();
		DepartmentModel mDepartment = new DepartmentModel();
//		BeanUtils.copyProperties(department, mDepartment);
		mDepartment.setDepartmentId(department.getDepartnmentId());
		mDepartment.setDepartmentName(department.getDepartmentName());
		return mDepartment;
	}

	// gets department by name
	public Department getByDepartmentName(String department) {
		return departmentRepository.getByDepartmentName(department);
	}

	// Finds department by name.
	// return true if found otherwise false
	public boolean findByDepartmentName(String department) {
		return departmentRepository.findByDepartmentName(department).isPresent();
	}

}
