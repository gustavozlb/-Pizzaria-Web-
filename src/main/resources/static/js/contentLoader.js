import { updateSlides } from "./carousel.js";

export async function loadTitle(type, elementId, fallbackText) {
  try {
    const res = await fetch(`/api/content/last?type=${type}`);
    if (!res.ok) throw new Error("Erro ao carregar título");
    const data = await res.json();
    const el = document.getElementById(elementId);
    if (el) {
      el.innerHTML = data.text || data.titleHome || fallbackText;
    }
  } catch (err) {
    console.error(err);
    const el = document.getElementById(elementId);
    if (el) el.innerHTML = fallbackText;
  }
}

export async function loadSlides(type) {
  try {
    // usar /list para garantir que venha uma lista
    const res = await fetch(`/api/content/list?type=${type}`);
    if (!res.ok) throw new Error("Erro ao carregar slides");
    const slides = await res.json();
    if (!Array.isArray(slides)) return;

    const slidesHTML = slides.map(item => `
      <div class="swiper-slide">
        <img src="/api/content/${type}/image/${item.id}" />
        <div class="text-box"><p>${item.text || ""}</p></div>
      </div>
    `);

    updateSlides(slidesHTML);
  } catch (err) {
    console.error("Erro ao carregar slides:", err);
  }
}
