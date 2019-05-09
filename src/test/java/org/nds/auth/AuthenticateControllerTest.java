package org.nds.auth;

import static org.hamcrest.Matchers.notNullValue;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nds.auth.controller.AuthenticateController;
import org.nds.auth.model.AuthenticateRequest;
import org.nds.auth.model.AuthenticateResponse;
import org.nds.auth.util.ServerProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticateControllerTest {

    private static final String REQUEST_BODY="client_id=%s&client_secret=%s";

    @Autowired
    private MockMvc mvc;

    @Test
    public void getAuth() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .content(String.format(REQUEST_BODY, ServerProperty.getAuthenticationTokenClientId(),
                        ServerProperty.getAuthenticationTokenSecret())))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(notNullValue()));
    }

    @Test
    public void testAuthenticate() {
        AuthenticateController authenticateController;
        ResponseEntity<AuthenticateResponse> responseEntity;

        authenticateController = new AuthenticateController();

        responseEntity = authenticateController.authenticate(new AuthenticateRequest(ServerProperty.getAuthenticationTokenClientId(), ServerProperty.getAuthenticationTokenSecret()));

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertNotNull(responseEntity.getBody().getAccess_token());
        assertNotNull(responseEntity.getBody().getExpires_in());
        assertNotNull(responseEntity.getBody().getToken_type());
        assertEquals(ServerProperty.getAuthenticationTokenInterval(), responseEntity.getBody().getExpires_in());
        assertEquals(ServerProperty.getAuthenticationTokenType(), responseEntity.getBody().getToken_type());
    }
}
