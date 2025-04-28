function valideazaFormular() {
    let nume = document.getElementById("nume");
    let data = document.getElementById("dataNasterii");
    let varsta = document.getElementById("varsta");
    let email = document.getElementById("email");

    let campuriInvalide = [];

    [nume, data, varsta, email].forEach(camp => camp.classList.remove("eroare"));

    if (!nume.value.trim() || !/^[A-Za-zĂăÎîȘșȚț\s-]+$/.test(nume.value)) {
      campuriInvalide.push("nume");
      nume.classList.add("eroare");
    }

    if (!data.value || new Date(data.value) > new Date()) {
      campuriInvalide.push("data nașterii");
      data.classList.add("eroare");
    }

    if (!varsta.value || isNaN(varsta.value) || varsta.value < 0 || varsta.value > 120) {
      campuriInvalide.push("vârstă");
      varsta.classList.add("eroare");
    }

    let emailPattern = /^[a-zA-Z][a-zA-Z0-9_]*@[a-zA-Z]+\.[a-zA-Z]+$/;
    if (!email.value.trim() || !emailPattern.test(email.value)) {
      campuriInvalide.push("email");
      email.classList.add("eroare");
    }

    let mesaj = document.getElementById("mesaj");
    if (campuriInvalide.length > 0) {
      mesaj.style.color = "red";
      mesaj.textContent = "Câmpurile: '" + campuriInvalide + "' nu sunt completate corect.";
      return false; // nu trimitem formularul
    } else {
      mesaj.style.color = "green";
      mesaj.textContent = "Datele sunt completate corect.";
      return false; // nu trimitem formularul
    }
  }