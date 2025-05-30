const $treeRoot = $("#tree");
const $fileContent = $("#fileContent");

function loadDirectory(path, $element) {
  $.ajax({
    url: "list_dir.php",
    type: "GET",
    data: { path: path },
    dataType: "json",
    success: function (items) {
      const $ul = $("<ul></ul>");

      items.forEach(function (item) {
        const $li = $("<li></li>")
          .text(item.name)
          .addClass(item.type);

        if (item.type === "directory") {
          $li.on("click", function (e) {
            e.stopPropagation();
            if ($li.data("loaded") !== true) {
              loadDirectory(item.path, $li);
              $li.data("loaded", true);
            } else {
              $li.find("> ul").remove();
              $li.data("loaded", false);
            }
          });
        } else {
          $li.on("click", function (e) {
            e.stopPropagation();
            loadFileContent(item.path);
          });
        }

        $ul.append($li);
      });

      $element.append($ul);
    },
    error: function () {
      alert("Eroare la încărcarea directorului.");
    }
  });
}

function loadFileContent(filePath) {
  $.ajax({
    url: "list_dir.php",
    type: "GET",
    data: { file: filePath },
    success: function (data) {
      $fileContent.text(data);
    },
    error: function () {
      $fileContent.text("Eroare la încărcarea fișierului.");
    }
  });
}

// Load root directory on page load
loadDirectory("root", $treeRoot);