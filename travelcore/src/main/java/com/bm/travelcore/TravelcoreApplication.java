package com.bm.travelcore;

import com.bm.travelcore.model.Role;
import com.bm.travelcore.model.enums.Roles;
import com.bm.travelcore.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
@EnableCaching
public class TravelcoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(TravelcoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(RoleRepository roleRepository) {
        return args -> {
            if (roleRepository.findByName(String.valueOf(Roles.ROLE_USER)).isEmpty()) {
                roleRepository.save(
                        Role.builder().name("ROLE_USER").build()
                );
            }
        };
    }

}
