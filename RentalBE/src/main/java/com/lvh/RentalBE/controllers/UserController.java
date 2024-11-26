package com.lvh.RentalBE.controllers;

import com.lvh.RentalBE.model.User;
import com.lvh.RentalBE.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/users")

public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping
    public String index(Model model, @RequestParam Map<String, String> params) {
        model.addAttribute("users", this.userService.findByUserRole(params.get("role")));
        return "users";
    }

    @GetMapping("/user")
    public String createView(Model model) {
        model.addAttribute("user", new User());
        return "user";
    }

    @GetMapping("/user/{username}")
    public String updateView(Model model, @PathVariable(value = "username") String username) {
        model.addAttribute("user", this.userService.findByUsername(username));
        return "user";
    }

    @PostMapping("/user")
    public String createUser(@ModelAttribute(value ="user")User u) {
        try {
            this.userService.save(u);
            return "redirect:/users";
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return "user";
    }

    @GetMapping("/user/delete/{username}")
    public String delete(Model model, @PathVariable(value = "username") String username) {
        this.userService.delete(username);
        return "redirect:/users";
    }
}

