package org.nds.auth.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.nds.auth.model.AuthenticateRequest;
import org.nds.auth.model.AuthenticateResponse;
import org.nds.auth.model.AuthenticationError;
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

import static org.apache.naming.ResourceRef.SCOPE;

@RestController
public class AuthenticateController {


    private static Logger logger = LoggerFactory.getLogger(AuthenticateController.class);

    private static final String TOKEN_PATH="/token";
    private static final String CLIENT_ID="client_id";

    @PostMapping(value = TOKEN_PATH, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> authenticate(AuthenticateRequest authenticateRequest) {
        ResponseEntity<?> responseEntity;
        String token;

        logger.info("Received authentication request {}", authenticateRequest);

        if(requestBodyIsValid(authenticateRequest.getClient_id(), authenticateRequest.getClient_secret())) {
            logger.info("Received valid authentication request {}", authenticateRequest);

            token = createJWT(authenticateRequest.getClient_secret());

            logger.info("Created JWT {}", token);

            responseEntity = ResponseEntity.ok().body(new AuthenticateResponse(token, ServerProperty.getAuthenticationTokenInterval(),ServerProperty.getAuthenticationTokenType()));

            logger.info("Responding with authentication response {}", responseEntity);
        } else {
            logger.info("Received invalid authentication request {}", authenticateRequest);

            responseEntity = ResponseEntity.badRequest().body(new AuthenticationError("Post contains invalid request body"));
        }

        return responseEntity;
    }

    private boolean requestBodyIsValid(String client_id, String client_Secret) {
        boolean valid=false;

        if((client_id!=null && client_id.equalsIgnoreCase(ServerProperty.getAuthenticationTokenClientId()))
                && (client_Secret!=null && client_Secret.equalsIgnoreCase(ServerProperty.getAuthenticationTokenSecret()))) {
            valid=true;
        }

        return valid;
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
