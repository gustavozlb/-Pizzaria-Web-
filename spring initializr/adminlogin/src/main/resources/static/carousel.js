window.addEventListener("DOMContentLoaded", () => {
  const logoutButton = document.getElementById("btnLogout");

  // Carrega título 
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

  // Verifica login 
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

  // Logout 
  if (logoutButton) {
    logoutButton.addEventListener("click", () => {
      fetch("/api/logout", { method: "POST", credentials: "include" })
        .then(() => window.location.href = "home.html")
        .catch(() => alert("Erro ao fazer logout"));
    });
  }

  // Swiper
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

  loadCategoryMenu();
});


// Função global para carregar opções de exclusão
function loadDeleteOptions(type) {
  const deleteSelect = document.getElementById("deleteSelect");
  if (!deleteSelect) return;

  deleteSelect.innerHTML = "";

  fetch(`/api/content/list?type=${type}`)
    .then(res => res.json())
    .then(resposta => {
      const itens = Array.isArray(resposta) ? resposta : resposta.data || [];

      if (!Array.isArray(itens) || itens.length === 0) {
        const opt = document.createElement("option");
        opt.value = "";
        opt.innerHTML = "Nenhum item disponível";
        deleteSelect.appendChild(opt);
        return;
      }

      itens.forEach(item => {
        const opt = document.createElement("option");
        opt.value = item.id;
        const texto = (item.text || item.nome || "")
          .replace(/<[^>]+>/g, '')
          .slice(0, 30);
        if (type === "slides") {
          opt.innerHTML = `${item.id} - ${item.category} - ${texto}`;
        } else {
          opt.innerHTML = `${item.id} - ${texto}`;
        }
        deleteSelect.appendChild(opt);
      });
    })
    .catch(err => {
      console.error("Erro ao carregar opções:", err);
      const opt = document.createElement("option");
      opt.value = "";
      opt.innerHTML = "Erro ao carregar";
      deleteSelect.appendChild(opt);
    });
}

// Painel Admin
function exibirPainelAdmin() {
  const wrapper = document.createElement("div");
  wrapper.className = "panel-wrapper";

  const panel = document.createElement("div");
  panel.className = "admin-panel";

  panel.innerHTML = `
    <h3 class="titulo-branco">Gerenciar Conteúdo</h3>
    <label for="contentType">type:</label>
    <select id="contentType">
      <option value="slides">Slides</option>
      <option value="titulo">Título</option>
      <option value="categoria">Categoria</option>
    </select>

    <div id="editor" style="height: 120px; background: white;"></div>
    <input type="file" id="itemFile" accept="image/*">
    <select id="selectedCategory"></select>

    <button onclick="addContent()">Adicionar</button>

    <div id="deleteWrapper">
      <label for="deleteSelect">Excluir item:</label>
      <select id="deleteSelect"></select>
      <button onclick="deleteContent()">Excluir</button>
    </div>
  `;

  wrapper.appendChild(panel);
  document.body.appendChild(wrapper);

  // Quill
  const quill = new Quill('#editor', {
    theme: 'snow',
    placeholder: 'Digite o texto...',
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
  });
  window._quillEditor = quill;

  // Carrega categorias no select
  function loadCategorySelected() {
    return fetch("/api/categorias")
      .then(res => res.json())
      .then(categorias => {
        const select = document.getElementById("selectedCategory");
        select.innerHTML = "";
        categorias.forEach(cat => {
          const option = document.createElement("option");
          option.value = cat.nome;
          option.innerHTML = cat.nome;
          select.appendChild(option);
        });
      });
  }

  // Alterna campos
  const toggleFields = () => {
    const type = document.getElementById("contentType").value;
    document.getElementById("selectedCategory").style.display = type === "slides" ? "block" : "none";
    document.getElementById("itemFile").style.display = type === "slides" ? "block" : "none";
    document.getElementById("deleteWrapper").style.display = type === "titulo" ? "none" : "block";

    if (type === "slides") {
      loadCategorySelected();
    }

    loadDeleteOptions(type);
  };

  document.getElementById("contentType").addEventListener("change", toggleFields);

  // Inicializa
  document.getElementById("contentType").value = "slides";
  toggleFields();
}

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

// Adicionar conteúdo
window.addContent = function() {
  const type = document.getElementById("contentType").value;
  const text = window._quillEditor.root.innerHTML.trim();
  const image = document.getElementById("itemFile").files[0];
  const category = document.getElementById("selectedCategory").value;

  const formData = new FormData();
  formData.append("type", type);
  formData.append("text", text);
  if (type === "slides") {
    formData.append("file", image);
    formData.append("category", category);
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
      loadDeleteOptions(type);
      if (type === "slides") {
        loadCategoryMenu();
      }
    })
    .catch(err => alert(err.message));
};

// Excluir conteúdo
window.deleteContent = function() {
  const type = document.getElementById("contentType").value;
  const id = document.getElementById("deleteSelect").value;

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
    loadDeleteOptions(type);
    if (type === "slides") {
      loadCategoryMenu();
    }
  })
  .catch(err => {
    console.error("Erro ao excluir:", err);
    alert("Erro ao excluir: " + err.message);
  });
};
