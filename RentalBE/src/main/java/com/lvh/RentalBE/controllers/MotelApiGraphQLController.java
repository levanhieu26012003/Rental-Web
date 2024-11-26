/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lvh.RentalBE.controllers;

import com.lvh.RentalBE.dto.MotelPage;
import com.lvh.RentalBE.model.Motel;
import com.lvh.RentalBE.services.MotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

/**
 * @author levan
 */
//@RestController
////@RequestMapping("/motels")
//public class MotelController {
////    @Autowired
////    private MotelService motelService;
//
//    @GetMapping("/hello1")
//    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
//        return String.format("Hello1 %s!", name);
//    }
//
//    @GetMapping
//    public List<Motel> getAllMotels() {
//        return motelService.getAllMotels();
//    }
//
//    @GetMapping("/{id}")
//    public Motel getMotelById(@PathVariable Long id) {
//        return motelService.getMotelById(id);
//    }
//
//    @PostMapping
//    public Motel createMotel(@RequestBody Motel Motel) {
//        return motelService.saveMotel(Motel);
//    }
//
//    @PutMapping("/{id}")
//    public Motel updateMotel(@PathVariable Long id, @RequestBody Motel Motel) {
//        Motel.setId(id);
//        return motelService.saveMotel(Motel);
//    }
//
//    @DeleteMapping("/{id}")
//    public void deleteMotel(@PathVariable Long id) {
//        motelService.deleteMotel(id);
//    }
//}


@Controller
public class MotelApiGraphQLController {
    private final MotelService motelService;

    public MotelApiGraphQLController(MotelService motelService) {
        this.motelService = motelService;
    }

    @QueryMapping
    public Motel getMotelById(@Argument Long id) {
        return motelService.getMotelById(id);
    }

    @QueryMapping
    public MotelPage getAllMotels(@Argument Integer limit, @Argument Integer offset, @Argument Integer priceMin, @Argument Integer priceMax, @Argument String address,
                                  @Argument Float lat, @Argument Float lng,@Argument Float radius) {
        limit = (limit != null && limit > 0) ? limit : 12;
        offset = (offset != null && offset >= 0) ? offset : 0;
        return motelService.findMotelsWithPagination(limit, offset, priceMin, priceMax, address, lat, lng, radius);
    }


}