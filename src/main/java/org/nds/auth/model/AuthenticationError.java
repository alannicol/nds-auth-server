package org.nds.auth.model;

public class AuthenticationError {

    private String error;

    public AuthenticationError(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
