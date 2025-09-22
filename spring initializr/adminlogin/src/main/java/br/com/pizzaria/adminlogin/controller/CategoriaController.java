package br.com.pizzaria.adminlogin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import br.com.pizzaria.adminlogin.model.Category;
import br.com.pizzaria.adminlogin.repository.CategoryRepository;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private CategoryRepository categoriaRepository;

    @GetMapping
    public List<Category> listar() {
        return categoriaRepository.findAll();
    }

    @PostMapping
    public Category criar(@RequestBody Category categoria) {
        return categoriaRepository.save(categoria);
    }
}
