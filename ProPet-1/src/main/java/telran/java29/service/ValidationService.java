package telran.java29.service;

import org.springframework.http.ResponseEntity;

import io.jsonwebtoken.Claims;

public interface ValidationService {

	ResponseEntity<String> checkToken(String xToken);

}
