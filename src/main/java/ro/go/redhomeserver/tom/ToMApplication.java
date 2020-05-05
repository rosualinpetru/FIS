package ro.go.redhomeserver.tom;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@EnableEncryptableProperties
public class ToMApplication {

    public static void main(String[] args) {
        SpringApplication.run(ToMApplication.class, args);
    }

}
