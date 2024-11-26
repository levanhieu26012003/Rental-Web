package com.lvh.RentalBE.dto;

import lombok.Data;

@Data
public class UserReviewDTO {
    private Long id;
    private String username;
    private String fullName;
    private String avatar;
    private Double avrRating;
    private Boolean active;

    public UserReviewDTO(Long id, String username, String fullName, String avatar, Double avrRating, Boolean active) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.avatar = avatar;
        this.avrRating = avrRating;
        this.active = active;
    }
}
