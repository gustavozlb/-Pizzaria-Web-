package br.com.pizzaria.adminlogin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping
    public List<Categorias> listar() {
        return categoriaRepository.findAll();
    }

    @PostMapping
    public Categorias criar(@RequestBody Categorias categoria) {
        return categoriaRepository.save(categoria);
    }
}
