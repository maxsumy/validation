package telran.java29.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class UserConflictException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
