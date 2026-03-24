package br.com.pizzaria.adminlogin.controller;

import br.com.pizzaria.adminlogin.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

import javax.imageio.ImageIO;

import br.com.pizzaria.adminlogin.model.Category;
import br.com.pizzaria.adminlogin.model.Slide;
import br.com.pizzaria.adminlogin.model.Title;
import br.com.pizzaria.adminlogin.model.HomeTitle;
import br.com.pizzaria.adminlogin.model.HomeSlide;

import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

import java.awt.image.BufferedImage;

@RestController
@RequestMapping("/api/content")
public class ContentController {

    @Autowired
    private CategoryRepository categoriaRepository;

    @Autowired
    private SlideRepository slideRepository;

    @Autowired
    private TitleRepository titleRepository;

    @Autowired
    private HomeTitleRepository homeTitleRepository;

    @Autowired
    private HomeSlideRepository homeSlideRepository;

    private static final PolicyFactory POLICY =
            Sanitizers.FORMATTING
            .and(Sanitizers.LINKS)
            .and(Sanitizers.BLOCKS);

    private String sanitizeHtml(String html) {
        if (html == null) return null;
        return POLICY.sanitize(html);
    }

    private boolean isValidImage(MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) return false;

            String type = file.getContentType();

            BufferedImage img = ImageIO.read(file.getInputStream());

            if(img == null){
                return false;
            }

            if (img.getWidth() > 5000 || img.getHeight() > 5000) {
                return false;
            }

