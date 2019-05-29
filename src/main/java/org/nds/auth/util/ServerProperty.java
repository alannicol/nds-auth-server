package org.nds.auth.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.util.Properties;

public class ServerProperty {

    private static Logger logger = LoggerFactory.getLogger(ServerProperty.class);

    private static final String SERVER_PROPERTIES="server.properties";
    private static final String SECURITY_PROPERTIES="security.properties";
    private static final String AUTHENTICATION_TOKEN_PRIVATE_KEY ="authentication.token.private.key";
    private static final String AUTHENTICATION_TOKEN_SECRET ="authentication.token.secret";
    private static final String AUTHENTICATION_TOKEN_INTERVAL ="authentication.token.interval";
    private static final String AUTHENTICATION_TOKEN_TYPE ="authentication.token.type";
    private static final String AUTHENTICATION_TOKEN_ISSUER ="authentication.token.issuer";
    private static final String AUTHENTICATION_TOKEN_AUDIENCE ="authentication.token.audience";
    private static final String AUTHENTICATION_TOKEN_CLIENT_ID ="authentication.token.client.id";
    private static final String AUTHENTICATION_TOKEN_SCOPE ="authentication.token.scope";

    private static Properties serverProperties = obtainProperties(SERVER_PROPERTIES);
    private static Properties securityProperties = obtainProperties(SECURITY_PROPERTIES);

    public static String getAuthenticationTokenPrivateKey() {
        return securityProperties.get(AUTHENTICATION_TOKEN_PRIVATE_KEY).toString();
    }

    public static String getAuthenticationTokenSecret() {
        return securityProperties.get(AUTHENTICATION_TOKEN_SECRET).toString();
    }

    public static String getAuthenticationTokenInterval() {
        return serverProperties.get(AUTHENTICATION_TOKEN_INTERVAL).toString();
    }

    public static String getAuthenticationTokenType() {
        return serverProperties.get(AUTHENTICATION_TOKEN_TYPE).toString();
    }

    public static String getAuthenticationTokenIssuer() {
        return serverProperties.get(AUTHENTICATION_TOKEN_ISSUER).toString();
    }

    public static String getAuthenticationTokenAudience() {
        return serverProperties.get(AUTHENTICATION_TOKEN_AUDIENCE).toString();
    }

    public static String getAuthenticationTokenClientId() {
        return serverProperties.get(AUTHENTICATION_TOKEN_CLIENT_ID).toString();
    }

    public static String getAuthenticationTokenScope() {
        return serverProperties.get(AUTHENTICATION_TOKEN_SCOPE).toString();
    }

    private static Properties obtainProperties(String propertiesFile) {
        Properties properties=null;

        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(ServerProperty.class.getClassLoader().getResourceAsStream(propertiesFile))) {
            properties = new Properties();
            properties.load(bufferedInputStream);
        } catch(Exception exception) {
            logger.error("Cannot obtain properties file", exception);
        }

        return properties;
    }
}
