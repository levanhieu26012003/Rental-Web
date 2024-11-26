package com.lvh.RentalBE.controllers;

import com.lvh.RentalBE.model.User;

import com.lvh.RentalBE.services.UserService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class UserApiGraphController {
    private final UserService userService;

    public UserApiGraphController(UserService userService) {
        this.userService = userService;
    }

    @QueryMapping
    public User getUserById(@Argument Long id) {
        return userService.findByID(id);
    }
}
