package org.nds.auth.model;

import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
public class AuthenticateResponse {

    private String access_token;
    private String expires_in;
    private String token_type;

    public AuthenticateResponse(String accessToken, String expiresIn, String tokenType) {
        this.access_token = accessToken;
        this.expires_in = expiresIn;
        this.token_type = tokenType;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }
}
