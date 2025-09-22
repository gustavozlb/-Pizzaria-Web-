package br.com.pizzaria.adminlogin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import br.com.pizzaria.adminlogin.model.Category;
import br.com.pizzaria.adminlogin.model.Slide;
import br.com.pizzaria.adminlogin.model.Title;
import br.com.pizzaria.adminlogin.repository.CategoryRepository;
import br.com.pizzaria.adminlogin.repository.SlideRepository;
import br.com.pizzaria.adminlogin.repository.TitleRepository;

@RestController
@RequestMapping("/api/content")
public class ContentController {

    @Autowired
    private CategoryRepository categoriaRepository;

    @Autowired
    private SlideRepository slideRepository;

    @Autowired
    private TitleRepository titleRepository;

    // Adicionar conteúdo
    @PostMapping("/add")
    public ResponseEntity<String> addContent(
            @RequestParam String type,
            @RequestParam String text,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) MultipartFile file) {
        try {
            switch (type) {
                case "categoria":
                    Category cat = new Category();
                    cat.setNome(text);
                    categoriaRepository.save(cat);
                    break;

                case "titulo":
                    Title existingTitle = titleRepository.findTopByOrderByCriadoEmDesc();
                    if (existingTitle != null) {
                        existingTitle.setText(text);
                        titleRepository.save(existingTitle);
                    } else {
                        Title title = new Title();
                        title.setText(text);
                        titleRepository.save(title);
                    }
                    break;

                case "slides":
                    if (file == null || file.isEmpty()) {
                        return ResponseEntity.badRequest().body("Imagem é obrigatória para slides");
                    }
                    Slide slide = new Slide();
                    slide.setText(text);
                    slide.setCategory(category);
                    slide.setImage(file.getBytes());
                    slideRepository.save(slide);
                    break;

                default:
                    return ResponseEntity.badRequest().body("Tipo inválido");
            }
            return ResponseEntity.ok("Item salvo com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro: " + e.getMessage());
        }
    }

    // Buscar último item
    @GetMapping("/last")
    public ResponseEntity<?> getLast(@RequestParam String type) {
        try {
            switch (type.toLowerCase()) {
                case "titulo":
                    Title lastTitle = titleRepository.findTopByOrderByCriadoEmDesc();
                    if (lastTitle == null) return ResponseEntity.notFound().build();
                    return ResponseEntity.ok(lastTitle);

                case "slides":
                    Slide lastSlide = slideRepository.findTopByOrderByIdDesc();
                    if (lastSlide == null) return ResponseEntity.notFound().build();
                    return ResponseEntity.ok(lastSlide);

                case "categoria":
                    Category lastCat = categoriaRepository.findTopByOrderByIdDesc();
                    if (lastCat == null) return ResponseEntity.notFound().build();
                    return ResponseEntity.ok(lastCat);

                default:
                    return ResponseEntity.badRequest().body("Tipo inválido");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erro no servidor");
        }
    }

    // Listar todos
    @GetMapping("/list")
    public ResponseEntity<?> listAll(@RequestParam String type) {
        try {
            switch (type.toLowerCase()) {
                case "slides":
                    return ResponseEntity.ok(slideRepository.findAll());

                case "categoria":
                    return ResponseEntity.ok(categoriaRepository.findAll());

                default:
                    return ResponseEntity.badRequest().body("Tipo inválido");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erro no servidor");
        }
    }

    // Deletar por id
    @DeleteMapping("/delete/{id}")
public ResponseEntity<?> deleteContent(@PathVariable Long id, @RequestParam String type) {
    try {
        switch (type.toLowerCase()) {
            case "slides":
                if (slideRepository.existsById(id)) {
                    slideRepository.deleteById(id);
                    return ResponseEntity.ok("Slide excluído com sucesso!");
                }
                return ResponseEntity.status(404).body("Slide não encontrado");

            case "categoria":
                if (categoriaRepository.existsById(id)) {
                    categoriaRepository.deleteById(id);
                    return ResponseEntity.ok("Categoria excluída com sucesso!");
                }
                return ResponseEntity.status(404).body("Categoria não encontrada");

            default:
                return ResponseEntity.badRequest().body("Tipo inválido");
        }
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body("Erro: " + e.getMessage());
    }
}

}

