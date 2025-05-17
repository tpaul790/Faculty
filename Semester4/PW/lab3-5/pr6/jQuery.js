const imageURL = "sprite.jpg";
const n = 4;
let matrix = [];
let emptyRow, emptyCol;

function generateMatrix() {
  let nums = [];
  for (let i = 0; i < n ** 2; i++) {
    nums.push(i);
  }

  for (let i = nums.length - 1; i > 0; i--) {
    const j = Math.floor(Math.random() * (i + 1));
    [nums[i], nums[j]] = [nums[j], nums[i]];
  }

  matrix = [];
  for (let i = 0; i < n; i++) {
    matrix[i] = [];
    for (let j = 0; j < n; j++) {
      const value = nums[i * n + j];
      matrix[i][j] = value;
      if (value === 0) {
        emptyRow = i;
        emptyCol = j;
      }
    }
  }
}

function renderMatrix() {
    const $puzzle = $('#puzzle')
    $puzzle.empty();
    for (let i = 0; i < n; i++) {
      const $row = $('<tr></tr>')
      for (let j = 0; j < n; j++) {
        const $cell = $('<td></td>')
        const value = matrix[i][j];
        if (value !== 0) {
          $cell.text(value);
        } else {
          $cell.addClass('empty');
        }
        $row.append($cell);
      }
      $puzzle.append($row);
    }
}

function renderMatrixImage() {
  const $puzzle = $('#puzzle');
  $puzzle.empty();

  for (let i = 0; i < n; i++) {
    const $row = $('<tr></tr>');
    for (let j = 0; j < n; j++) {
      const index = matrix[i][j];
      const $td = $('<td></td>');

      if (index !== 0) {
        const rowInImage = Math.floor(index / n);
        const colInImage = index % n;
        $td.css({
          backgroundImage: `url('${imageURL}')`,
          backgroundSize: `${n * 100}% ${n * 100}%`,
          backgroundPosition: `${-colInImage * 100}% ${-rowInImage * 100}%`
        });
      } else {
        $td.addClass('empty');
      }

      $row.append($td);
    }
    $puzzle.append($row);
  }
}

function swap(row1, col1, row2, col2) {
  [matrix[row1][col1], matrix[row2][col2]] = [matrix[row2][col2], matrix[row1][col1]];
}

$(document).on('keydown', function (e) {
  let newRow = emptyRow;
  let newCol = emptyCol;

  if (e.key === 'ArrowUp') newRow++;
  else if (e.key === 'ArrowDown') newRow--;
  else if (e.key === 'ArrowLeft') newCol++;
  else if (e.key === 'ArrowRight') newCol--;

  if (newRow >= 0 && newRow < n && newCol >= 0 && newCol < n) {
    swap(emptyRow, emptyCol, newRow, newCol);
    emptyRow = newRow;
    emptyCol = newCol;
    // renderMatrixImage();
    renderMatrix();
  }
});

$(document).ready(function () {
  generateMatrix();
  renderMatrix();
//   renderMatrixImage();
});