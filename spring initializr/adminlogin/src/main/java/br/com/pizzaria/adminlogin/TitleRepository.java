package br.com.pizzaria.adminlogin;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TitleRepository extends JpaRepository<Title, Long> {
    Title findTopByOrderByCriadoEmDesc();
}
