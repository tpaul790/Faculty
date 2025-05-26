<?php
header('Content-Type: application/json');

$data = json_decode(file_get_contents('php://input'), true);
$board = $data['board'];
$computer = $data['computer'];
$player = $computer === "X" ? "0" : "X";

// Funcție pentru verificare câștigător
function checkWin($board, $sym) {
  for ($i = 0; $i < 3; $i++) {
    if ($board[$i][0] === $sym && $board[$i][1] === $sym && $board[$i][2] === $sym) return true;
    if ($board[0][$i] === $sym && $board[1][$i] === $sym && $board[2][$i] === $sym) return true;
  }
  if ($board[0][0] === $sym && $board[1][1] === $sym && $board[2][2] === $sym) return true;
  if ($board[0][2] === $sym && $board[1][1] === $sym && $board[2][0] === $sym) return true;
  return false;
}

// Căutăm primul loc liber și punem mutarea
for ($i = 0; $i < 3; $i++) {
  for ($j = 0; $j < 3; $j++) {
    if ($board[$i][$j] === "") {
      $board[$i][$j] = $computer;
      break 2;
    }
  }
}

// Verificare câștig
$status = null;
if (checkWin($board, $computer)) {
  $status = "Calculatorul a câștigat!";
} elseif (checkWin($board, $player)) {
  $status = "Jucătorul a câștigat!";
} elseif (!in_array("", array_merge(...$board))) {
  $status = "Remiză!";
}

echo json_encode([
  "board" => $board,
  "status" => $status
]);