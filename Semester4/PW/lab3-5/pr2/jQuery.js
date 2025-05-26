$(document).ready(function () {
    $('#formular').on('submit', function (e) {
      e.preventDefault(); // oprește trimiterea formularului
  
      const $nume = $('#nume');
      const $data = $('#dataNasterii');
      const $varsta = $('#varsta');
      const $email = $('#email');
      const $mesaj = $('#mesaj');
  
      let campuriInvalide = [];
  
      // Șterge stilul anterior
      $nume.removeClass('eroare');
      $data.removeClass('eroare');
      $varsta.removeClass('eroare');
      $email.removeClass('eroare');
  
      // Validare nume
      const numeVal = $nume.val().trim();
      if (!numeVal || !/^[A-Za-zĂăÎîȘșȚț\s-]+$/.test(numeVal)) {
        campuriInvalide.push('nume');
        $nume.addClass('eroare');
      }
  
      // Validare data nașterii
      const dataVal = $data.val();
      if (!dataVal || new Date(dataVal) > new Date()) {
        campuriInvalide.push('data nașterii');
        $data.addClass('eroare');
      }
  
      // Validare vârstă
      const currentDateYear = new Date().getFullYear();
      const birthYear = new Date(dataVal).getFullYear();
      const varstaVal = parseInt($varsta.val(), 10);
      if (isNaN(varstaVal) || varstaVal < 0 || varstaVal > 120 || varstaVal != currentDateYear - birthYear) {
        campuriInvalide.push('vârstă');
        $varsta.addClass('eroare');
      }
  
      // Validare email
      const emailVal = $email.val().trim();
      const emailPattern = /^[a-zA-Z][a-zA-Z0-9_]*@[a-zA-Z]+\.[a-zA-Z]+$/;
      if (!emailVal || !emailPattern.test(emailVal)) {
        campuriInvalide.push('email');
        $email.addClass('eroare');
      }
  
      // Afișeaz mesaj
      if (campuriInvalide.length > 0) {
        $mesaj.css('color', 'red').html("Câmpurile: '" + campuriInvalide.join(', ') + "' nu sunt completate corect.");
      } else {
        $mesaj.css('color', 'green').text("Datele sunt completate corect.");
        // this.submit();
      }
    });
  });