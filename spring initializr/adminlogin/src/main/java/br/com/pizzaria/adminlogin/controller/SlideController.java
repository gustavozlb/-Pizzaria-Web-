package br.com.pizzaria.adminlogin.controller;

import br.com.pizzaria.adminlogin.model.Slide;
import br.com.pizzaria.adminlogin.repository.SlideRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class SlideController {

    private final SlideRepository slideRepository;

    public SlideController(SlideRepository slideRepository) {
        this.slideRepository = slideRepository;
    }

    // Buscar slides por categoria (com fallback)
    @GetMapping("/slides")
    public List<Slide> getSlidesByCategory(@RequestParam(required = false) String category) {
        if (category == null || category.isBlank()) {
            return slideRepository.findAll();
        }
        List<Slide> slides = slideRepository.findByCategoryIgnoreCase(category);
        return slides.isEmpty() ? slideRepository.findAll() : slides;
    }

    // Salvar slide com upload de imagem
    @PostMapping(value = "/slides/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> saveSlide(@RequestParam String category,
                                       @RequestParam String text,
                                       @RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Imagem não pode ser vazia.");
            }

            Slide slide = new Slide();
            slide.setCategory(category);
            slide.setText(text);
            slide.setImage(file.getBytes());

            slideRepository.save(slide);
            return ResponseEntity.ok("Imagem salva com sucesso!");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Erro ao salvar imagem: " + e.getMessage());
        }
    }

    // Exibir imagem pelo ID
    @GetMapping("/slides/image/{id}")
    public ResponseEntity<byte[]> showImage(@PathVariable Long id) {
        Slide slide = slideRepository.findById(id).orElse(null);

        if (slide == null || slide.getImage() == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(slide.getImage());
    }
}