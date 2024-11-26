/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lvh.RentalBE.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lvh.RentalBE.model.User;
import com.lvh.RentalBE.services.UserService;
import com.lvh.RentalBE.services.VipPackageService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author levan
 */
@RestController
@RequestMapping("/api/package")
public class VipPacakgeApiController {

    private final VipPackageService vipPackageService;
    public VipPacakgeApiController(VipPackageService vipPackageService) {
        this.vipPackageService = vipPackageService;
    }


    @GetMapping
    public ResponseEntity<String> checkRegisterPackage(@RequestParam Map<String, String> request) {
        return ResponseEntity.ok(this.vipPackageService.checkRegistedPackage(Long.parseLong(request.get("id")),Long.parseLong(request.get("vipPackageId"))));
    }


}


