const treeRoot = document.getElementById("tree");
const fileContent = document.getElementById("fileContent");

function loadDirectory(path, element) {
  const xhr = new XMLHttpRequest();
  xhr.open("GET", `list_dir.php?path=${encodeURIComponent(path)}`, true);
  xhr.onreadystatechange = function () {
    if (xhr.status === 200 && xhr.readyState === 4) {
      const items = JSON.parse(xhr.responseText);
      const ul = document.createElement("ul");

      items.forEach(item => {
        const li = document.createElement("li");
        li.textContent = item.name;
        li.className = item.type;

        if (item.type === "directory") {
          li.addEventListener("click", function (e) {
            e.stopPropagation();
            if (li.dataset.loaded !== "true") {
              loadDirectory(item.path, li);
              li.dataset.loaded = "true";
            } else {
              li.querySelector("ul")?.remove();
              li.dataset.loaded = "";
            }
          });
        } else {
          li.addEventListener("click", function (e) {
            e.stopPropagation();
            loadFileContent(item.path);
          });
        }

        ul.appendChild(li);
      });

      element.appendChild(ul);
    }
  };
  xhr.send();
}

function loadFileContent(filePath) {
  const xhr = new XMLHttpRequest();
  xhr.open("GET", `list_dir.php?file=${encodeURIComponent(filePath)}`, true);
  xhr.onload = function () {
    if (xhr.status === 200) {
      fileContent.textContent = xhr.responseText;
    } else {
      fileContent.textContent = "Eroare la încărcarea fișierului.";
    }
  };
  xhr.send();
}

// Load root directory on page load
loadDirectory("root", treeRoot);