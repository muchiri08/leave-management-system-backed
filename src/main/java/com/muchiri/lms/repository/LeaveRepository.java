package com.muchiri.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.muchiri.lms.entity.Leave;

@Repository
public interface LeaveRepository extends JpaRepository<Leave, Long> {

}
