package com.muchiri.lms.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Balance implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Id
	private Long employeeId;
	private Integer leaveBalance;

}
