package br.com.pizzaria.adminlogin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import br.com.pizzaria.adminlogin.repository.AdminRepository;

public class teste {
    
    @Component
public class Teste implements CommandLineRunner {

    @Autowired
    private AdminRepository repo;

    @Override
    public void run(String... args) {
        System.out.println("ADMIN COUNT: " + repo.count());
    }
}
}