            return type != null && (
                type.equals("image/jpeg") ||
                type.equals("image/png") ||
                type.equals("image/webp")
            );

            } catch (Exception e) {
                return false;
            }
    }

    private boolean isValidSize(MultipartFile file) {
         return file != null && file.getSize() <= 5 * 1024 * 1024;
    }
    
    // Adicionar
    @PostMapping("/add")
    public ResponseEntity<String> addContent(
            @RequestParam String type,
            @RequestParam(required = false) String text,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) MultipartFile file) {
        try {
            text = sanitizeHtml(text);
            category = sanitizeHtml(category);
            if (text != null && text.length() > 500) {
                return ResponseEntity.badRequest().body("Texto muito grande");
            }
            if (type == null || type.isBlank()) {
                return ResponseEntity.badRequest().body("Tipo é obrigatório");
            }   
            switch (type.toLowerCase()) {
                // Cardápio
                case "categoria":
                    if (category != null && category.length() > 100) {
                        return ResponseEntity.badRequest().body("Categoria inválida");
                    }
                    if (text == null || text.isBlank()) {
                        return ResponseEntity.badRequest().body("Texto é obrigatório");
                    }
                    Category cat = new Category();
                    cat.setNome(text);
                    categoriaRepository.save(cat);
                    break;

                case "titulo":
                
                    if (text == null || text.trim().isEmpty()) {
                        return ResponseEntity.badRequest().body("Texto é obrigatório");
                    }

                    Title title = new Title();
                    title.setText(text);
                    titleRepository.save(title);
                    break;

                case "slides":
                    if (file == null || file.isEmpty() || !isValidImage(file)) {
                        return ResponseEntity.badRequest().body("Imagem inválida");
                    }
                    if (!isValidSize(file)) {
                        return ResponseEntity.badRequest().body("Imagem muito grande (máx 5MB)");
                    }
                    if (category == null || category.isBlank()) {
                        return ResponseEntity.badRequest().body("Categoria é obrigatória");
                    }
                    Slide slide = new Slide();
                    slide.setText(text);
                    slide.setCategory(category);
                    slide.setImage(file.getBytes());
                    slideRepository.save(slide);
                    break;

                // Home
                case "home_title":
                    if (text == null || text.isBlank()) {
                        return ResponseEntity.badRequest().body("Texto é obrigatório");
                    }
                    HomeTitle homeTitle = new HomeTitle();
                    homeTitle.setTitleHome(text);
                    homeTitleRepository.save(homeTitle);
                    break;

                case "home_slides":
                    if (text != null && text.length() > 500) {
                        return ResponseEntity.badRequest().body("Texto muito grande");
                    }
                    if (file == null || file.isEmpty() || !isValidImage(file)) {
                        return ResponseEntity.badRequest().body("Imagem inválida");
                    }
                    if (!isValidSize(file)) {
                        return ResponseEntity.badRequest().body("Imagem muito grande (máx 5MB)");
                    }
                    HomeSlide slideHome = new HomeSlide();
                    slideHome.setSlideHome(file.getBytes());
                    slideHome.setText(text); 
                    homeSlideRepository.save(slideHome);
                    break;

                default:
                    return ResponseEntity.badRequest().body("Tipo inválido");
            }

            return ResponseEntity.ok("Item salvo com sucesso!");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro interno do servidor");        }
    }

    // Buscar último
    @GetMapping("/last")
    public ResponseEntity<?> getLast(@RequestParam String type) {
        try {
            if (type == null || type.isBlank()) {
                return ResponseEntity.badRequest().body("Tipo é obrigatório");
            }
            switch (type.toLowerCase()) {
                // Cardápio
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

                // Home
                case "home_title":
                    HomeTitle lastTitleHome = homeTitleRepository.findTopByOrderByIdDesc();
                        if (lastTitleHome == null) return ResponseEntity.notFound().build();
                    return ResponseEntity.ok(lastTitleHome);

                case "home_slides":
                    List<HomeSlide> allSlidesHome = homeSlideRepository.findBySlideHomeIsNotNull();
                    if (allSlidesHome.isEmpty()) return ResponseEntity.notFound().build();
                    return ResponseEntity.ok(allSlidesHome);

                default:
                    return ResponseEntity.badRequest().body("Tipo inválido");
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro no servidor");
        }
    }

    // Listar todos
    @GetMapping("/list")
    public ResponseEntity<?> listAll(@RequestParam String type) {
        try {
            if (type == null || type.isBlank()) {
                return ResponseEntity.badRequest().body("Tipo é obrigatório");
            }
            switch (type.toLowerCase()) {
                // Cardápio
                case "slides":
                    return ResponseEntity.ok(slideRepository.findAll());

                case "titulo":
                    return ResponseEntity.ok(titleRepository.findAll());

                case "categoria":
                    return ResponseEntity.ok(categoriaRepository.findAll());

                // Home
                case "home_title":
                    return ResponseEntity.ok(homeTitleRepository.findAll());

                case "home_slides":
                    return ResponseEntity.ok(homeSlideRepository.findAll());

                default:
                    return ResponseEntity.badRequest().body("Tipo inválido");
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro no servidor");
        }
    }

    //  Deletar por ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteContent(@PathVariable Long id, @RequestParam String type) {
        try {
            if (type == null || type.isBlank()) {
                return ResponseEntity.badRequest().body("Tipo é obrigatório");
            }
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest().body("ID inválido");
            }
            switch (type.toLowerCase()) {
                // Cardápio
                case "slides":
                    if (slideRepository.existsById(id)) {
                        slideRepository.deleteById(id);
                        return ResponseEntity.ok("Slide do cardápio excluído com sucesso!");
                    }
                    return ResponseEntity.status(404).body("Slide do cardápio não encontrado");

                case "titulo":
                    if (titleRepository.existsById(id)) {
                        titleRepository.deleteById(id);
                        return ResponseEntity.ok("Título do cardápio excluído com sucesso!");
                    }
                    return ResponseEntity.status(404).body("Título do cardápio não encontrado");

                case "categoria":
                    if (categoriaRepository.existsById(id)) {
                        categoriaRepository.deleteById(id);
                        return ResponseEntity.ok("Categoria excluída com sucesso!");
                    }
                    return ResponseEntity.status(404).body("Categoria não encontrada");

                // Home

                case "home_slides":
                    if (homeSlideRepository.existsById(id)) {
                        homeSlideRepository.deleteById(id);
                        return ResponseEntity.ok("Slide da home excluído com sucesso!");
                    }
                    return ResponseEntity.status(404).body("Slide da home não encontrado");

                default:
                    return ResponseEntity.badRequest().body("Tipo inválido");
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro no servidor");
        }
    }
    @GetMapping("/home_slides/image/{id}")
    public ResponseEntity<byte[]> getHomeSlideImage(@PathVariable Long id) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().build();
        }

        HomeSlide slide = homeSlideRepository.findById(id).orElse(null);
        if (slide == null || slide.getSlideHome() == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity
                .ok()
                .header("Content-Type", "image/*")
                .body(slide.getSlideHome());
        }
}
