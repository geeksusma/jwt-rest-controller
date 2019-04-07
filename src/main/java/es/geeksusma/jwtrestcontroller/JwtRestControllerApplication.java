package es.geeksusma.jwtrestcontroller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication
public class JwtRestControllerApplication {
    public static void main(String[] args) {
        SpringApplication.run(JwtRestControllerApplication.class, args);
    }

}
