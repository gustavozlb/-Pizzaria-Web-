package br.com.pizzaria.adminlogin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "br.com.pizzaria.adminlogin")
public class PizzariaAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(PizzariaAdminApplication.class, args);
    }
}
