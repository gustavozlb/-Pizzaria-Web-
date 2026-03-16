export function initCarousel() {
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
}

export function updateSlides(slidesHTML) {
  if (window._swiper) {
    window._swiper.removeAllSlides();
    window._swiper.appendSlide(slidesHTML);
    window._swiper.update();
  }
}