package org.nds.auth;

import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
public class AuthenticateResponse {

    private String accessToken;
    private String expiresIn;
    private String tokenType;

    public AuthenticateResponse(String accessToken, String expiresIn, String tokenType) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.tokenType = tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
}
