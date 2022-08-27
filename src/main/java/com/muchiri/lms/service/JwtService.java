package com.muchiri.lms.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.muchiri.lms.entity.Employee;
import com.muchiri.lms.exception.InvalidUserInputException;
import com.muchiri.lms.exception.LeaveSystemException;
import com.muchiri.lms.model.JwtRequestModel;
import com.muchiri.lms.model.JwtResponseModel;
import com.muchiri.lms.repository.EmployeeRepository;
import com.muchiri.lms.util.JwtUtil;

@Service
public class JwtService implements UserDetailsService {
	
	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private AuthenticationManager authenticationManager;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Employee employee = employeeRepository.findByEmail(email).get();

		if (employee != null) {
			return new User(employee.getEmail(), employee.getPassword(), getAuthorities(employee));
		} else {
			throw new LeaveSystemException("Email not found");
		}

	}

	public JwtResponseModel createJwtToken(JwtRequestModel jwtRequest) throws Exception {

		String email = jwtRequest.getEmail();
		String password = jwtRequest.getPassword();

		authenticate(email, password);

		final UserDetails userDetails = loadUserByUsername(email);

		String newGeneratedToken = jwtUtil.generateToken(userDetails);

		Employee employee = employeeRepository.findByEmail(email).get();

		return new JwtResponseModel(employee, newGeneratedToken);
	}

	private Set getAuthorities(Employee employee) {
		Set authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_" + employee.getRole().getRoleName()));

		return authorities;
	}

	private void authenticate(String email, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
		} catch (BadCredentialsException e) {
			throw new InvalidUserInputException("Bad credentials from user!");
		} catch (DisabledException e) {
			throw new LeaveSystemException("User is disabled!");
		}
	}

}
