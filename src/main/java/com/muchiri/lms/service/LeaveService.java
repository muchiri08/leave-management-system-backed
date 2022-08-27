package com.muchiri.lms.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.muchiri.lms.entity.Employee;
import com.muchiri.lms.entity.Leave;
import com.muchiri.lms.entity.LeaveType;
import com.muchiri.lms.exception.LeaveSystemException;
import com.muchiri.lms.model.LeaveModel;
import com.muchiri.lms.model.LeaveTypeModel;
import com.muchiri.lms.repository.EmployeeRepository;
import com.muchiri.lms.repository.LeaveRepository;
import com.muchiri.lms.repository.LeaveTypeRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LeaveService {

	private final LeaveRepository leaveRepository;
	private final LeaveTypeRepository leaveTypeRepository;
	private final EmployeeRepository employeeRepository;
	private final MailSenderService mailSenderService;

	// creates new leave type
	public void createLeaveType(LeaveTypeModel leaveTypeModel) {
		LeaveType leaveType = new LeaveType();
		leaveType.setLeaveName(leaveTypeModel.getLeaveName());
		leaveType.setNoOfDays(leaveTypeModel.getNumOfDays());

		leaveTypeRepository.save(leaveType);

	}

	// Gets all leave type
	@Transactional(readOnly = true)
	public List<LeaveTypeModel> getLeaveTypes() {
		List<LeaveType> leaveTypes = leaveTypeRepository.findAll();
		List<LeaveTypeModel> leaveTypesModel = leaveTypes.stream()
				.map(leave -> new LeaveTypeModel(leave.getLeaveTypeId(), leave.getLeaveName(), leave.getNoOfDays()))
				.collect(Collectors.toList());
		return leaveTypesModel;
	}

	// Updates the leave type
	public LeaveTypeModel updateLeaveType(Long id, LeaveTypeModel leaveTypeModel) {
		LeaveType leaveType = leaveTypeRepository.findById(id).get();
		leaveType.setLeaveName(leaveTypeModel.getLeaveName());
		leaveType.setNoOfDays(leaveTypeModel.getNumOfDays());

		leaveTypeRepository.save(leaveType);

		return leaveTypeModel;
	}

	// Deletes a leave type
	public boolean deleteLeaveType(Long id) {
		LeaveType leaveType = leaveTypeRepository.findById(id).get();
		leaveTypeRepository.delete(leaveType);

		return true;
	}

	// finds leave type by name
	public Optional<LeaveType> findLeaveTypeByName(String name) {
		return leaveTypeRepository.findByLeaveTypeName(name);
	}

	// deletes leave type
	public LeaveTypeModel findLeaveTypeById(Long id) {
		LeaveType leaveType = leaveTypeRepository.findById(id).get();
		LeaveTypeModel mLeaveType = new LeaveTypeModel();
		mLeaveType.setLeaveId(leaveType.getLeaveTypeId());
		mLeaveType.setLeaveName(leaveType.getLeaveName());
		mLeaveType.setNumOfDays(leaveType.getNoOfDays());

		return mLeaveType;
	}

	/*
	 * Creates leave request
	 *
	 * @param long id is the id of the request creator(employee)
	 * 
	 */
	public LeaveModel createLeaveRequest(Long id, LeaveModel leaveModel) {
		Employee employee = employeeRepository.findById(id).get();
		String status = "Requested";
		String approvedBy = "Waiting";
		Leave leave = new Leave();

		LeaveType leaveType = leaveTypeRepository.findByLeaveTypeName(leaveModel.getLeaveType()).get();

		leave.setEmployee(employee);
		leave.setLeaveType(leaveType);
		leave.setStartDate(convertedDate(leaveModel.getStartDate()));
		leave.setEndDate(convertedDate(leaveModel.getEndDate()));
		leave.setReason(leaveModel.getReason());
		leave.setStatus(status);
		leave.setDateOfCreation(LocalDateTime.now());
		leave.setApprovedBy(approvedBy);

		leaveRepository.save(leave);

		Employee oneToBeSentMail = new Employee();

		List<Employee> employees = employeeRepository.findAll();
		if (employee.getRole().getRoleName().equals("CASUAL_EMPLOYEE")) {
			List<Employee> headEmployees = employees.stream()
					.filter(filteredEmployee -> filteredEmployee.getDepartment().getDepartmentName()
							.equals(employee.getDepartment().getDepartmentName())
							&& filteredEmployee.getRole().getRoleName().equals("HOD"))
					.map(filteredEmployee -> new Employee(filteredEmployee.getFirstName(),
							filteredEmployee.getLastName(), filteredEmployee.getEmail(),
							filteredEmployee.getDepartment(), filteredEmployee.getRole()))
					.collect(Collectors.toList());

			if (headEmployees.size() != 1) {
				throw new LeaveSystemException("There is more than one hod in this department");
			}

			oneToBeSentMail = headEmployees.get(0);
			mailSenderService.sendEmail(oneToBeSentMail.getEmail(), "Leave Application",
					employee.getFirstName() + " " + employee.getLastName() + " is applying for leave of type "
							+ leave.getLeaveType().getLeaveName() + " from " + leaveModel.getStartDate() + " to "
							+ leaveModel.getEndDate());

		} else if (employee.getRole().getRoleName().equals("HOD")) {
			List<Employee> hrEmployees = employees.stream()
					.filter(filteredHr -> filteredHr.getRole().getRoleName().equals("HR"))
					.map(filteredHr -> new Employee(filteredHr.getFirstName(), filteredHr.getLastName(),
							filteredHr.getEmail(), filteredHr.getDepartment(), filteredHr.getRole()))
					.collect(Collectors.toList());

			if (hrEmployees.size() != 1) {
				throw new LeaveSystemException("There is more than one hod in this department");
			}
			oneToBeSentMail = hrEmployees.get(0);
			mailSenderService.sendEmail(oneToBeSentMail.getEmail(), "Leave Application",
					employee.getFirstName() + " " + employee.getLastName() + " is applying for leave from "
							+ leaveModel.getStartDate() + " to" + leaveModel.getEndDate());
		} else if (employee.getRole().getRoleName().equals("HR")) {
			List<Employee> hlEmployees = employees.stream()
					.filter(filteredHl -> filteredHl.getRole().getRoleName().equals("HIGHER_LEVEL"))
					.map(filteredHl -> new Employee(filteredHl.getFirstName(), filteredHl.getLastName(),
							filteredHl.getEmail(), filteredHl.getDepartment(), filteredHl.getRole()))
					.collect(Collectors.toList());

			if (hlEmployees.size() != 1) {
				throw new LeaveSystemException("There is more than hod in this department");
			}
			oneToBeSentMail = hlEmployees.get(0);
			mailSenderService.sendEmail(oneToBeSentMail.getEmail(), "Leave Application",
					employee.getFirstName() + " " + employee.getLastName() + " is applying for leave from "
							+ leaveModel.getStartDate() + " to" + leaveModel.getEndDate());
		}

		return leaveModel;

	}

	/*
	 * Approves leave request. Only accessible by Hods' and higher
	 * 
	 * @param Long leaveId is the id of the leave requested
	 * 
	 * @param Long approverId is the id of the one approving the leave
	 */
	public LeaveModel approveLeaveRequest(Long leaveId, LeaveModel leaveModel, Long approverId) {
		Leave leave = leaveRepository.findById(leaveId).get();
		Employee employee = employeeRepository.findById(approverId).get();
		String approved = "Approved";
		String rejected = "Rejected";

		if (leaveModel.getStatus().equals("Approve")) {
			leave.setStatus(approved);
			mailSenderService.sendEmail(leave.getEmployee().getEmail(), "Leave Application Feedback",
					"Dear " + leave.getEmployee().getFirstName() + ", your leave request has been approved.");
		}

		if (leaveModel.getStatus().equals("Reject")) {
			leave.setStatus(rejected);
			mailSenderService.sendEmail(leave.getEmployee().getEmail(), "Leave Application Feedback",
					"Dear " + leave.getEmployee().getFirstName() + ", your leave request has been rejected.");
		}

//		leave.setApprovedBy(employee.getFirstName() + " " + employee.getLastName());
		leave.setApprovedBy(employee.getRole().getRoleName());

		leaveRepository.save(leave);

		return leaveModel;

	}

	// Gets all leave requests
	@Transactional(readOnly = true)
	public List<LeaveModel> getAllLeaves() {

		List<Leave> leaves = leaveRepository.findAll();
		List<LeaveModel> mLeaves = leaves.stream()
				.map(leave -> new LeaveModel(leave.getLeaveId(), leave.getEmployee().getFirstName(),
						leave.getEmployee().getLastName(), leave.getLeaveType().getLeaveName(),
						leave.getStartDate().toString(), leave.getEndDate().toString(), leave.getReason(),
						leave.getStatus(), leave.getDateOfCreation().toString(), leave.getApprovedBy()))
				.collect(Collectors.toList());
		return mLeaves;

	}

	public List<LeaveModel> getHodLeaves() {
		List<Leave> leaves = leaveRepository.findAll();
		List<LeaveModel> mLeaves = leaves.stream()
				.filter(hodLeave -> hodLeave.getEmployee().getRole().getRoleName().equals("HOD"))
				.map(hodLeave -> new LeaveModel(hodLeave.getLeaveId(), hodLeave.getEmployee().getFirstName(),
						hodLeave.getEmployee().getLastName(), hodLeave.getLeaveType().getLeaveName(),
						hodLeave.getStartDate().toString(), hodLeave.getEndDate().toString(), hodLeave.getReason(),
						hodLeave.getStatus(), hodLeave.getDateOfCreation().toString(), hodLeave.getApprovedBy()))
				.collect(Collectors.toList());
		return mLeaves;
	}

	/*
	 * Gets employees by department
	 * 
	 * @param long id is the employee id trying to access the method. Check the
	 * employee department. Role must be hod. If true executes the method
	 * 
	 * @return returns the list by the department the hod heads
	 */
	public List<LeaveModel> getLeavesByDepartment(Long id) {
		List<Leave> leaves = leaveRepository.findAll();
		Employee employee = employeeRepository.findById(id).get();

		List<LeaveModel> departmentalLeaves = leaves.stream()
				.filter(leave -> leave.getEmployee().getDepartment().getDepartmentName()
						.equals(employee.getDepartment().getDepartmentName())
						&& !leave.getEmployee().getRole().getRoleName().equals(employee.getRole().getRoleName()))
				.map(leave -> new LeaveModel(leave.getLeaveId(), leave.getEmployee().getFirstName(),
						leave.getEmployee().getLastName(), leave.getLeaveType().getLeaveName(),
						leave.getStartDate().toString(), leave.getEndDate().toString(), leave.getReason(),
						leave.getStatus(), leave.getDateOfCreation().toString(), leave.getApprovedBy()))
				.collect(Collectors.toList());
		return departmentalLeaves;
	}

	public LeaveModel getLeaveRequestById(Long id) {
		Leave leave = leaveRepository.findById(id).get();
		LeaveModel mLeave = new LeaveModel();

		mLeave.setLeaveId(leave.getLeaveId());
		mLeave.setFirstName(leave.getEmployee().getFirstName());
		mLeave.setLastName(leave.getEmployee().getLastName());
		mLeave.setLeaveType(leave.getLeaveType().getLeaveName());
		mLeave.setStartDate(leave.getStartDate().toString());
		mLeave.setEndDate(leave.getEndDate().toString());
		mLeave.setReason(leave.getReason());
		mLeave.setStatus(leave.getStatus());
		mLeave.setDateOfCreation(leave.getDateOfCreation().toString());
		mLeave.setApprovedBy(leave.getApprovedBy());
		return mLeave;
	}

	public List<LeaveModel> getEmployeeLeaves(Long id) {
		List<Leave> leaves = leaveRepository.findAll();
		List<LeaveModel> mLeaves = leaves.stream().filter(empLeave -> empLeave.getEmployee().getEmployeeId().equals(id))
				.map(empLeave -> new LeaveModel(empLeave.getLeaveId(), empLeave.getEmployee().getFirstName(),
						empLeave.getEmployee().getLastName(), empLeave.getLeaveType().getLeaveName(),
						empLeave.getStartDate().toString(), empLeave.getEndDate().toString(), empLeave.getReason(),
						empLeave.getStatus(), empLeave.getDateOfCreation().toString(), empLeave.getApprovedBy()))
				.collect(Collectors.toList());
		return mLeaves;
	}

	// converts string to date
	private LocalDate convertedDate(String date) {
		LocalDate mDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		return mDate;
	}

}
