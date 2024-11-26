/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lvh.RentalBE.repositories;

import com.lvh.RentalBE.model.Motel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author levan
 */
@Repository
public interface MotelRepository extends JpaRepository<Motel, Long> {
    @Query("SELECT m FROM Motel m WHERE (:status IS NULL OR m.status = :status) AND (:active IS NULL OR m.active = :active)")
    List<Motel>  findByStatusAndActive(@Param("status") String status, @Param("active") Boolean active, Sort sort);

    @Query("SELECT m FROM Motel m WHERE (:active IS NULL OR m.active = :active)")
    List<Motel> findAllByActive(@Param("active") Boolean active, Sort sort);

    List<Motel> findAll(Sort sort);

    @Query("SELECT m FROM Motel m WHERE (6371 * acos(cos(radians(:lat)) * cos(radians(m.lat)) * cos(radians(m.lng) - radians(:lng)) + sin(radians(:lat)) * sin(radians(m.lat)))) < :radius")
    List<Motel> findMotelsWithinRadius(@Param("lat") Float lat, @Param("lng") Float lng, @Param("radius") Float radius);

    List<Motel> findAllByCreatedDateBeforeAndStatus(LocalDateTime createdDate, String status);
}
