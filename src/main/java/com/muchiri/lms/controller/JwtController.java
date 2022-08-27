package com.muchiri.lms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.muchiri.lms.model.JwtRequestModel;
import com.muchiri.lms.model.JwtResponseModel;
import com.muchiri.lms.service.JwtService;

@RestController
@CrossOrigin
public class JwtController {
	
	@Autowired
	private JwtService jwtService;
	
	@PostMapping({"/authenticate"})
	public JwtResponseModel createJwtToken(@RequestBody JwtRequestModel jwtRequest) throws Exception {
		return jwtService.createJwtToken(jwtRequest);
	}

}
