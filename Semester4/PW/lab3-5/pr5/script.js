const items = document.querySelectorAll('#carousel li');
const nextBtn = document.getElementById('nextBtn');
const prevBtn = document.getElementById('prevBtn');
let currentIndex = 0;
const intervalTime = 3000; 
let intervalId;

function showSlide(index) {
  items.forEach((item, i) => {
    if (i === index) {
        item.classList.add('active');
      } else {
        item.classList.remove('active');
      }
  });
}

function nextSlide() {
  currentIndex = (currentIndex + 1) % items.length;
  showSlide(currentIndex);
}

function prevSlide() {
  currentIndex = (currentIndex - 1 + items.length) % items.length;
  showSlide(currentIndex);
}

nextBtn.addEventListener('click', () => {
  nextSlide();
  resetInterval();
});

prevBtn.addEventListener('click', () => {
  prevSlide();
  resetInterval();
});

function resetInterval() {
  clearInterval(intervalId);
  intervalId = setInterval(nextSlide, intervalTime);
}

intervalId = setInterval(nextSlide, intervalTime);