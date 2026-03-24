package br.com.pizzaria.adminlogin.model;

import jakarta.persistence.*;

@Entity
@Table(name = "home_slides")
public class HomeSlide {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "slide_home", nullable = false, length = 16777215)
    private byte[] slideHome;

    @Column(name = "text", length = 1000) 
    private String text;

    // Getters e Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getSlideHome() {
        return slideHome;
    }
    public void setSlideHome(byte[] slideHome) {
        this.slideHome = slideHome;
    }

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
}
