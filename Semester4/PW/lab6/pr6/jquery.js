function fetchFiltered() {
  const params = {
    producator: $('#producator').val(),
    procesor: $('#procesor').val(),
    memorie: $('#memorie').val(),
    hdd: $('#hdd').val(),
    placa_video: $('#placa_video').val()
  };

  $.ajax({
    url: 'server.php',
    type: 'GET',
    data: params,
    dataType: 'json',
    success: function (data) {
      const $ul = $('#rezultate');
      $ul.empty(); // curăță lista anterioară

      if (data.length === 0) {
        $ul.html('<li>Nu există rezultate</li>');
      } else {
        data.forEach(n => {
          const li = `<li>${n.producator}, ${n.procesor}, ${n.memorie}, ${n.hdd}, ${n.placa_video}</li>`;
          $ul.append(li);
        });
      }
    },
    error: function () {
      $('#rezultate').html('<li>Eroare la încărcare</li>');
    }
  });
}

// Apelează inițial la pornire
$(document).ready(fetchFiltered);