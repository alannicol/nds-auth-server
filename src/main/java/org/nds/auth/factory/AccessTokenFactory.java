package org.nds.auth.factory;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.nds.auth.util.ServerProperty;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.Instant;
import java.util.*;

public class AccessTokenFactory {

    private static final String CLIENT_ID="client_id";
    private static final String SCOPE="scope";

    public static String create(String clientSecret) {
        Date now = Date.from(Instant.now());

        String keyString = ServerProperty.getAuthenticationTokenPrivateKey() + Base64.getEncoder().encodeToString(clientSecret.getBytes());

        Key key = new SecretKeySpec(keyString.getBytes(), SignatureAlgorithm.HS256.getJcaName());

        return Jwts.builder().signWith(key)
                .setNotBefore(now)
                .setExpiration(getExpirationDate(now))
                .setIssuer(ServerProperty.getAuthenticationTokenIssuer())
                .setAudience(ServerProperty.getAuthenticationTokenAudience())
                .addClaims(createClaims())
                .compact();
    }

    private static Date getExpirationDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, 1);

        return cal.getTime();
    }

    private static Map<String, Object> createClaims() {
        Map<String, Object> map;
        String scope[] = new String[1];

        scope[0] = ServerProperty.getAuthenticationTokenScope();

        map = new HashMap<>();
        map.put(CLIENT_ID,ServerProperty.getAuthenticationTokenClientId());
        map.put(SCOPE,scope);

        return map;
    }
}

