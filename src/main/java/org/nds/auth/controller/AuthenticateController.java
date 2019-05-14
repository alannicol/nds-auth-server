package org.nds.auth.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.nds.auth.model.AuthenticateRequest;
import org.nds.auth.model.AuthenticateResponse;
import org.nds.auth.util.ServerProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.Instant;
import java.util.*;

@RestController
public class AuthenticateController {

    private static Logger logger = LoggerFactory.getLogger(AuthenticateController.class);

    private static final String TOKEN_PATH="/token";
    private static final String CLIENT_ID="client_id";
    private static final String SCOPE="scope";

    @PostMapping(value = TOKEN_PATH, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<AuthenticateResponse> authenticate(AuthenticateRequest authenticateRequest) {
        ResponseEntity<AuthenticateResponse> responseEntity;
        String token;

        logger.info("Received authentication request {}", authenticateRequest);

        token = createJWT(authenticateRequest.getClient_secret());

        logger.info("Created JWT {}", token);

        responseEntity = ResponseEntity.ok().body(new AuthenticateResponse(token, ServerProperty.getAuthenticationTokenInterval(),ServerProperty.getAuthenticationTokenType()));

        logger.info("Responding with authentication response {}", responseEntity);

        return responseEntity;
    }

    private String createJWT(String client_secret) {
        Date now = Date.from(Instant.now());

        String keyString = ServerProperty.getAuthenticationTokenPrivateKey() + Base64.getEncoder().encodeToString(client_secret.getBytes());

        Key key = new SecretKeySpec(keyString.getBytes(), SignatureAlgorithm.HS256.getJcaName());

        return Jwts.builder().signWith(key)
                .setNotBefore(now)
                .setExpiration(getExpirationDate(now))
                .setIssuer(ServerProperty.getAuthenticationTokenIssuer())
                .setAudience(ServerProperty.getAuthenticationTokenAudience())
                .addClaims(createClaims())
                .compact();
    }

    private Date getExpirationDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, 1);

        return cal.getTime();
    }

    private Map<String, Object> createClaims() {
        Map<String, Object> map;
        String scope[] = new String[1];

        scope[0] = ServerProperty.getAuthenticationTokenScope();

        map = new HashMap<>();
        map.put(CLIENT_ID,ServerProperty.getAuthenticationTokenClientId());
        map.put(SCOPE,scope);

        return map;
    }
}
