package com.muchiri.lms.service;

import org.springframework.stereotype.Service;

import com.muchiri.lms.entity.Role;
import com.muchiri.lms.repository.RoleRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RoleService {

	private final RoleRepository roleRepository;

	//initializes roles in the database
	public void initialiseRoles() {
		Role admin = new Role(null,"ADMIN");
		Role hr = new Role(null, "HR");
		Role hod = new Role(null, "HOD");
		Role higherLevel = new Role(null, "HIGHER_LEVEL");
		Role casualEmpl = new Role(null, "CASUAL_EMPLOYEE");
		
		roleRepository.save(admin);
		roleRepository.save(casualEmpl);
		roleRepository.save(higherLevel);
		roleRepository.save(hod);
		roleRepository.save(hr);		

	}

	// gets role by name
	public Role getByRoleName(String role) {
		return roleRepository.getByRoleName(role);
	}

	// Finds role by name.
	// return true if found otherwise false
	public boolean findByRoleName(String role) {
		return roleRepository.findByRoleName(role).isPresent();
//		if (roleRepository.findByRoleName(role).isPresent()) {
//			return true;
//		} else {
//			return false;
//		}
	}

}
