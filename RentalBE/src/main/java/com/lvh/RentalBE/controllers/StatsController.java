package com.lvh.RentalBE.controllers;

import com.lvh.RentalBE.services.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class StatsController {

    @Autowired
    private StatsService statsService;

    @GetMapping("/stats")
    public String getStats(@RequestParam int year, @RequestParam String period, Model model) {
        List<Object[]> revenueStats = statsService.getRevenueStats(year, period);
        model.addAttribute("revenueStats", revenueStats);
        model.addAttribute("year", year);
        model.addAttribute("period", period);
        List<Object[]> usageData = statsService.countVipPackageUsage();
        List<String> packageTypes = new ArrayList<>();
        List<Long> usageCounts = new ArrayList<>();

        for (Object[] row : usageData) {
            packageTypes.add((String) row[0]);
            usageCounts.add((Long) row[1]);
        }

        model.addAttribute("packageTypes", packageTypes);
        model.addAttribute("usageCounts", usageCounts);

        return "stats";
    }

}
