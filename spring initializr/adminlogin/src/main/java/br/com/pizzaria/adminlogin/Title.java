package br.com.pizzaria.adminlogin;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "title") 
public class Title {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

    @PrePersist
    protected void onCreate() {
        this.criadoEm = LocalDateTime.now();
    }

    // Getters e Setters
    public Long getId() { return id; }

    public String getText() { return text; }

    public void setText(String text) { this.text = text; }

    public LocalDateTime getCriadoEm() { return criadoEm; }
}
