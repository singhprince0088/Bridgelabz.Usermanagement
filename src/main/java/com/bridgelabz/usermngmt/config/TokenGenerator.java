package com.bridgelabz.usermngmt.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;

@Configuration
public class TokenGenerator {
	static String tokenKey = "tzABx31ddQLAWEP";
	private static final Logger logger = LoggerFactory.getLogger(TokenGenerator.class);

	public String createToken(long id) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(tokenKey);
			return JWT.create().withClaim("id", id).sign(algorithm);

		} catch (JWTCreationException exception) {
			logger.error("Token encoding exception", exception);
			exception.printStackTrace();
		}
		return null;
	}

	public Long decodeToken(String token) {
		try {
			Verification verification = JWT.require(Algorithm.HMAC256(tokenKey));
			JWTVerifier jwtverifier = verification.build();
			DecodedJWT decodedjwt = jwtverifier.verify(token);
			Claim claim = decodedjwt.getClaim("id");
			return claim.asLong();
		} catch (JWTDecodeException e) {
			logger.error("Token Decoding exception", e);
			e.printStackTrace();
		}
		return null;
	}
}
