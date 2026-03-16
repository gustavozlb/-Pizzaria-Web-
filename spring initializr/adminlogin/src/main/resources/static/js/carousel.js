window.addEventListener("DOMContentLoaded", () => {
  const logoutButton = document.getElementById("btnLogout");

  // Swiper primeiro!
  if (typeof Swiper !== "undefined") {
    window._swiper = new Swiper(".mySwiper", {
      loop: true,
      spaceBetween: 8,
      slidesPerView: 3,
      navigation: {
        nextEl: ".swiper-button-next",
        prevEl: ".swiper-button-prev",
      },
    });
  }

  // Carrega título do cardápio (sempre)
  fetch("/api/content/last?type=titulo")
    .then(response => {
      if (!response.ok) throw new Error("Erro ao carregar título");
      return response.json();
    })
    .then(data => {
      const titulo = document.getElementById("tituloSite");
      if (titulo) titulo.innerHTML = data.text;
    })
    .catch(error => {
      console.error(error);
      const titulo = document.getElementById("tituloSite");
      if (titulo) titulo.innerHTML = "Cardápio 🍕";
    });

  // Verifica login se for página manager
  if (window.location.pathname.includes("manager")) {
    fetch("/api/admin/status", { credentials: "include" })
      .then(res => {
        if (!res.ok) throw new Error("Não autorizado");
        return res.json();
      })
      .then(data => {
        if (data.status === "ok") {
          exibirPainelAdmin();
        } else {
          window.location.href = "home.html";
        }
      })
      .catch(() => {
        window.location.href = "home.html";
      });
  }

  // Só executa na página home.html
  if (window.location.pathname.includes("home")) {
    // Carrega título da home
    fetch("/api/content/last?type=home_title")
      .then(res => {
        if (!res.ok) throw new Error("Erro ao carregar título da home");
        return res.json();
      })
      .then(data => {
        const homeTitle = document.getElementById("homeTitle");
        if (homeTitle) homeTitle.innerHTML = data.titleHome;
      })
      .catch(err => {
        console.error("Erro ao carregar título da home:", err);
      });

    // Carrega slides da home
    fetch("/api/content/last?type=home_slides")
      .then(res => res.json())
      .then(slides => {
        if (!Array.isArray(slides)) return;
        const slidesHTML = slides.map((item, index) => `
          <div class="swiper-slide">
            <img src="/api/content/home_slides/image/${item.id}" />
            <div class="text-box">
              <p>${item.text || ""}</p>
            </div>
          </div>
        `);
        if (window._swiper) {
          window._swiper.removeAllSlides();
          window._swiper.appendSlide(slidesHTML);
          window._swiper.update();
        }
      })
      .catch(err => {
        console.error("Erro ao carregar slides da home:", err);
      });
  }

  // Logout
  if (logoutButton) {
    logoutButton.addEventListener("click", () => {
      fetch("/api/logout", { method: "POST", credentials: "include" })
        .then(() => window.location.href = "home.html")
        .catch(() => alert("Erro ao fazer logout"));
    });
  }

  loadCategoryMenu();
});



