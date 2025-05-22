const idSelect = document.getElementById("idSelect");
const form = document.getElementById("form");
const saveBtn = document.getElementById("saveBtn");
const numeInput = document.getElementById("nume");
const prenumeInput = document.getElementById("prenume");
const telefonInput = document.getElementById("telefon");

let currentId = null;
let formModified = false;

function loadIds() {
    const xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if(xhr.status === 200 && xhr.readyState === 4){
            const ids = JSON.parse(this.responseText);
            idSelect.innerHTML = "";
            ids.forEach(id => {
                const opt = document.createElement("option");
                opt.value = id;
                opt.textContent = `ID ${id}`;
                idSelect.appendChild(opt);
            });
            if (ids.length > 0) {
                currentId = ids[0];
                idSelect.value = currentId;
                loadFormData(currentId);
            }
        };
    }
    xhr.open("GET", "server.php?action=get_ids");
    xhr.send();
}

function loadFormData(id) {
  const xhr = new XMLHttpRequest();
  xhr.onreadystatechange = function () {
    if(xhr.status === 200 && xhr.readyState === 4){
        const data = JSON.parse(this.responseText);
        numeInput.value = data.nume;
        prenumeInput.value = data.prenume;
        telefonInput.value = data.telefon;
        formModified = false;
        saveBtn.disabled = true;
    }
  };
  xhr.open("GET", `server.php?action=get_data&id=${id}`);
  xhr.send();
}

[idSelect].forEach(el => el.addEventListener("change", () => {
  if (formModified) {
    const confirmSave = confirm("Modificările nu au fost salvate. Vrei să le salvezi?");
    if (confirmSave) {
      saveData(() => {
        currentId = idSelect.value;
        loadFormData(currentId);
      });
    } else {
      currentId = idSelect.value;
      loadFormData(currentId);
    }
  } else {
    currentId = idSelect.value;
    loadFormData(currentId);
  }
}));

[numeInput, prenumeInput, telefonInput].forEach(input => {
  input.addEventListener("input", () => {
    formModified = true;
    saveBtn.disabled = false;
  });
});

form.addEventListener("submit", function (e) {
  e.preventDefault();
  saveData();
});

function saveData(callback) {
  const xhr = new XMLHttpRequest();
  xhr.open("POST", "server.php");
  xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

  const data = `action=save&id=${currentId}&nume=${encodeURIComponent(numeInput.value)}&prenume=${encodeURIComponent(prenumeInput.value)}&telefon=${encodeURIComponent(telefonInput.value)}`;

  xhr.onreadystatechange = function () {
    if(xhr.status === 200 && xhr.readyState === 4){
        alert("Salvat cu succes!");
        formModified = false;
        saveBtn.disabled = true;
        if (callback) 
            callback();
    }
  };
  xhr.send(data);
}

loadIds();