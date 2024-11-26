/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lvh.RentalBE.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lvh.RentalBE.model.Motel;
import com.lvh.RentalBE.model.User;
import com.lvh.RentalBE.model.UserVipPackage;
import com.lvh.RentalBE.services.MotelService;
import com.lvh.RentalBE.services.UserService;
import com.lvh.RentalBE.services.VipPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author levan
 */
@RestController
@RequestMapping("/api")
public class MotelApiController {
    private final MotelService motelService;
    private final UserService userService;
    private final ObjectMapper objectMapper;
    private final VipPackageService vipPackageService;
    public MotelApiController(MotelService motelService, UserService userService, ObjectMapper objectMapper, VipPackageService vipPackageService) {
        this.motelService = motelService;
        this.userService = userService;
        this.objectMapper = objectMapper;
        this.vipPackageService = vipPackageService;
    }
    @PostMapping(path = "/createMotel", consumes = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE})
//    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> create(@RequestParam Map<String, String> params, @RequestPart List<MultipartFile> files) {
        // kiểm tra người dùng có vượt giới hạn khoong , đếm số bài viết, kiểm tra gói nang cấp hiện tại
        Motel motel = objectMapper.convertValue(params, Motel.class);
        User u = userService.findByID(Long.parseLong(params.get("userId")));

        Optional<UserVipPackage> u_v = this.vipPackageService.checkUserVipPackage(Long.parseLong(params.get("userId")));
        Integer limitTime = u_v.isEmpty()?5:vipPackageService.findById(u_v.get().getUserVipPackagePK().getVipPackageId()).get().getLimitTime();
        List<Motel> motels = u.getMotelCollection().stream()
                .filter(Motel::getActive)
                .collect(Collectors.toList());

        if (limitTime > u.getMotelCollection().size()){
            motel.setUserId(u);
            motel.setFiles(files);
            motelService.saveMotel(motel);
            return new ResponseEntity<>("Giá trị hợp lệ và đã tạo thành công", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Giá trị vượt quá giới hạn", HttpStatus.BAD_REQUEST);

    }
    @PutMapping("/{id}")
    public Motel updateMotel(@PathVariable Long id, @RequestBody Motel Motel) {
        Motel.setId(id);
        return motelService.saveMotel(Motel);
    }
    @DeleteMapping("deleteMotel/{id}")
    public void deleteMotel(@PathVariable Long id) {
        motelService.deleteMotelById(id);
    }
    @GetMapping("/suggest_keyword")
    public List<String> keywordSuggestions(@RequestParam String keyword) {
        return motelService.getKeywordSuggestions(keyword);
    }
    @PostMapping("/rentedMotel")
    public void rentedMotel(@RequestBody Map<String, String> request) {
        motelService.rentedMotel(Long.parseLong(request.get("id")));
    }
}


