package telran.java29.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Claims;
import telran.java29.service.ValidationService;

@RestController
@RequestMapping("/propets/en/search/v1")

public class ValidationController {
	
	@Autowired
	ValidationService service;

	@GetMapping("/validation")
	public ResponseEntity<String> client(@RequestHeader(value="X-Token") String xToken) {
		
		return service.checkToken(xToken);
				
	}
	
}
