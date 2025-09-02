package br.com.pizzaria.adminlogin;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SlideRepository extends JpaRepository<Slide, Long> {
 
    List<Slide> findByCategoryIgnoreCase(String category);


    Slide findTopByOrderByIdDesc();
}
