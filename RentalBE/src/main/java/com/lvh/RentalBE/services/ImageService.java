package com.lvh.RentalBE.services;

import com.lvh.RentalBE.model.Image;
import com.lvh.RentalBE.repositories.ImageRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ImageService {
    private final ImageRepository imageRepository;
    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public Optional<Image> findImageById(Long id){
        return imageRepository.findById(id);
    }


}


