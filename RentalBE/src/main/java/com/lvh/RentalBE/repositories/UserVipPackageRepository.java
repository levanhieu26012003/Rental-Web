package com.lvh.RentalBE.repositories;

import com.lvh.RentalBE.model.UserVipPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserVipPackageRepository extends JpaRepository<UserVipPackage, Long> {
    Optional<UserVipPackage> findTopByUserIdAndDayStartBetweenOrderByDayStartDesc(Long userId, LocalDateTime startOfMonth, LocalDateTime endOfMonth);

}