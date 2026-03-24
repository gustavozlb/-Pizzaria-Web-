package br.com.pizzaria.adminlogin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.pizzaria.adminlogin.model.HomeTitle;

public interface HomeTitleRepository extends JpaRepository<HomeTitle, Long> {
    HomeTitle findTopByOrderByIdDesc();
}
