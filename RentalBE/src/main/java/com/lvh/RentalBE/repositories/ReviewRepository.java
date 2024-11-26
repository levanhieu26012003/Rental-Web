/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lvh.RentalBE.repositories;

import com.lvh.RentalBE.model.Reviews;
import com.lvh.RentalBE.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @author levan
 */
@Repository
public interface ReviewRepository extends JpaRepository<Reviews, Long>{
    List<Reviews> findReviewsByHostId(User HostId);
}
