$(function () {
    const $items = $('#carousel li');
    const $nextBtn = $('#nextBtn');
    const $prevBtn = $('#prevBtn');
    const intervalTime = 3500;
    let currentIndex = 0;
    let intervalId;
  
    function showSlide(index) {
      $items.removeClass('active').eq(index).addClass('active');
    }
  
    function nextSlide() {
      currentIndex = (currentIndex + 1) % $items.length;
      showSlide(currentIndex);
    }
  
    function prevSlide() {
      currentIndex = (currentIndex - 1 + $items.length) % $items.length;
      showSlide(currentIndex);
    }
  
    function resetInterval() {
      clearInterval(intervalId);
      intervalId = setInterval(nextSlide, intervalTime);
    }
  
    $nextBtn.on('click', () => {
      nextSlide();
      resetInterval();
    });
  
    $prevBtn.on('click', () => {
      prevSlide();
      resetInterval();
    });
  
    intervalId = setInterval(nextSlide, intervalTime);
  });