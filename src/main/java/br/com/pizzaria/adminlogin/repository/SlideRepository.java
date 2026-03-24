package br.com.pizzaria.adminlogin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.pizzaria.adminlogin.model.Slide;

import java.util.List;

public interface SlideRepository extends JpaRepository<Slide, Long> {
 
    List<Slide> findByCategoryIgnoreCase(String category);


    Slide findTopByOrderByIdDesc();
}
