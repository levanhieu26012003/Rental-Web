package com.lvh.RentalBE.controllers;

import com.lvh.RentalBE.services.MotelService;
import com.lvh.RentalBE.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Map;

@Controller
@RequestMapping("/motels")
public class IndexController {

    @Autowired
    private MotelService motelService;

    @Autowired
    private UserService userService;

    @ModelAttribute
    public void commonAttr(Model model) {
        model.addAttribute("users", this.userService.findAll());
    }

    @RequestMapping("")
    public String index(Model model) {
        model.addAttribute("motels", this.motelService.getAllMotels());
        return "index";
    }
}
