import { initCarousel } from "./carousel.js";
import { loadTitle, loadSlides } from "./contentLoader.js";
import { setupLogoutButton, checkAdminStatus } from "./authManager.js";
import { loadCategoryMenu } from "./categoryManager.js";
import { renderAdminPanel } from "./adminPanel.js";

window.addEventListener("DOMContentLoaded", () => {
  // Inicializa carrossel se existir
  if (document.querySelector(".mySwiper")) {
    initCarousel();
  }

  // Carrega título do cardápio (sempre)
  loadTitle("titulo", "tituloSite", "Cardápio 🍕");

  // Página home
  if (window.location.pathname.includes("home")) {
    loadTitle("home_title", "homeTitle", "Home");
    loadSlides("home_slides");
  }

  // Página manager
  if (window.location.pathname.includes("manager")) {
    checkAdminStatus(renderAdminPanel);
  }

  // Configura botão de logout
  setupLogoutButton();

  // Carrega categorias no menu
  loadCategoryMenu();
});
