/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lvh.RentalBE.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.lvh.RentalBE.dto.MotelPage;
import com.lvh.RentalBE.model.Image;
import com.lvh.RentalBE.model.Motel;
import com.lvh.RentalBE.repositories.ImageRepository;
import com.lvh.RentalBE.repositories.MotelRepository;
import com.lvh.RentalBE.resolvers.MotelSubscription;
import com.lvh.RentalBE.utils.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author levan
 */
@Service
public class MotelService {
    private final Cloudinary cloudinary;

    private final MotelRepository motelRepository;
    private final List<Motel> createdMotels = new ArrayList<>();
    private final ImageRepository imageRepository;
    private final MotelSubscription motelSubscription;
    private final Sinks.Many<Motel> sink = Sinks.many().multicast().directBestEffort();

    public MotelService(Cloudinary cloudinary, MotelRepository motelRepository, ImageService imageService, ImageRepository imageRepository, MotelSubscription motelSubscription) {
        this.cloudinary = cloudinary;
        this.motelRepository = motelRepository;
        this.imageRepository = imageRepository;
        this.motelSubscription = motelSubscription;
    }

    public List<Motel> getAllMotels() {
        List<Motel> motels = motelRepository.findAll();
        Collections.sort(motels, new Comparator<Motel>() {
            @Override
            public int compare(Motel m1, Motel m2) {
                return m2.getId().compareTo(m1.getId());
            }
        });
        return motels;
    }

    public Motel getMotelById(Long id) {
        return motelRepository.findById(id).orElse(null);
    }

    public Motel saveMotel(Motel motel) {
        if (motel.getStatus() == null) {
            motel.setStatus("PENDING");
        }
        this.motelRepository.save(motel);
        if (motel.getFiles().get(0).getSize() > 0) {
            for (MultipartFile image : motel.getFiles()) {
                Map uploadResult;
                try {
                    uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
                    String url = (String) uploadResult.get("url");
                    String publicId = (String) uploadResult.get("public_id");
                    Image img = new Image();
                    img.setMotelId(motel);
                    img.setUrl(url);
                    img.setPublicUrl(publicId);
                    imageRepository.save(img);
                } catch (IOException ex) {
                }
            }
        } else {
            Optional<Motel> existingMotel = motelRepository.findById(motel.getId());
            motel.setImageCollection(existingMotel.get().getImageCollection());
        }
//        motelSubscription.publish(motel);
        return motel;
    }


//        this.createdMotels.add(motel);
//        if (this.sink.currentSubscriberCount() > 0) {
//            this.sink.tryEmitNext(motel);
//        }
//        return Mono.just(motel);
//    }

    public Flux<Motel> getEventFlux() {
        return Flux.concat(Flux.fromIterable(this.createdMotels), sink.asFlux());
    }

    public MotelPage findMotelsWithPagination(int limit, int offset, Integer priceMin, Integer priceMax, String
            address,
                                              Float lat, Float lng, Float radius) {
        // Bước 1: Tìm kiếm và lọc trước
        List<Motel> filteredMotels = (lng != null) ? motelRepository.findMotelsWithinRadius(lat, lng, radius) : motelRepository.findAll();
        // Lọc theo giá nếu có điều kiện
        if (priceMin != null && priceMax != null) {
            filteredMotels = filteredMotels.stream()
                    .filter(motel -> motel.getPrice() >= priceMin && motel.getPrice() <= priceMax)
                    .collect(Collectors.toList());
        }

        //Loc nếu đã duoc chap nhan boi admin
        filteredMotels = filteredMotels.stream()
                .filter(motel -> motel.getStatus().toLowerCase().contains("approved"))
                .collect(Collectors.toList());

        //Lọc ẩn hiện
        filteredMotels = filteredMotels.stream()
                .filter(motel -> motel.getActive().equals(true))
                .collect(Collectors.toList());

        // Lọc theo địa chỉ nếu có điều kiện

        if (address != null && !address.isEmpty()) {
            String normalizedKeyword = StringUtils.removeAccents(address.toLowerCase());
            filteredMotels = filteredMotels.stream()
                    .filter(motel -> StringUtils.removeAccents(motel.getTitle().toLowerCase()).contains(normalizedKeyword))
                    .collect(Collectors.toList());
        }

        // Bước 2: Áp dụng phân trang
        int start = offset; // Vị trí bắt đầu của trang
        int end = Math.min(start + limit, filteredMotels.size()); // Vị trí kết thúc của trang (phụ thuộc vào limit)

        // Kiểm tra giới hạn để tránh lỗi chỉ mục ngoài phạm vi (IndexOutOfBoundsException)
        List<Motel> pagedMotels = start <= end ? filteredMotels.subList(start, end) : new ArrayList<>(); // Lấy danh sách sau khi phân trang

//        // Tạo đối tượng MotelPage để trả về
        MotelPage resultPage = new MotelPage();
        resultPage.setMotels(pagedMotels); // Đặt dữ liệu motels sau khi phân trang
        resultPage.setTotalCount(filteredMotels.size()); // Tổng số phần tử sau khi lọc

        return resultPage;
    }


    public void deleteMotelById(Long id) {
        motelRepository.deleteById(id);
    }

    public List<Motel> getAllMotels(String sort, String status, String active) {
        Sort sortOrder = Sort.by("updatedDate");
        if ("desc".equals(sort)) {
            sortOrder = sortOrder.descending();
        } else {
            sortOrder = sortOrder.ascending();
        }
        Boolean isActive = (active != null && !active.isEmpty()) ? Boolean.valueOf(active) : null;
        if (status != null && !status.isEmpty()) {
            return motelRepository.findByStatusAndActive(status, isActive, sortOrder);
        } else {
            return motelRepository.findAllByActive(isActive, sortOrder);
        }
    }

    public void updateActive(Long id) {
        Motel motel = motelRepository.findById(id).orElse(null);
        motel.setActive(motel.getActive() == false);
        motelRepository.save(motel);
    }

    public List<String> getKeywordSuggestions(String keyword) {
        List<Motel> motels = this.getAllMotels("desc", "APPROVED", "true");
        String normalizedKeyword = StringUtils.removeAccents(keyword.toLowerCase());
        return motels.stream()
                .map(Motel::getTitle)
                .filter(title -> StringUtils.removeAccents(title.toLowerCase()).contains(normalizedKeyword))
                .distinct()
                .collect(Collectors.toList());  // Lọc và lấy từ khóa duy nhất
    }

    public void rentedMotel(Long id) {
        Motel motel = motelRepository.findById(id).orElse(null);
        motel.setActive(false);
        motel.setStatus("RENTED");
        motelRepository.save(motel);
    }


}