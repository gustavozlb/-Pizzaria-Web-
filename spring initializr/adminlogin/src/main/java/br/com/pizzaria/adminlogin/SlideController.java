package br.com.pizzaria.adminlogin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/slides")
public class SlideController {

    @Autowired
    private SlideRepository slideRepository;

    // Buscar slides por categoria
    @GetMapping
    public ResponseEntity<List<Slide>> findByCategory(@RequestParam String category) {
        List<Slide> slides = slideRepository.findByCategoryIgnoreCase(category);

        if (slides == null || slides.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(slides);
    }

    // Salvar slide com upload
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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

    // Exibe a imagem pelo id
    @GetMapping("/image/{id}")
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
