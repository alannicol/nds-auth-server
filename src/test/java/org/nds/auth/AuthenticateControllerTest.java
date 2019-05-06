package org.nds.auth;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticateControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void getAuth() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .content("client_id=dhp_msg&client_secret=secret"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(notNullValue()));
    }

    @Test
    public void testAuthenticate() {
        AuthenticateController authenticateController;
        ResponseEntity<AuthenticateResponse> responseEntity;

        authenticateController = new AuthenticateController();

        responseEntity = authenticateController.authenticate(new AuthenticateRequest("dhp_msg", "qtXfG3VtUbsCN4v8s20gA9ClajzAzo"));

        assertNotNull(responseEntity.getBody());
    }
}
