const board = document.getElementById("board");
const statusDiv = document.getElementById("status");

let player = "";  // "X" sau "0"
let computer = "";

function drawBoard() {
  board.innerHTML = "";
  for (let i = 0; i < 3; i++) {
    const row = document.createElement("tr");
    for (let j = 0; j < 3; j++) {
      const cell = document.createElement("td");
      cell.dataset.row = i;
      cell.dataset.col = j;
      cell.textContent = table[i][j];
      if (table[i][j] !== "") {
        cell.classList.add("disabled");
      } else {
        cell.addEventListener("click", playerMove);
      }
      row.appendChild(cell);
    }
    board.appendChild(row);
  }
}

function playerMove(e) {
  const row = parseInt(e.target.dataset.row);
  const col = parseInt(e.target.dataset.col);
  if (table[row][col] === "") {
    table[row][col] = player;
    drawBoard();
    checkGameStatus();

    const xhr = new XMLHttpRequest();
    xhr.open("POST", "move.php", true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function () {
      if (xhr.readyState === 4 && xhr.status === 200) {
        const resp = JSON.parse(xhr.responseText);
        table = resp.board;
        drawBoard();
        if (resp.status) {
          statusDiv.textContent = resp.status;
          disableAll();
        }
      }
    };
    xhr.send(JSON.stringify({ board: table, computer: computer }));
  }
}

function disableAll() {
  document.querySelectorAll("td").forEach(td => td.classList.add("disabled"));
}

function checkGameStatus() {
  const flat = table.flat();
  if (!flat.includes("")) {
    statusDiv.textContent = "Remiză!";
    disableAll();
  }
}

function initGame() {
  table = Array(3).fill().map(() => Array(3).fill(""));
  player = Math.random() < 0.5 ? "X" : "0";
  computer = player === "X" ? "0" : "X";
  statusDiv.textContent = `Jucătorul este ${player}, calculatorul este ${computer}`;
  drawBoard();

  // Dacă începe calculatorul
  if (computer === "X") {
    const xhr = new XMLHttpRequest();
    xhr.open("POST", "move.php", true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function () {
      if (xhr.readyState === 4 && xhr.status === 200) {
        const resp = JSON.parse(xhr.responseText);
        table = resp.board;
        drawBoard();
      }
    };
    xhr.send(JSON.stringify({ board: table, computer: computer }));
  }
}

initGame();