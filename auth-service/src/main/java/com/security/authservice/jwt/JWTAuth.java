package com.security.authservice.jwt;

public class JWTAuth {

	public interface JWTClaimKeys {
		String TYPE = "typ";
		String OWNER = "owner";
		String NAME = "name";
		String ROLE = "role";
		String PERMISSION = "permissions";
		String EMAIL = "email";
		String ISSUED_AT = "iat";
		String EXPIRE_ON = "exp";
	}

	/**
	 * This interface used to define the list of claim values to generate JWT token.
	 *
	 */
	public interface JWTClaimValues {
		String JWT_TYPE = "JWT";
		String JWT_OWNER = "OWNER";
	}
}