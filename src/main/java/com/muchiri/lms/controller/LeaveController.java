package com.muchiri.lms.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.muchiri.lms.model.LeaveModel;
import com.muchiri.lms.model.LeaveTypeModel;
import com.muchiri.lms.service.LeaveService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/leave")
@Slf4j
public class LeaveController {

	private final LeaveService leaveService;

	@PostMapping("/create-leave-type")
	@PreAuthorize("hasAnyRole('ADMIN', 'HR')")
	public LeaveTypeModel createLeaveType(@RequestBody LeaveTypeModel leaveTypeModel) {
		leaveService.createLeaveType(leaveTypeModel);
		return leaveTypeModel;
	}

	@GetMapping("/leave-types")
	@PreAuthorize("hasAnyRole('ADMIN', 'HIGHER_LEVEL', 'HR', 'HOD', 'CASUAL_EMPLOYEE')")
	public List<LeaveTypeModel> getLeaveTypes() {
		return leaveService.getLeaveTypes();
	}

	@GetMapping("/leave-type/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'HR')")
	public ResponseEntity<LeaveTypeModel> getLeaveTypeById(@PathVariable("id") Long id) {
		LeaveTypeModel leave = leaveService.findLeaveTypeById(id);
		return ResponseEntity.ok(leave);
	}

	@PutMapping("/update-leave-type/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'HR')")
	public LeaveTypeModel updateLeaveType(@PathVariable("id") Long id, @RequestBody LeaveTypeModel leaveTypeModel) {
		return leaveService.updateLeaveType(id, leaveTypeModel);
	}

	@DeleteMapping("/delete-leave-type/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'HR')")
	public ResponseEntity<Boolean> deleteLeaveType(@PathVariable("id") Long id) {
		boolean deleted = false;
		deleted = leaveService.deleteLeaveType(id);

		return ResponseEntity.ok(deleted);
	}

	@PostMapping("/create-leave-request/{id}")
	@PreAuthorize("hasAnyRole('HR', 'HOD', 'CASUAL_EMPLOYEE', 'ADMIN')")
	public void createLeaveRequest(@PathVariable("id") Long id, @RequestBody LeaveModel leaveModel) {
		leaveService.createLeaveRequest(id, leaveModel);
	}

	@PutMapping("/approve-leave/{leaveId}/{approverId}")
	@PreAuthorize("hasAnyRole('HIGHER_LEVEL', 'HR', 'HOD', 'ADMIN')")
	public void approveLeaveRequest(@PathVariable("leaveId") Long leaveId, @PathVariable("approverId") Long approverId,
			@RequestBody LeaveModel leaveModel) {
		leaveService.approveLeaveRequest(leaveId, leaveModel, approverId);
		log.info(leaveModel.toString());
	}

	@GetMapping("/all-leaves")
	@PreAuthorize("hasAnyRole('HR', 'ADMIN')")
	public List<LeaveModel> getAllLeaves() {
		return leaveService.getAllLeaves();
	}

	@PreAuthorize("hasRole('HR')")
	@GetMapping("/hod-leaves")
	public List<LeaveModel> getHodLeaves() {
		return leaveService.getHodLeaves();
	}

	@PreAuthorize("hasAnyRole('HR', 'HOD', 'ADMIN')")
	@GetMapping("/leave-request/{id}")
	public LeaveModel getLeaveRequestById(@PathVariable("id") Long id) {
		return leaveService.getLeaveRequestById(id);
	}

	@GetMapping("/department-leaves/{id}")
	@PreAuthorize("hasAnyRole('HOD')")
	public List<LeaveModel> getLeavesByDepartment(@PathVariable("id") Long id) {
		return leaveService.getLeavesByDepartment(id);
	}

	@PreAuthorize("hasAnyRole('HR', 'HOD', 'CASUAL_EMPLOYEE')")
	@GetMapping("/my-leaves/{id}")
	public List<LeaveModel> getEmployeeLeaves(@PathVariable("id") Long id) {
		return leaveService.getEmployeeLeaves(id);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete-leave/{id}")
	public ResponseEntity<Map<String, Boolean>> deleteLeaveRequest(@PathVariable("id") Long id){
		boolean deleted = false;
		deleted = leaveService.deleteLeaveRequest(id);
		
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", deleted);
		
		return ResponseEntity.ok(response);
	}

}
