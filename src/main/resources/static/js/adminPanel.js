import { loadCategorySelected, loadDeleteOptions, addContent, deleteContent } from "./categoryManager.js";

window.addContent = addContent;
window.deleteContent = deleteContent;

export function renderAdminPanel() {
  const wrapper = document.createElement("div");
  wrapper.className = "panel-wrapper";
  const panel = document.createElement("div");
  panel.className = "admin-panel";

  
  panel.innerHTML = `
    <div class="tabs">
      <button class="tab-button" data-tab="cardapio">Cardápio</button>
      <button class="tab-button" data-tab="home">Home</button>
    </div>

    <!-- Conteúdo Cardápio -->
    <div class="tab-content active" id="tab-cardapio">
      <h3>Gerenciar Conteúdo (Cardápio)</h3>
      <label for="contentTypeCardapio">Tipo:</label>
      <select id="contentTypeCardapio">
        <option value="slides">Slides</option>
        <option value="titulo">Título</option>
        <option value="categoria">Categoria</option>
      </select>

      <div id="editorCardapio" style="height:120px; background:white;"></div>
      <div id="fileWrapperCardapio"><input type="file" id="itemFileCardapio" accept="image/*"></div>
      <div id="categoryWrapperCardapio"><select id="selectedCategoryCardapio"></select></div>

      <button onclick="addContent('cardapio')">Adicionar</button>

      <div id="deleteWrapperCardapio">
        <label for="deleteSelectCardapio">Excluir item:</label>
        <select id="deleteSelectCardapio"></select>
        <button onclick="deleteContent('cardapio')">Excluir</button>
      </div>
    </div>

    <!-- Conteúdo Home -->
    <div class="tab-content" id="tab-home">
      <h3>Gerenciar Conteúdo (Home)</h3>
      <label for="contentTypeHome">Tipo:</label>
      <select id="contentTypeHome">
        <option value="home_slides">Slides Home</option>
        <option value="home_title">Título Home</option>
      </select>

      <div id="editorHome" style="height:120px; background:white;"></div>
      <div id="fileWrapperHome"><input type="file" id="itemFileHome" accept="image/*"></div>

      <button onclick="addContent('home')">Adicionar</button>

      <div id="deleteWrapperHome">
        <label for="deleteSelectHome">Excluir item:</label>
        <select id="deleteSelectHome"></select>
        <button onclick="deleteContent('home')">Excluir</button>
      </div>
    </div>
  `;


  wrapper.appendChild(panel);
  document.body.appendChild(wrapper);

  // Alternar abas
  document.querySelectorAll(".tab-button").forEach(btn => {
    btn.addEventListener("click", () => {
      document.querySelectorAll(".tab-content").forEach(c => c.classList.remove("active"));
      document.getElementById(`tab-${btn.dataset.tab}`).classList.add("active");
    });
  });

  // Inicializa Quill
  const quillConfig = {
  theme: 'snow',
  placeholder: 'Digite o conteúdo...',
  modules: {
    toolbar: [
      [{ header: [1, 2, false] }],
      ['bold', 'italic', 'underline'],
      [{ size: ['small', false, 'large', 'huge'] }],
      [{ color: [] }, { background: [] }],
      [{ align: [] }],
      ['link', 'clean']
    ]
  }
};

window._quillEditors = {
  cardapio: new Quill('#editorCardapio', quillConfig),
  home: new Quill('#editorHome', quillConfig)
};

  // Configura campos Cardápio
  const contentTypeCardapio = document.getElementById("contentTypeCardapio");
  contentTypeCardapio.addEventListener("change", () => {
    const type = contentTypeCardapio.value;
    document.getElementById("fileWrapperCardapio").style.display = type === "slides" ? "block" : "none";
    document.getElementById("categoryWrapperCardapio").style.display = type === "slides" ? "block" : "none";
    document.getElementById("deleteWrapperCardapio").style.display = (type === "titulo" || type === "home_title") ? "none" : "block";
    loadDeleteOptions(type, "cardapio");
    if (type === "slides") loadCategorySelected("cardapio");
  });
  contentTypeCardapio.dispatchEvent(new Event("change"));

  // Configura campos Home
  const contentTypeHome = document.getElementById("contentTypeHome");
  contentTypeHome.addEventListener("change", () => {
    const type = contentTypeHome.value;
    document.getElementById("fileWrapperHome").style.display = type === "home_slides" ? "block" : "none";
    document.getElementById("deleteWrapperHome").style.display = type === "home_title" ? "none" : "block";
    loadDeleteOptions(type, "home");
  });
  contentTypeHome.dispatchEvent(new Event("change"));
}