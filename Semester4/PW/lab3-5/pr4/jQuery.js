$(function () {
    $("table.sortable").each(function () {
      makeTableSortable($(this));
    });
    $("table.sortable2").each(function () {
      makeVerticalTableSortable($(this));
    });
  });

  function makeTableSortable($table) {
    const $headers = $table.find("th");
    $headers.each(function (index) {
      let asc = true;
      $(this).on("click", function () {
        const $rows = $table.find("tbody tr").toArray();

        $rows.sort(function (a, b) {
          let aText = $(a).find("td").eq(index).text().trim();
          let bText = $(b).find("td").eq(index).text().trim();

          let aVal = parseFloat(aText.replace(",", ".")) || aText.toLowerCase();
          let bVal = parseFloat(bText.replace(",", ".")) || bText.toLowerCase();

          if (aVal < bVal) return asc ? -1 : 1;
          if (aVal > bVal) return asc ? 1 : -1;
          return 0;
        });

        $.each($rows, function (_, row) {
          $table.children("tbody").append(row);
        });
        asc = !asc;
      });
    });
  }

  function makeVerticalTableSortable($table) {
    const $rows = $table.find("tr").toArray();
    const colCount = $rows[0].cells.length;
    const rowCount = $rows.length;

    // Construim coloanele (fără prima coloană)
    let columns = [];
    for (let colIndex = 1; colIndex < colCount; colIndex++) {
      let column = [];
      for (let rowIndex = 0; rowIndex < rowCount; rowIndex++) {
        column.push($rows[rowIndex].cells[colIndex].textContent.trim());
      }
      columns.push(column);
    }

    for (let rowIndex = 0; rowIndex < rowCount; rowIndex++) {
      let asc = true;
      let $headerCell = $($rows[rowIndex].cells[0]);

      $headerCell.on("click", function () {
        columns.sort(function (a, b) {
          let aText = a[rowIndex];
          let bText = b[rowIndex];

          let aVal = parseFloat(aText.replace(",", ".")) || aText.toLowerCase();
          let bVal = parseFloat(bText.replace(",", ".")) || bText.toLowerCase();

          if (aVal < bVal) return asc ? -1 : 1;
          if (aVal > bVal) return asc ? 1 : -1;
          return 0;
        });

        for (let colIndex = 0; colIndex < columns.length; colIndex++) {
          for (let r = 0; r < rowCount; r++) {
            $rows[r].cells[colIndex + 1].textContent = columns[colIndex][r];
          }
        }
        asc = !asc;
      });
    }
  }