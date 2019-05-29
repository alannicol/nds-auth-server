package org.nds.auth.controller;

import org.nds.auth.factory.AccessTokenFactory;
import org.nds.auth.model.AuthenticateRequest;
import org.nds.auth.model.AuthenticateResponse;
import org.nds.auth.util.ServerProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticateController {


    private static Logger logger = LoggerFactory.getLogger(AuthenticateController.class);

    private static final String TOKEN_PATH="/token";

    @PostMapping(value = TOKEN_PATH, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<AuthenticateResponse> authenticate(AuthenticateRequest authenticateRequest) {
        ResponseEntity<AuthenticateResponse> responseEntity;
        String token;

        logger.info("Received authentication request {}", authenticateRequest);

        token = AccessTokenFactory.create(authenticateRequest.getClient_secret());

        logger.info("Created JWT {}", token);

        responseEntity = ResponseEntity.ok().body(new AuthenticateResponse(token, ServerProperty.getAuthenticationTokenInterval(),ServerProperty.getAuthenticationTokenType()));

        logger.info("Responding with authentication response {}", responseEntity);

        return responseEntity;
    }
}
