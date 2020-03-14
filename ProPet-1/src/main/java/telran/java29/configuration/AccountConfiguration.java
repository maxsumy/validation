package telran.java29.configuration;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;

import telran.java29.exception.UserAuthenticationException;



@Configuration
public class AccountConfiguration {
	
	
	
	public AccountUserCredentials tokenDecode(String auth) {
		try {
			int pos = auth.indexOf(" ");
			String token = auth.substring(pos + 1);
			byte[] decodeBytes = Base64.getDecoder().decode(token);
			String credential = new String(decodeBytes);
			String[] credentials = credential.split(":");
			return new AccountUserCredentials(credentials[0], credentials[1]);
		} catch (Exception e) {
			throw new UserAuthenticationException("Error");
		}
	}

}
