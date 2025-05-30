let offset = 0;
const limit = 3;

const $prevBtn = $("#prev");
const $nextBtn = $("#next");
const $tabelDiv = $("#tabel");

function loadPage() {
  $.ajax({
    url: "server.php",
    type: "GET",
    data: { offset: offset },
    dataType: "json",
    success: function (data) {
      const total = data.total;
      const contacte = data.contacte;

      let html = "<table><tr><th>Nume</th><th>Prenume</th><th>Telefon</th><th>Email</th></tr>";
      contacte.forEach(function (p) {
        html += `<tr>
          <td>${p.nume}</td>
          <td>${p.prenume}</td>
          <td>${p.telefon}</td>
          <td>${p.email}</td>
        </tr>`;
      });
      html += "</table>";

      $tabelDiv.html(html);

      // Control butoane
      $prevBtn.prop("disabled", offset === 0);
      $nextBtn.prop("disabled", offset + limit >= total);
    },
    error: function () {
      $tabelDiv.html("<p>Eroare la încărcare!</p>");
    }
  });
}

$prevBtn.on("click", function () {
  if (offset >= limit) {
    offset -= limit;
    loadPage();
  }
});

$nextBtn.on("click", function () {
  offset += limit;
  loadPage();
});

loadPage();