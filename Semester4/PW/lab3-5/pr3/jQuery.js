  const linii = 4, coloane = 4;
  let valori = [];
  let primaCelula = null;
  let blocat = false;
  let perechiGhicite = 0;
  const imagini = ["🍎", "🍌", "🍇", "🍓", "🍍", "🥝", "🍒", "🍉"];

  function amesteca(array) {
    for (let i = array.length - 1; i > 0; i--) {
      const j = Math.floor(Math.random() * (i + 1));
      [array[i], array[j]] = [array[j], array[i]];
    }
  }

  function genereazaValori() {
    const perechi = [];
    for (let i = 1; i <= (linii * coloane) / 2; i++) {
      perechi.push(i, i);
    }
    amesteca(perechi);
    return perechi;
  }

  function genereazaImagini() {
    const perechi = [];
    for (let img of imagini) {
      perechi.push(img, img);
    }
    amesteca(perechi);
    return perechi;
  }

  function creeazaTabla() {
    // valori = genereazaValori();
    valori = genereazaImagini();
    let index = 0;

    const $tabla = $("#tabla");
    $tabla.empty();

    for (let i = 0; i < linii; i++) {
      const $rand = $("<tr></tr>");
      for (let j = 0; j < coloane; j++) {
        const $celula = $("<td></td>")
          .data("valoare", valori[index++])
          .addClass("hidden")
          .on("click", function () {
            dacaApasPeCelula($(this));
          });
        $rand.append($celula);
      }
      $tabla.append($rand);
    }
  }

  function dacaApasPeCelula($celula) {
    if (blocat || $celula.hasClass("revealed")) return;

    $celula.text($celula.data("valoare")).removeClass("hidden");

    if (!primaCelula) {
      primaCelula = $celula;
    } else {
      if (primaCelula.is($celula)) return; // nu poate selecta aceeasi casuta

      if (primaCelula.data("valoare") === $celula.data("valoare")) {
        // Pereche gasita
        primaCelula.addClass("revealed");
        $celula.addClass("revealed");
        primaCelula = null;
        perechiGhicite++;

        if (perechiGhicite === (linii * coloane) / 2) {
          $("#mesajFinal").text("Felicitări! Ai găsit toate perechile!");
        }
      } else {
        // Nu sunt pereche
        blocat = true;
        setTimeout(() => {
          primaCelula.text("").addClass("hidden");
          $celula.text("").addClass("hidden");
          primaCelula = null;
          blocat = false;
        }, 1000);
      }
    }
  }

  $(document).ready(function () {
    creeazaTabla();
  });