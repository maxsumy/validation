package telran.java29.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class UserAuthenticationException extends RuntimeException {
	
	public UserAuthenticationException(String message) {
        super(message);
    }

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	

}
