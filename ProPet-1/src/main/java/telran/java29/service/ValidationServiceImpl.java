package telran.java29.service;

import java.security.Key;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;


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

@Service
public class ValidationServiceImpl implements ValidationService {

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
	    responseHeaders.set("X-Token",createJWT("111", "Max", "all rigts", 1500000));
	    	 
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

}
