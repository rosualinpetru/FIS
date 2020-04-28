package ro.go.redhomeserver.tom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class ToMApplication {

    public static void main(String[] args) {
        SpringApplication.run(ToMApplication.class, args);
    }

}
