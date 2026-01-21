package poly.edu.java5_asm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class Java5AsmApplication {

    public static void main(String[] args) {
        SpringApplication.run(Java5AsmApplication.class, args);
    }

}
