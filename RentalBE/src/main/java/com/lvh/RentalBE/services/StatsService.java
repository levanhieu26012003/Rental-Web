package com.lvh.RentalBE.services;

import com.lvh.RentalBE.repositories.StatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Service
public class StatsService {

    private final StatsRepository statsRepository;



    @Autowired
    public StatsService(StatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }

    public List<Object[]> getRevenueStats(int year, String period) {
        switch (period.toUpperCase()) {
            case "MONTH":{
                List<Object[]> queryResult = statsRepository.findMonthlyRevenue(year);

                // Tạo một mảng cho 12 tháng, mặc định doanh thu là 0
                List<Object[]> fullMonthlyRevenue = new ArrayList<>();
                for (int i = 1; i <= 12; i++) {
                    fullMonthlyRevenue.add(new Object[]{i, 0.0});
                }

                // Duyệt qua kết quả query và gán vào mảng đầy đủ 12 tháng
                for (Object[] result : queryResult) {
                    int month = (int) result[0]; // Tháng từ kết quả query
                    double revenue = (double) result[1]; // Doanh thu từ kết quả query
                    fullMonthlyRevenue.set(month - 1, new Object[]{month, revenue}); // Gán vào mảng tại vị trí tương ứng
                }

                return fullMonthlyRevenue;
            }
            case "QUARTER": {
                List<Object[]> queryResult = statsRepository.findQuarterlyRevenue(year);

                // Tạo một mảng cho 4 quý, mặc định doanh thu là 0
                List<Object[]> fullQuarterlyRevenue = new ArrayList<>();
                for (int i = 1; i <= 4; i++) {
                    fullQuarterlyRevenue.add(new Object[]{i, 0.0});
                }

                // Duyệt qua kết quả query và gán vào mảng đầy đủ 4 quý
                for (Object[] result : queryResult) {
                    int quarter = (int) result[0]; // Quý từ kết quả query
                    double revenue = (double) result[1]; // Doanh thu từ kết quả query
                    fullQuarterlyRevenue.set(quarter - 1, new Object[]{quarter, revenue}); // Gán vào mảng tại vị trí tương ứng
                }

                return fullQuarterlyRevenue;
            }

            case "YEAR": {
                List<Object[]> queryResult = statsRepository.findYearlyRevenue();

                // Tạo một danh sách cho từng năm có dữ liệu, không cố định
                List<Object[]> fullYearlyRevenue = new ArrayList<>();

                // Duyệt qua kết quả query và gán vào danh sách
                for (Object[] result : queryResult) {
                    int queryYear = (int) result[0]; // Năm từ kết quả query
                    double revenue = (double) result[1]; // Doanh thu từ kết quả query
                    fullYearlyRevenue.add(new Object[]{queryYear, revenue}); // Gán vào danh sách
                }

                return fullYearlyRevenue;
            }
            default:
                throw new IllegalArgumentException("Unsupported period: " + period);

        }
    }

    public  List<Object[]> countVipPackageUsage(){
        return statsRepository.countVipPackageUsage();
    }
}


