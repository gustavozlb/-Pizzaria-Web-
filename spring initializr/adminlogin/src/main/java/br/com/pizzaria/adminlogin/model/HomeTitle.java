package br.com.pizzaria.adminlogin.model;

import jakarta.persistence.*;

@Entity
public class HomeTitle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String titleHome;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitleHome() { return titleHome; }
    public void setTitleHome(String titleHome) { this.titleHome = titleHome; }
}
