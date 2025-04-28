    const linii = 4, coloane = 4;
    const tabla = document.getElementById("tabla");
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
      for(let img of imagini)
        perechi.push(img,img);
      amesteca(perechi);
      return perechi;
    }

    function creeazaTabla() {
      valori = genereazaValori();
      // valori = genereazaImagini();
      let index = 0;
      for (let i = 0; i < linii; i++) {
        const rand = document.createElement("tr");
        for (let j = 0; j < coloane; j++) {
          const celula = document.createElement("td");
          celula.dataset.valoare = valori[index++];
          celula.classList.add("hidden");
          celula.addEventListener("click", function () {
            dacaApasPeCelula(celula);
          });
          rand.appendChild(celula);
        }
        tabla.appendChild(rand);
      }
    }

    function dacaApasPeCelula(celula) {
      if (blocat || celula.classList.contains("revealed")) return;

      celula.textContent = celula.dataset.valoare;
      celula.classList.remove("hidden");

      if (!primaCelula) {
        primaCelula = celula;
      } else {
        if(primaCelula === celula)
          return; // nu poate selecta aceeasi casuta ca sa para ca a gasit o pereche
        if (primaCelula.dataset.valoare === celula.dataset.valoare) {
          // Pereche gasita
          primaCelula.classList.add("revealed");
          celula.classList.add("revealed");
          primaCelula = null;
          perechiGhicite++;
          if (perechiGhicite === (linii * coloane) / 2) {
            document.getElementById("mesajFinal").textContent = "Felicitări! Ai găsit toate perechile!";
          }
        } else {
          // Nu sunt pereche
          blocat = true;
          setTimeout(() => {
            primaCelula.textContent = "";
            primaCelula.classList.add("hidden");
            celula.textContent = "";
            celula.classList.add("hidden");
            primaCelula = null;
            blocat = false;
          }, 1000);
        }
      }
    }

    creeazaTabla();