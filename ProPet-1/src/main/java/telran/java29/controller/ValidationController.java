package telran.java29.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import telran.java29.dto.UserProfileDto;
import telran.java29.dto.UserRegDto;
import telran.java29.service.ValidationService;

@RestController
@RequestMapping("/propets/en/search/v1")

public class ValidationController {

	@Autowired
	ValidationService service;

	@PostMapping("/register")
	public UserProfileDto register(@RequestBody UserRegDto userRegDto) {
		return service.addUser(userRegDto);
	}

	@GetMapping("/validation")
	public ResponseEntity<String> checkToken(@RequestHeader(value = "X-Token") String xToken) {

		return service.checkToken(xToken);

	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestHeader(value = "Authorization") String auth) {

		return service.login(auth);

	}

}
