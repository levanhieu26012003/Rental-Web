package com.lvh.RentalBE.services;

import com.lvh.RentalBE.model.UserVipPackage;
import com.lvh.RentalBE.model.Vippackage;
import com.lvh.RentalBE.repositories.UserVipPackageRepository;
import com.lvh.RentalBE.repositories.VipPackageRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class VipPackageService {

    private final VipPackageRepository vipPackageRepository;
    private final UserVipPackageRepository userVipPackageRepository;

    public VipPackageService(VipPackageRepository vipPackageRepository, UserVipPackageRepository userVipPackageRepository) {
        this.vipPackageRepository = vipPackageRepository;
        this.userVipPackageRepository = userVipPackageRepository;
    }

    public List<Vippackage> getAllVipPackages() {
        return vipPackageRepository.findAll();
    }

    public Optional<Vippackage> findById(Long id) {
        return vipPackageRepository.findById(id);
    }

    public void save(Vippackage vipPackage) {
        vipPackageRepository.save(vipPackage);
    }

    public void delete(Long id) {
        Optional<Vippackage> vipPackage = vipPackageRepository.findById(id);
        vipPackageRepository.delete(vipPackage.get());
    }

    @Transactional
    public void registerVipPackage(Long userId, Long vipPackageId) throws Exception {
        // Lấy gói VIP dựa trên vipPackageId
        Vippackage vipPackage = vipPackageRepository.findById(vipPackageId).orElse(null);
        if (vipPackage == null) {
            throw new IllegalArgumentException("Vip package not found");
        }

        // Ngày bắt đầu là ngày hiện tại
        Date dayStart = new Date();

        // Tính ngày kết thúc bằng cách cộng upgradeTime (số ngày) vào ngày hiện tại
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dayStart);
        if (vipPackage.getUpgradeTime() != null) {
            calendar.add(Calendar.DAY_OF_MONTH, vipPackage.getUpgradeTime()); // Cộng upgradeTime ngày
        }
        Date dayEnd = calendar.getTime();

        // Tạo đối tượng UserVipPackage và lưu vào cơ sở dữ liệu
        UserVipPackage userVipPackage = new UserVipPackage(userId, vipPackageId);
        userVipPackage.setDayStart(dayStart);
        userVipPackage.setDayEnd(dayEnd);

        userVipPackageRepository.save(userVipPackage);
    }

    public Optional<UserVipPackage> checkUserVipPackage(Long id){
        // Lấy thời điểm bắt đầu tháng (ngày đầu tiên của tháng, lúc 00:00:00)
        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();

        // Lấy thời điểm kết thúc tháng (ngày cuối cùng của tháng, lúc 23:59:59)
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1).withDayOfMonth(1).minusDays(1).withHour(23).withMinute(59).withSecond(59);
        Optional<UserVipPackage> rs =  userVipPackageRepository.findTopByUserIdAndDayStartBetweenOrderByDayStartDesc(id, startOfMonth, endOfMonth);

        return rs;
    }

    public String checkRegistedPackage(Long userId, Long vipPackageId) {
        Optional<UserVipPackage> rs =  this.checkUserVipPackage(userId);
        if (rs.isEmpty()) {
            return "Có thể đăng ký";
        }
        if (rs.get().getUserVipPackagePK().getVipPackageId() == vipPackageId) {
            return "Gói này hiện tại bạn đang đăng kí";
        }else{
            Optional<Vippackage> ved = vipPackageRepository.findById(rs.get().getUserVipPackagePK().getVipPackageId());
            Optional<Vippackage> v = vipPackageRepository.findById(vipPackageId);
            if (ved.get().getPrice() > v.get().getPrice()) {
                return "Không thể nâng cấp gói nhỏ hơn";
            }else {
                return "Có thể đăng ký";
            }
        }
    }
}
