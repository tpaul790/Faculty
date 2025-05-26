imageURL = "sprite.jpg"
const n = 4; 
  const puzzle = document.getElementById('puzzle');
  let matrix = [];
  let emptyRow, emptyCol;

  function generateMatrix() {
    nums = []
    for(let i = 0; i < n**2; i++)
        nums.push(i);
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
    puzzle.innerHTML = '';
    for (let i = 0; i < n; i++) {
      const row = document.createElement('tr');
      for (let j = 0; j < n; j++) {
        const cell = document.createElement('td');
        const value = matrix[i][j];
        if (value !== 0) {
          cell.textContent = value;
        } else {
          cell.classList.add('empty');
        }
        row.appendChild(cell);
      }
      puzzle.appendChild(row);
    }
  }

  function renderMatrixImage() {
    puzzle.innerHTML = '';
    for (let i = 0; i < n; i++) {
      const row = document.createElement('tr');
      for (let j = 0; j < n; j++) {
        const td = document.createElement('td');
        const index = matrix[i][j];

        if (index !== 0) {
          const rowInImage = Math.floor(index / n);
          const colInImage = index % n;
          td.style.backgroundImage = `url('${imageURL}')`;
          td.style.backgroundSize = `${n * 100}% ${n * 100}%`;
          td.style.backgroundPosition = `${-colInImage * 100}% ${-rowInImage * 100}%`;
        } else {
          td.classList.add('empty');
        }
        row.appendChild(td);
      }
      puzzle.appendChild(row);
    }
  }

  function swap(row1, col1, row2, col2) {
    [matrix[row1][col1],matrix[row2][col2]] = [matrix[row2][col2],matrix[row1][col1]];
  }

  document.addEventListener('keydown', (e) => {
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
      // renderMatrix();
      renderMatrixImage();
    }
  });

  generateMatrix();
  // renderMatrix();
  renderMatrixImage();

