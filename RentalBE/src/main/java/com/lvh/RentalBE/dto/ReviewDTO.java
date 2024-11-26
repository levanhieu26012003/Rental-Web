package com.lvh.RentalBE.dto;

import lombok.Data;

@Data
public class ReviewDTO {
    private Long id;
    private String comment;
    private int rating;
    private String reateDate;
    private UserDTO tenant;
    public ReviewDTO(Long id, String comment, int rating, String reateDate, UserDTO tenant) {
        this.id = id;
        this.comment = comment;
        this.rating = rating;
        this.reateDate = reateDate;
        this.tenant = tenant;
    }

}
