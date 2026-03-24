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

    private String detectImageType(byte[] bytes) {
    // JPEG
    if (bytes.length > 3 &&
        (bytes[0] & 0xFF) == 0xFF &&
        (bytes[1] & 0xFF) == 0xD8 &&
        (bytes[2] & 0xFF) == 0xFF) {
        return "image/jpeg";
    }

    // PNG
    if (bytes.length > 4 &&
        (bytes[0] & 0xFF) == 0x89 &&
        (bytes[1] & 0xFF) == 0x50 &&
        (bytes[2] & 0xFF) == 0x4E &&
        (bytes[3] & 0xFF) == 0x47) {
        return "image/png";
    }

    // WEBP
    if (bytes.length > 12 &&
        bytes[0] == 'R' && bytes[1] == 'I' &&
        bytes[2] == 'F' && bytes[3] == 'F' &&
        bytes[8] == 'W' && bytes[9] == 'E' &&
        bytes[10] == 'B' && bytes[11] == 'P') {
        return "image/webp";
    }
    return null; 

    }   
    

    public SlideController(SlideRepository slideRepository) {
        this.slideRepository = slideRepository;
    }

    // Buscar slides por categoria
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

            byte[] bytes = file.getBytes();
            String contentType = detectImageType(bytes);

            if (contentType == null) {
            return ResponseEntity.badRequest().body("Formato de imagem inválido.");
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
