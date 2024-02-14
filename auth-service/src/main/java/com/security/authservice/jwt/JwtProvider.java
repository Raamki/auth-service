package com.security.authservice.jwt;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.security.authservice.dto.response.AuthResponse;
import com.security.authservice.dto.response.UserResponse;
import com.security.authservice.util.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.*;

import static com.security.authservice.util.AuthUtil.buildUserFailureAuthResponse;
import static com.security.authservice.util.AuthUtil.buildUserSuccessAuthResponse;
import static com.security.authservice.util.Constants.*;

@Service
public class JwtProvider {

    @Value("${jwt.secret.password}")
    private String jwtPassword;

    @Value("${jwt.alias}")
    private String jwtAlias;

    private KeyStore keyStore;

    @PostConstruct
    public void init() {
        try {
            keyStore = KeyStore.getInstance("JKS");
            InputStream resourceAsStream = getClass().getResourceAsStream("/authtoken.jks");
            keyStore.load(resourceAsStream, jwtPassword.toCharArray());
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
        }

    }

    public String encryptUserUniqueId(UserResponse user, String jwtSecretKey, int expire) {
        String encryptedToken = null;
        try {
            // header claims
            Map<String, Object> headerClaims = new HashMap<String, Object>();
            headerClaims.put(JWTAuth.JWTClaimKeys.TYPE, JWTAuth.JWTClaimValues.JWT_TYPE);
            headerClaims.put(JWTAuth.JWTClaimKeys.OWNER, JWTAuth.JWTClaimValues.JWT_OWNER);

            // expiry date for jwt token
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, expire);
            Date expiryDate = calendar.getTime();

            // create jwt token
            encryptedToken = Jwts.builder().setSubject(user.getUserId())
                    .setHeader(headerClaims)
                    .setExpiration(expiryDate)
                    .setIssuer(user.getUsername())
                    .claim(JWTAuth.JWTClaimKeys.NAME, user.getUsername())
                    .claim(JWTAuth.JWTClaimKeys.ROLE, user.getRole())
                    .claim(JWTAuth.JWTClaimKeys.PERMISSION, user.getPermission().toString())
                    .claim(JWTAuth.JWTClaimKeys.EMAIL, user.getEmail())
                    .claim(JWTAuth.JWTClaimKeys.ISSUED_AT, new Date())
                    .claim(JWTAuth.JWTClaimKeys.EXPIRE_ON, expiryDate)
                    .signWith(getPrivateKey()).compact();

        } catch (JWTCreationException e1) {
            throw new RuntimeException(Constants.TOKEN_ENCRYPTION_FAILED, e1);
        } catch (Exception ex) {
            throw new RuntimeException(Constants.TOKEN_ENCRYPTION_FAILED, ex);
        }
        return encryptedToken;
    }

    private PrivateKey getPrivateKey() throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException {
        return (PrivateKey) keyStore.getKey("authtoken", jwtPassword.toCharArray());
    }

    public AuthResponse decryptToken(String token, String jwtSecretKey) {
        String userId, username, role;
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(getPublickey())
                    .parseClaimsJws(token)
                    .getBody();
            if (Calendar.getInstance().getTime().after(claims.getExpiration()))
                return buildUserFailureAuthResponse(TOKEN_EXPIRED);
            userId = String.valueOf(claims.getSubject());
            username = claims.getIssuer();
            role = String.valueOf(claims.get(JWTAuth.JWTClaimKeys.ROLE));
            return buildUserSuccessAuthResponse(userId, username, role);
        } catch (TokenExpiredException e0) {
            return buildUserFailureAuthResponse(TOKEN_EXPIRED);
        } catch (Exception e) {
            return buildUserFailureAuthResponse(INVALID_TOKEN);
        }
    }

    private PublicKey getPublickey() throws KeyStoreException {
        return keyStore.getCertificate(jwtAlias).getPublicKey();
    }

    public String getUsernameFromJWT(String token) throws KeyStoreException {
        Claims claims = Jwts.parser()
                .setSigningKey(getPublickey())
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}