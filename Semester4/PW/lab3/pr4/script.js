document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll("table.sortable").forEach(makeTableSortable);
});

document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll("table.sortable2").forEach(makeVerticalTableSortable);
});

  function makeTableSortable(table) {
    const headers = table.querySelectorAll("th");
    headers.forEach((header, index) => {
      let asc = true;
      header.addEventListener("click", () => {
        const rows = Array.from(table.tBodies[0].rows);
        rows.sort((a, b) => {
          const aText = a.cells[index].textContent.trim();
          const bText = b.cells[index].textContent.trim();

          const aVal = parseFloat(aText.replace(",", ".")) || aText.toLowerCase();
          const bVal = parseFloat(bText.replace(",", ".")) || bText.toLowerCase();

          if (aVal < bVal) return asc ? -1 : 1;
          if (aVal > bVal) return asc ? 1 : -1;
          return 0;
        });

        rows.forEach(row => table.tBodies[0].appendChild(row));
        asc = !asc;
      });
    });
  }

  function makeVerticalTableSortable(table) {
    const rows = Array.from(table.rows);
    const colCount = rows[0].cells.length;
    const rowCount = rows.length;
  
    const columns = [];
    for (let colIndex = 1; colIndex < colCount; colIndex++) {
      const column = [];
      for (let rowIndex = 0; rowIndex < rowCount; rowIndex++) {
        column.push(rows[rowIndex].cells[colIndex].textContent.trim());
      }
      columns.push(column);
    }
  
    for (let rowIndex = 0; rowIndex < rowCount; rowIndex++) {
      let asc = true;
      const headerCell = rows[rowIndex].cells[0];
  
      headerCell.addEventListener("click", () => {
        columns.sort((a, b) => {
          const aText = a[rowIndex];
          const bText = b[rowIndex];
  
          const aVal = parseFloat(aText.replace(",", ".")) || aText.toLowerCase();
          const bVal = parseFloat(bText.replace(",", ".")) || bText.toLowerCase();
  
          if (aVal < bVal) return asc ? -1 : 1;
          if (aVal > bVal) return asc ? 1 : -1;
          return 0;
        });
  
        for (let colIndex = 0; colIndex < columns.length; colIndex++) {
          for (let rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            rows[rowIndex].cells[colIndex + 1].textContent = columns[colIndex][rowIndex];
          }
        }
        asc = !asc;
      });
    }
  }