package com.lvh.RentalBE.controllers;


import com.lvh.RentalBE.dto.UserDTO;
import com.lvh.RentalBE.mapper.UserMapper;
import com.lvh.RentalBE.model.User;
import com.lvh.RentalBE.services.TokenService;
import com.lvh.RentalBE.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;


@RestController
public class AuthController {
    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);
    private final UserService userService ;
    private final TokenService tokenService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final BCryptPasswordEncoder passEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthController(TokenService tokenService, UserService userService, AuthenticationManagerBuilder authenticationManagerBuilder, BCryptPasswordEncoder passEncoder, AuthenticationManager authenticationManager ) {
        this.tokenService = tokenService;
        this.userService = userService;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.passEncoder = passEncoder;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/api/token")
    public ResponseEntity<String> login(@RequestBody  Map<String, String>  authRequest) {
        try {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(authRequest.get("username"), authRequest.get("password"));
            Authentication authentication = authenticationManager.authenticate(authToken);
            String token = tokenService.generateToken(authentication);
            return ResponseEntity.ok(token);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @GetMapping(path = "/api/current-user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> getCurrentUser(Principal principal) {
        User user = this.userService.findByUsername(principal.getName());
        UserDTO userDTO = UserMapper.toDTO(user);
        userDTO.setFollower(UserMapper.toDTOList(user.getUserCollection1()));
        userDTO.setFollowing(UserMapper.toDTOList(user.getUserCollection()));

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

}