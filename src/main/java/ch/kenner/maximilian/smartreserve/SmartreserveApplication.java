package ch.kenner.maximilian.smartreserve;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class SmartreserveApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartreserveApplication.class, args);
    }

}
