function getDestinations(plecare) {
  const $sosiri = $("#sosire");
  $sosiri.html('<li>Se încarcă...</li>');

  if (plecare !== "") {
    $.ajax({
      url: "script.php",
      type: "GET",
      data: { plecare: plecare },
      dataType: "json",
      success: function (destinatii) {
        $sosiri.empty(); // Golește lista
        if (destinatii.length > 0) {
          destinatii.forEach(function (oras) {
            $sosiri.append("<li>" + oras + "</li>");
          });
        } else {
          $sosiri.html('<li>Nu există destinații disponibile</li>');
        }
      },
      error: function () {
        $sosiri.html('<li>Eroare la încărcare</li>');
      }
    });
  } else {
    $sosiri.html('<li>-- Selectează stația de plecare mai întâi --</li>');
  }
}