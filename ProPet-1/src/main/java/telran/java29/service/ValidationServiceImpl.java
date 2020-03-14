package telran.java29.service;

import java.security.Key;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import telran.java29.configuration.AccountConfiguration;
import telran.java29.configuration.AccountUserCredentials;
import telran.java29.dao.UserAccountRepository;
import telran.java29.domain.UserAccount;
import telran.java29.dto.UserProfileDto;
import telran.java29.dto.UserRegDto;
import telran.java29.exception.UserAuthenticationException;
import telran.java29.exception.UserConflictException;


@Service
public class ValidationServiceImpl implements ValidationService {
	
	@Autowired
	UserAccountRepository userRepository;
	
	@Autowired
	AccountConfiguration accountConfiguration;

//	@Value("${exp.secret_key}")

	private static String SECRET_KEY = "oeRaYY7W4cVREf9cUsprCRK93w";
	
	
//	@Autowired
//	private static Environment env;
//	private static String SECRET_KEY = env.getProperty("secret.key");

//	@Value("${secret.key}")
//	private static String SECRET_KEY;

//	@ManagedAttribute
//	public String getSecretKey() {
//		return SECRET_KEY;
//	}
//
//	@ManagedAttribute
//	public void setSecretKey(String SECRET_KEY) {
//		this.SECRET_KEY = SECRET_KEY;
//	}
	
	@Override
	public ResponseEntity<String> checkToken(String xToken) {

//		return createJWT("111", "Max", "all rigts", 1500000);
//		return decodeJWT(xToken);
		HttpHeaders responseHeaders = new HttpHeaders();
	    responseHeaders.set("X-Token",createJWT("111", "Max", "all rigts", 3_600_000));
	    	 
	    	    return ResponseEntity.ok()
	    	      .headers(responseHeaders)
	    	      .body("");
	}

	
	@Override
	public UserProfileDto addUser(UserRegDto userRegDto) {
		if(userRepository.existsById(userRegDto.getEmail())) {
			throw new UserConflictException();
		}
		String hashPassword = BCrypt.hashpw(userRegDto.getPassword(), BCrypt.gensalt());
		UserAccount userAccount = UserAccount.builder()				
				.email(userRegDto.getEmail())
				.login(userRegDto.getLogin())
				.password(hashPassword)
				.role("User")
				.xToken(createJWT("111", userRegDto.getEmail(), "all rigts", 3_600_000))
				.build();
		
		userRepository.save(userAccount);
		
		return convertToUserProfileDto(userAccount);
	}
	
	@Override
	public ResponseEntity<String> login(String auth) {
		if(auth == null) {
			throw new UserAuthenticationException("No header Authorization");
		}
		
		AccountUserCredentials credentials = accountConfiguration.tokenDecode(auth);
		UserAccount userAccount = userRepository.findById(credentials.getLogin()).orElse(null);
		if (userAccount == null) {
			throw new UserAuthenticationException("User not exists");
		} else {
			if (!BCrypt.checkpw(credentials.getPassword(), userAccount.getPassword())) {
				throw new UserAuthenticationException("User or password is not correct");
				}
		}
		
		HttpHeaders responseHeaders = new HttpHeaders();
	    responseHeaders.set("X-Token",createJWT("111", userAccount.getEmail(), "all rigts", 3_600_000));
	    responseHeaders.set("User", userAccount.getLogin());
	    	 
	    return ResponseEntity.ok()
	    	      .headers(responseHeaders)
	    	      .body("");
		
		
	}
	
	public static String createJWT(String id, String issuer, String subject, long ttlMillis) {

		// The JWT signature algorithm we will be using to sign the token
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);

		// We will sign our JWT with our ApiKey secret
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

		// Let's set the JWT Claims
		JwtBuilder builder = Jwts.builder().setId(id).setIssuedAt(now).setSubject(subject).setIssuer(issuer)
				.signWith(signatureAlgorithm, signingKey);

		// if it has been specified, let's add the expiration
		if (ttlMillis >= 0) {
			long expMillis = nowMillis + ttlMillis;
			Date exp = new Date(expMillis);
			builder.setExpiration(exp);
		}

		// Builds the JWT and serializes it to a compact, URL-safe string
		return builder.compact();
	}

	public static Claims decodeJWT(String jwt) {

		// This line will throw an exception if it is not a signed JWS (as expected)
		Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY)).parseClaimsJws(jwt)
				.getBody();
		
		
		
		return claims;
	}

	private UserProfileDto convertToUserProfileDto(UserAccount userAccount) {
		
		return UserProfileDto.builder()
				.login(userAccount.getLogin())
				.email(userAccount.getEmail())
				.roles(userAccount.getRoles())
				.xToken(userAccount.getXToken())
				.build();
	}

	

}
