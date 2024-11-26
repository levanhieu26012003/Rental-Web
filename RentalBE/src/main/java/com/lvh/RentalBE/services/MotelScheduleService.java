package com.lvh.RentalBE.services;

import com.lvh.RentalBE.model.Motel;
import com.lvh.RentalBE.repositories.MotelRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MotelScheduleService {
    final
    MotelRepository motelRepository;

    public MotelScheduleService(MotelRepository motelRepository) {
        this.motelRepository = motelRepository;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void checkExpiredMotel(){
        LocalDateTime expirationDate = LocalDateTime.now().minusDays(30);

        List<Motel> expiredMotels = motelRepository.findAllByCreatedDateBeforeAndStatus(expirationDate, "APPROVED");
        expiredMotels.forEach(motel -> {motel.setStatus("EXPIRED");});
        motelRepository.saveAll(expiredMotels);
        System.out.println("Đã ẩn " + expiredMotels.size() + " bài đăng hết hạn.");
    }
}
