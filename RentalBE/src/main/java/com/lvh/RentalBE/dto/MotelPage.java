package com.lvh.RentalBE.dto;

import com.lvh.RentalBE.model.Motel;

import java.util.List;

public class MotelPage {
    private List<Motel> motels;
    private int totalCount;

    // Getters and setters
    public List<Motel> getMotels() {
        return motels;
    }

    public void setMotels(List<Motel> motels) {
        this.motels = motels;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
