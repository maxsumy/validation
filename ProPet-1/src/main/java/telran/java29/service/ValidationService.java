package telran.java29.service;

import org.springframework.http.ResponseEntity;

import io.jsonwebtoken.Claims;
import telran.java29.dto.UserProfileDto;
import telran.java29.dto.UserRegDto;

public interface ValidationService {

	ResponseEntity<String> checkToken(String xToken);

	UserProfileDto addUser(UserRegDto userRegDto);

	ResponseEntity<String> login(String auth);

}
