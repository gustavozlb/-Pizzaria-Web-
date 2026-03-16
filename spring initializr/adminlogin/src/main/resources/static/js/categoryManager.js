import { updateSlides } from "./carousel.js";

function capitalize(str) {
  return str ? str.charAt(0).toUpperCase() + str.slice(1) : "";
}

export function loadCategoryMenu() {
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
              updateSlides(slidesHTML);
            });
        });
      });
    });
}

export function loadCategorySelected(context) {
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

export function loadDeleteOptions(type, context) {
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
    });
}

export function addContent(context) {
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
}

export function deleteContent(context) {
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
}
