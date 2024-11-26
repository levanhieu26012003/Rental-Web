package com.lvh.RentalBE.controllers;

import com.lvh.RentalBE.model.Vippackage;
import com.lvh.RentalBE.services.VipPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class VipPackageGraphController {
    private final VipPackageService vipPackageService;

    public VipPackageGraphController(VipPackageService vipPackageService) {
        this.vipPackageService = vipPackageService;
    }

    @QueryMapping
    public List<Vippackage> getAllVipPackages() {
        return vipPackageService.getAllVipPackages();
    }
}
