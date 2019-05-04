package org.nds.auth;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.*;

@RestController
public class AuthenticateController {


    private static Logger logger = LoggerFactory.getLogger(AuthenticateController.class);

    private String HS256_KEY="7OQdSnjzcne1NZBP8Sn671G5CS2kvGUtpG5HBjSNlrA";

    @RequestMapping("/")
    public String blank() {
        return "";
    }

    @PostMapping(value = "/auth", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<AuthenticateResponse> authenticate(AuthenticateRequest authenticateRequest) {

        logger.info("Received authentication request", authenticateRequest);

        return ResponseEntity.accepted().body(new AuthenticateResponse(createJWT(authenticateRequest.getClient_id(), authenticateRequest.getClient_secret()),"3600","Bearer"));
    }

    private String createJWT(String client_id, String client_secret) {
        Date now = new Date();

        String keyString = HS256_KEY + Base64.getEncoder().encodeToString(client_secret.getBytes());

        Key key = new SecretKeySpec(keyString.getBytes(), SignatureAlgorithm.HS256.getJcaName());

        return Jwts.builder().signWith(key)
                .setNotBefore(now)
                .setExpiration(getExpirationDate(now))
                .setIssuer("NDS")
                .setAudience("DHP")
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

        scope[0] = "msg.dvanotifr";

        map = new HashMap<>();
        map.put("client_id","dhp_msg");
        map.put("scope",scope);

        return map;
    }
}
