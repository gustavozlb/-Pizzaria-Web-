package br.com.pizzaria.adminlogin.repository;

import br.com.pizzaria.adminlogin.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByNome(String nome);
}
