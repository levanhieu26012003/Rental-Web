package com.lvh.RentalBE.controllers;

import com.lvh.RentalBE.model.Vippackage;
import com.lvh.RentalBE.services.VipPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/packages")
public class VipPackageController {
    private final VipPackageService vipPackageService;

    public VipPackageController(VipPackageService vipPackageService) {
        this.vipPackageService = vipPackageService;
    }

    @RequestMapping
    public String index(Model model) {
        model.addAttribute("packages", this.vipPackageService.getAllVipPackages());
        return "packages";
    }

    @GetMapping("/package")
    public String createView(Model model) {
        model.addAttribute("package", new Vippackage());
        return "package";
    }

    @GetMapping("/package/{id}")
    public String updateView(Model model, @PathVariable(value = "id") Long id) {
        model.addAttribute("package", this.vipPackageService.findById(id).get());
        return "package";
    }

    @PostMapping("/package")
    public String createPackage(@ModelAttribute(value ="package")Vippackage vippackage) {
        try {
            this.vipPackageService.save(vippackage);
            return "redirect:/packages";
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return "package";
    }

    @GetMapping("/package/delete/{id}")
    public String delete(Model model, @PathVariable(value = "id") Long id) {
        this.vipPackageService.delete(id);
        return "redirect:/packages";
    }


}

