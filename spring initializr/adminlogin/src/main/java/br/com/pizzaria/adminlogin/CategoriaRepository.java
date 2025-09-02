package br.com.pizzaria.adminlogin;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categorias, Long> {
    Categorias findTopByOrderByIdDesc();
}