// painel admin
function exibirPainelAdmin() {
  const wrapper = document.createElement("div");
  wrapper.className = "panel-wrapper";

  const panel = document.createElement("div");
  panel.className = "admin-panel";

  panel.innerHTML = `
    <div class="tabs">
      <button class="tab-button" data-tab="cardapio">Cardápio</button>
      <button class="tab-button" data-tab="home">Home</button>
    </div>


    <div class="tab-content active" id="tab-cardapio">
      <h3 class="titulo-branco">Gerenciar Conteúdo (Cardápio)</h3>
      
      <label for="contentTypeCardapio">Tipo:</label>
      <select id="contentTypeCardapio">
        <option value="slides">Slides</option>
        <option value="titulo">Título</option>
        <option value="categoria">Categoria</option>
      </select>

      <div id="editorCardapio" style="height: 120px; background: white;"></div>
      
      <div id="fileWrapperCardapio">
        <input type="file" id="itemFileCardapio" accept="image/*">
      </div>
      
      <div id="categoryWrapperCardapio">
        <select id="selectedCategoryCardapio"></select>
      </div>

      <button onclick="addContent('cardapio')">Adicionar</button>

      <div id="deleteWrapperCardapio">
        <label for="deleteSelectCardapio">Excluir item:</label>
        <select id="deleteSelectCardapio"></select>
        <button onclick="deleteContent('cardapio')">Excluir</button>
      </div>
    </div>

    <div class="tab-content" id="tab-home">
      <h3 class="titulo-branco">Gerenciar Conteúdo (Home)</h3>
      
      <label for="contentTypeHome">Tipo:</label>
      <select id="contentTypeHome">
        <option value="home_slides">Slides Home</option>
        <option value="home_title">Título Home</option>
      </select>

      <div id="editorHome" style="height: 120px; background: white;"></div>
      
      <div id="fileWrapperHome">
        <input type="file" id="itemFileHome" accept="image/*">
      </div>

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
  document.querySelectorAll(".tab-button").forEach((btn) => {
    btn.addEventListener("click", () => {
      const tab = btn.dataset.tab;
      document.querySelectorAll(".tab-content").forEach((c) =>
        c.classList.remove("active")
      );
      document.getElementById(`tab-${tab}`).classList.add("active");
    });
  });

    // Quill
  window._quillEditors = {
    cardapio: new Quill('#editorCardapio', {
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
    }),
    home: new Quill('#editorHome', {
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
    })
  };

  // Mostrar/ocultar campos do Cardápio
  const contentTypeCardapio = document.getElementById("contentTypeCardapio");
  const fileWrapperCardapio = document.getElementById("fileWrapperCardapio");
  const categoryWrapperCardapio = document.getElementById("categoryWrapperCardapio");

  function updateCardapioFields() {
  const type = contentTypeCardapio.value;
  fileWrapperCardapio.style.display = type === "slides" ? "block" : "none";
  categoryWrapperCardapio.style.display = type === "slides" ? "block" : "none";
  document.getElementById("deleteWrapperCardapio").style.display = (type === "titulo" || type === "home_title") ? "none" : "block";

  loadDeleteOptions(type, "cardapio");
  if (type === "slides") loadCategorySelected("cardapio");
}

  contentTypeCardapio.addEventListener("change", updateCardapioFields);
  updateCardapioFields();

  // Mostrar/ocultar campos do Home
  const contentTypeHome = document.getElementById("contentTypeHome");
  const fileWrapperHome = document.getElementById("fileWrapperHome");

function updateHomeFields() {
  const type = contentTypeHome.value;
  const deleteWrapperHome = document.getElementById("deleteWrapperHome");
  const fileWrapperHome = document.getElementById("fileWrapperHome");

  fileWrapperHome.style.display = type === "home_slides" ? "block" : "none";
  deleteWrapperHome.style.display = type === "home_title" ? "none" : "block";


  loadDeleteOptions(type, "home");
}

contentTypeHome.addEventListener("change", updateHomeFields);
updateHomeFields();
}
function capitalize(str) {
  if (!str) return "";
  return str.charAt(0).toUpperCase() + str.slice(1);
}

// Carrega categorias no select
function loadCategorySelected(context) {
  const select = document.getElementById(`selectedCategory${capitalize(context)}`);
  if (!select) return;
  fetch("/api/categorias")
    .then(res => res.json())
    .then(categorias => {
      select.innerHTML = "";
      categorias.forEach(cat => {
        const option = document.createElement("option");
        option.value = cat.nome;
        option.innerHTML = cat.nome;
        select.appendChild(option);
      });
    });
}

// Carrega opções de exclusão
function loadDeleteOptions(type, context) {
  const deleteSelect = document.getElementById(`deleteSelect${capitalize(context)}`);
  if (!deleteSelect) return;
  deleteSelect.innerHTML = "";

  fetch(`/api/content/list?type=${type}`)
    .then(res => res.json())
    .then(itens => {
      const data = Array.isArray(itens) ? itens : itens.data || [];
      if (data.length === 0) {
        const opt = document.createElement("option");
        opt.value = "";
        opt.innerHTML = "Nenhum item disponível";
        deleteSelect.appendChild(opt);
        return;
      }
      data.forEach(item => {
        const opt = document.createElement("option");
        opt.value = item.id;
        const texto = (item.text || item.nome || "").replace(/<[^>]+>/g, '').slice(0, 30);
        opt.innerHTML = `${item.id} - ${texto}`;
        deleteSelect.appendChild(opt);
      });
    })
    .catch(err => {
      console.error("Erro ao carregar opções:", err);
    });
}

// Adicionar conteúdo
window.addContent = function(context) {
  const type = document.getElementById(`contentType${capitalize(context)}`).value;
  const editor = window._quillEditors[context];
  const text = editor.root.innerHTML.trim();
  const fileInput = document.getElementById(`itemFile${capitalize(context)}`);
  const image = fileInput ? fileInput.files[0] : null;
  const categorySelect = document.getElementById(`selectedCategory${capitalize(context)}`);
  const category = categorySelect ? categorySelect.value : null;

  const formData = new FormData();
  formData.append("type", type);
  formData.append("text", text);
  if (type.includes("slides")) {
    formData.append("file", image);
    if (category) formData.append("category", category);
  } else if (type === "categoria") {
    if (category) formData.append("nome", category);
  }

  fetch("/api/content/add", {
    method: "POST",
    credentials: "include",
    body: formData
  })
    .then(r => {
      if (!r.ok) throw new Error("Erro ao adicionar");
      return r.text();
    })
    .then(() => {
      alert("Conteúdo adicionado com sucesso!");
      loadDeleteOptions(type, context);
      if (type === "slides") loadCategoryMenu();
    })
    .catch(err => alert(err.message));
};

// Excluir conteúdo
window.deleteContent = function(context) {
  const type = document.getElementById(`contentType${capitalize(context)}`).value;
  const id = document.getElementById(`deleteSelect${capitalize(context)}`).value;
  if (!id) {
    alert("Selecione um item para excluir.");
    return;
  }

  fetch(`/api/content/delete/${id}?type=${type}`, {
    method: "DELETE",
    credentials: "include"
  })
    .then(r => {
      if (!r.ok) return r.text().then(msg => { throw new Error(msg); });
      return r.text();
    })
    .then(() => {
      alert("Excluído com sucesso!");
      loadDeleteOptions(type, context);
      if (type === "slides") loadCategoryMenu();
    })
    .catch(err => {
      console.error("Erro ao excluir:", err);
      alert("Erro ao excluir: " + err.message);
    });
};

// Carrega categorias no menu
function loadCategoryMenu() {
  const container = document.getElementById("categoryButtons");
  if (!container) return;

  fetch("/api/categorias")
    .then(res => res.json())
    .then(categorias => {
      container.innerHTML = "";
      categorias.forEach(cat => {
        const button = document.createElement("button");
        button.className = "category-button btn-categoria";
        button.setAttribute("data-type", cat.nome);
        button.innerHTML = cat.nome;
        container.appendChild(button);

        button.addEventListener("click", () => {
          fetch(`/api/slides?category=${cat.nome}`)
            .then(r => r.json())
            .then(slides => {
              if (!Array.isArray(slides)) return;
              const slidesHTML = slides.map(item => `
                <div class="swiper-slide" data-category="${item.category}">
                  <img src="/api/slides/image/${item.id}" alt="${item.text}" />
                  <div class="text-box">${item.text}</div>
                </div>
              `);
              if (window._swiper) {
                window._swiper.removeAllSlides();
                window._swiper.appendSlide(slidesHTML);
                window._swiper.update();
              }
            });
        });
      });
    });
}
