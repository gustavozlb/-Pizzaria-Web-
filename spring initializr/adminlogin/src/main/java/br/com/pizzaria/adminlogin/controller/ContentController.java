package br.com.pizzaria.adminlogin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

import br.com.pizzaria.adminlogin.model.Category;
import br.com.pizzaria.adminlogin.model.Slide;
import br.com.pizzaria.adminlogin.model.Title;
import br.com.pizzaria.adminlogin.model.HomeTitle;
import br.com.pizzaria.adminlogin.model.HomeSlide;

import br.com.pizzaria.adminlogin.repository.CategoryRepository;
import br.com.pizzaria.adminlogin.repository.SlideRepository;
import br.com.pizzaria.adminlogin.repository.TitleRepository;
import br.com.pizzaria.adminlogin.repository.HomeTitleRepository;
import br.com.pizzaria.adminlogin.repository.HomeSlideRepository;


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

    @PostMapping("/add")
    public ResponseEntity<String> addContent(
            @RequestParam String type,
            @RequestParam(required = false) String text,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) MultipartFile file) {
        try {
            switch (type.toLowerCase()) {
                // Cardápio
                case "categoria":
                    Category cat = new Category();
                    cat.setNome(text);
                    categoriaRepository.save(cat);
                    break;

                case "titulo":
                    Title title = new Title();
                    title.setText(text);
                    titleRepository.save(title);
                    break;

                case "slides":
                    if (file == null || file.isEmpty()) {
                        return ResponseEntity.badRequest().body("Imagem é obrigatória para slides do cardápio");
                    }
                    Slide slide = new Slide();
                    slide.setText(text);
                    slide.setCategory(category);
                    slide.setImage(file.getBytes());
                    slideRepository.save(slide);
                    break;

                // Home
                case "home_title":
                    HomeTitle homeTitle = new HomeTitle();
                    homeTitle.setTitleHome(text);
                    homeTitleRepository.save(homeTitle);
                    break;

                case "home_slides":
                    if (file == null || file.isEmpty()) {
                        return ResponseEntity.badRequest().body("Imagem é obrigatória para slides da home");
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
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erro: " + e.getMessage());
        }
    }

    // Buscar último
    @GetMapping("/last")
    public ResponseEntity<?> getLast(@RequestParam String type) {
        try {
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
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erro no servidor");
        }
    }

    // Listar todos
    @GetMapping("/list")
    public ResponseEntity<?> listAll(@RequestParam String type) {
        try {
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
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erro no servidor");
        }
    }

    //  Deletar por ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteContent(@PathVariable Long id, @RequestParam String type) {
        try {
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
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erro: " + e.getMessage());
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
                .header("Content-Type", "image/jpeg")
                .body(slide.getSlideHome());
        }
}
