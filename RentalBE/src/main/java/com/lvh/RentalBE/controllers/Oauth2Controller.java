package com.lvh.RentalBE.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/oauth2")
public class Oauth2Controller {
    @GetMapping("")
    public Map<String,Object> createUserOauth2(@AuthenticationPrincipal OAuth2User principal) {
        return Collections.singletonMap("name", principal);
    }
}
