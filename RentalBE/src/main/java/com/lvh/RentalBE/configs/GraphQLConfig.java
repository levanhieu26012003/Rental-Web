package com.lvh.RentalBE.configs;

import com.lvh.RentalBE.scalar.DateTimeScalar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Configuration
public class GraphQLConfig {

    @Bean
    public RuntimeWiringConfigurer   runtimeWiringConfigurer() {
        return wiringBuilder -> wiringBuilder
                .scalar(DateTimeScalar.DateTime); // Đăng ký scalar DateTime
    }
}

