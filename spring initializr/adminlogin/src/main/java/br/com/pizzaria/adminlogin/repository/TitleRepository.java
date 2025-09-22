package br.com.pizzaria.adminlogin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.pizzaria.adminlogin.model.Title;

public interface TitleRepository extends JpaRepository<Title, Long> {
    Title findTopByOrderByCriadoEmDesc();
}
