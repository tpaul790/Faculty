let offset = 0;
const limit = 3;

const prevBtn = document.getElementById("prev");
const nextBtn = document.getElementById("next");
const tabelDiv = document.getElementById("tabel");

function loadPage() {
  const xhr = new XMLHttpRequest();
  xhr.open("GET", `server.php?offset=${offset}`, true);
  xhr.onreadystatechange = function () {
    if (xhr.status === 200 && xhr.readyState === 4) {
      const data = JSON.parse(xhr.responseText);
      const total = data.total;
      const contacte = data.contacte;

      let html = "<table><tr><th>Nume</th><th>Prenume</th><th>Telefon</th><th>Email</th></tr>";
      contacte.forEach(p => {
        html += `<tr>
          <td>${p.nume}</td>
          <td>${p.prenume}</td>
          <td>${p.telefon}</td>
          <td>${p.email}</td>
        </tr>`;
      });
      html += "</table>";
      tabelDiv.innerHTML = html;

      // Control butoane
      prevBtn.disabled = offset === 0;
      nextBtn.disabled = offset + limit >= total;
    }
  };
  xhr.send();
}

prevBtn.onclick = function () {
  if (offset >= limit) {
    offset -= limit;
    loadPage();
  }
};

nextBtn.onclick = function () {
  offset += limit;
  loadPage();
};

loadPage(); 