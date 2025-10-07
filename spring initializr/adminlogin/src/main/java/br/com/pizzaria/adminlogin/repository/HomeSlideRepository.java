package br.com.pizzaria.adminlogin.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.pizzaria.adminlogin.model.HomeSlide;

public interface HomeSlideRepository extends JpaRepository<HomeSlide, Long> {
    

    List<HomeSlide> findBySlideHomeIsNotNull();
    
    HomeSlide findTopByOrderByIdDesc();
}
