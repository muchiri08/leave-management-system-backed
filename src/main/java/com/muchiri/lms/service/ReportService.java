package com.muchiri.lms.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.muchiri.lms.entity.Leave;
import com.muchiri.lms.exception.LeaveSystemException;
import com.muchiri.lms.model.LeaveModel;
import com.muchiri.lms.repository.LeaveRepository;

import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
@RequiredArgsConstructor
public class ReportService {

	private final LeaveRepository leaveRepository;

	public ResponseEntity<byte[]> generateLeaveReport(String startDate, String endDate, String format)
			throws FileNotFoundException, JRException {
		LocalDate sDate = convertedDate(startDate);
		LocalDate eDate = convertedDate(endDate);

		List<Leave> leaves = leaveRepository.getLeaveBetween2Dates(LocalDateTime.of(sDate, LocalTime.MIN),
				LocalDateTime.of(eDate, LocalTime.MAX));
		
		if (leaves.isEmpty()) {
			throw new LeaveSystemException("No leaves found for the specified dates");
		}

		List<LeaveModel> mLeaves = leaves.stream()
				.map(report -> new LeaveModel(report.getLeaveId(), report.getEmployee().getFirstName(),
						report.getEmployee().getLastName(), report.getEmployee().getDepartment().getDepartmentName(),
						report.getLeaveType().getLeaveName(), report.getStartDate().toString(),
						report.getEndDate().toString(), report.getReason(), report.getStatus(),
						report.getDateOfCreation().toString(), report.getApprovedBy()))
				.collect(Collectors.toList());

		File file = ResourceUtils.getFile("classpath:leave_report.jrxml");
		JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(mLeaves);
		Map<String, Object> parameter = new HashMap<>();
		parameter.put("createdBy", "Muchiri Kennedy");
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, dataSource);

		HttpHeaders headers = new HttpHeaders();

		if (format.equalsIgnoreCase("pdf")) {
			headers.setContentType(MediaType.APPLICATION_PDF);
			headers.setContentDispositionFormData("filename", "leaves-report.pdf");
			return new ResponseEntity<byte[]>(JasperExportManager.exportReportToPdf(jasperPrint), headers,
					HttpStatus.OK);
		} else {
			return new ResponseEntity<byte[]>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private LocalDate convertedDate(String date) {
		LocalDate mDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		return mDate;
	}

}
