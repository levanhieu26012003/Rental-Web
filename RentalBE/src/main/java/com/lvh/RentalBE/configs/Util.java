package com.lvh.RentalBE.configs;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Configuration
public class Util {

    @Bean
    @Primary
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }


    @Bean
    public Cloudinary cloudinary() {
        Cloudinary cloudinary
                = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dlkfozznl",
                "api_key", "216689541712763",
                "api_secret", "m_IQI6lLGClNtrf8ZeuG97Do4Fs",
                "secure", true));
        return cloudinary;
    }
}
