package com.muchiri.lms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.muchiri.lms.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	Role getByRoleName(String role);

	Optional<Role> findByRoleName(String role);

}
