package com.example.passkey.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/")
    public String hello(@AuthenticationPrincipal OidcUser user) throws JsonProcessingException {
        return objectMapper.writeValueAsString(user);
    }
}
