package com.lvh.RentalBE.controllers;

import com.lvh.RentalBE.model.Motel;
import com.lvh.RentalBE.services.MotelService;
import com.lvh.RentalBE.services.UserService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@ControllerAdvice
@RequestMapping("/motels")
public class MotelController {

    private final MotelService motelService;

    private final UserService userService;

    public MotelController(MotelService motelService, UserService userService) {
        this.motelService = motelService;
        this.userService = userService;
    }

    @ModelAttribute
    public void commonAttr(Model model) {
        model.addAttribute("users", this.userService.findAll());
    }

//    @GetMapping()
//    public String listMotels(Model model) {
//        List<Motel> motels = motelService.getAllMotels();
//        model.addAttribute("motels", motels);
//        return "index";
//    }

    @GetMapping()
    public String listMotels(@RequestParam(required = false) String sort, @RequestParam(required = false) String status,  @RequestParam(required = false) String active, Model model) {

        List<Motel> motels = motelService.getAllMotels(sort, status, active);
        model.addAttribute("motels", motels);
        model.addAttribute("sort", sort);
        model.addAttribute("status", status);
        model.addAttribute("active", active);
        return "index";
    }

    @GetMapping("/motel")
    public String createMotelView(Model model) {
        model.addAttribute("motel", new Motel());
        return "motelDetail"; // Tên định nghĩa trong tiles.xml
    }

    @GetMapping("/motel/{motelId}")
    public String updateMotelView(Model model, @PathVariable(value = "motelId") Long id) {
        Motel motel = this.motelService.getMotelById(id);
        model.addAttribute("motel", motel);
        return "motelDetail";
    }

    @PostMapping("/motel")
    public String saveMotel(@ModelAttribute(value = "motel") Motel m, BindingResult result, Model model) {

        try {
            if (m.getStatus() == "PENDING") {
                m.setStatus("APPROVED");
            }
            this.motelService.saveMotel(m);
            return "redirect:/motels";
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return "motelDetail";
    }



    @GetMapping("deleteMotel/{id}")
    public String deleteMotel(@PathVariable Long id) {
        motelService.deleteMotelById(id);
        return "redirect:/motels";
    }

    @GetMapping("changeActivation/{id}")
    public String changeActivation(@PathVariable Long id) {
        motelService.updateActive(id);
        return "redirect:/motels";
    }
}