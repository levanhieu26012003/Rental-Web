/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lvh.RentalBE.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lvh.RentalBE.model.Motel;
import com.lvh.RentalBE.model.User;
import com.lvh.RentalBE.services.MotelService;
import com.lvh.RentalBE.services.UserService;
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
@RequestMapping("/api/user")
public class UserApiController {

    private final UserService userService;
    public UserApiController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "", consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE})
//    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> create(@RequestParam Map<String, String> params, @RequestPart MultipartFile avatar) {
       try{
           ObjectMapper objectMapper = new ObjectMapper();
           User u= objectMapper.convertValue(params, User.class);
           u.setFile(avatar);
           userService.save(u);
           return ResponseEntity.ok("User Created");
       } catch (Exception e) {
           System.out.println(e);
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            System.out.println(e.getLocalizedMessage());
       }

       return   ResponseEntity.badRequest().build();

    }

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

//    @PutMapping("/{id}")
//    public Motel updateMotel(@PathVariable Long id, @RequestBody Motel Motel) {
//        Motel.setId(id);
//        return motelService.saveMotel(Motel);
//    }
//
//    @DeleteMapping("/{id}")
//    public void deleteMotel(@PathVariable Long id) {
//        motelService.deleteMotelById(id);
//    }
}


