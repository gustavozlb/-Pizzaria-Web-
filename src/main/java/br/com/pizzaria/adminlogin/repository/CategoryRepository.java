package br.com.pizzaria.adminlogin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.pizzaria.adminlogin.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findTopByOrderByIdDesc();
}
