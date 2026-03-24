# 🍕 Pizzaria Web

Sistema completo de gerenciamento de uma pizzaria com painel administrativo, API REST e galeria dinâmica de slides categorizados.

---

## 📌 Sobre o Projeto

Este projeto permite o gerenciamento completo do conteúdo de uma pizzaria, incluindo:

- Cadastro de categorias
- Upload de imagens (slides)
- Gerenciamento de textos (títulos)
- Painel administrativo
- Exibição dinâmica no frontend

A API foi construída para ser **totalmente administrável**, permitindo fácil integração com qualquer frontend.

---

## 🚀 Funcionalidades

### 📋 Cardápio
- Criar categorias
- Listar categorias
- Evitar duplicação (constraint no banco)
- Upload de slides com imagem + texto
- Filtro por categoria
- Exclusão por ID

### 🏠 Home
- Título principal editável
- Slides da home com imagem + texto
- Listagem de todos os slides
- Endpoint para servir imagens

---

## 🔒 Segurança

- Sanitização de HTML (proteção contra XSS)
- Validação de imagens:
  - JPG, PNG, WEBP
  - Máximo: 5MB

---

## 🛠️ Tecnologias Utilizadas

- Java 21
- Spring Boot 3.5
- Spring Data JPA
- MySQL / MariaDB
- Maven
- Docker & Docker Compose
- OWASP Java HTML Sanitizer
- HTML5, CSS3, JavaScript
- Swiper.js

---

## 🐳 Rodando com Docker (RECOMENDADO)

### 📌 Pré-requisitos

- Docker
- Docker Compose

---

### ▶️ Passos

```bash
# Clonar repositório
git clone https://github.com/gustavozlb/-Pizzaria-Web-

# Entrar na pasta
cd adminlogin

# Subir containers
docker compose up --build
```

👨‍💻 Autor

https://www.linkedin.com/in/gustavo-zimermann-lopes-barroso-710789348/
