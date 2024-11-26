package com.lvh.RentalBE.repositories;

import com.lvh.RentalBE.model.User;
import com.lvh.RentalBE.model.UserVipPackage;
import com.lvh.RentalBE.model.Vippackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<UserVipPackage, Long> {


    @Query(
            "Select  MONTH(u.dayStart), SUM(v.price) " +
            "FROM UserVipPackage u " +
            "JOIN u.vippackage v " +
            "WHERE YEAR(u.dayStart) = :year " +
            "GROUP BY  MONTH(u.dayStart) " +
            "ORDER BY  MONTH(u.dayStart)"
    )
    List<Object[]> findMonthlyRevenue(@Param("year") int year);

    @Query(
            "SELECT QUARTER(u.dayStart), SUM(v.price) " +
            "FROM UserVipPackage u " +
            "JOIN u.vippackage v " +
            "WHERE YEAR(u.dayStart) = :year " +
            "GROUP BY QUARTER(u.dayStart) " +
            "ORDER BY QUARTER(u.dayStart)"
    )
    List<Object[]> findQuarterlyRevenue(@Param("year") int year);

    @Query(
            "SELECT YEAR(u.dayStart), SUM(v.price) " +
            "FROM UserVipPackage u " +
            "JOIN u.vippackage v " +
            "GROUP BY YEAR(u.dayStart) " +
            "ORDER BY YEAR(u.dayStart)"
    )
    List<Object[]> findYearlyRevenue();

    @Query("SELECT v.type, COUNT(uvp.userVipPackagePK.vipPackageId) FROM UserVipPackage uvp " +
           "JOIN Vippackage v ON uvp.userVipPackagePK.vipPackageId = v.id " +
           "GROUP BY v.type")
    List<Object[]> countVipPackageUsage();

}


