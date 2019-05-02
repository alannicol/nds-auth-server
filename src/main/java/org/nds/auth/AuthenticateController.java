package org.nds.auth;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticateController {

    @RequestMapping("/")
    public String blank() {
        return "";
    }

    @RequestMapping("/auth")
    public String authenticate() {
        return "Greetings from Authentication server!";
    }
}
