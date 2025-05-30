<?php
try {
    $pdo = new PDO("sqlite:produse.db");
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
} catch (PDOException $e) {
    die("Eroare la conectare: " . $e->getMessage());
}

// Preiau parametrii
$n = isset($_GET['n']) ? (int)$_GET['n'] : 5;
$page = isset($_GET['page']) ? (int)$_GET['page'] : 1;

// Validare valori
if ($n <= 0) $n = 5;
if ($page <= 0) $page = 1;

// Total produse
$total = $pdo->query("SELECT COUNT(*) FROM produse")->fetchColumn();
$totalPages = ceil($total / $n);
if ($page > $totalPages) $page = $totalPages;

// Calculez offsetul
$offset = ($page - 1) * $n;

// Pregătesc interogarea
$stmt = $pdo->prepare("SELECT * FROM produse LIMIT :limit OFFSET :offset");
$stmt->bindValue(':limit', $n, PDO::PARAM_INT);
$stmt->bindValue(':offset', $offset, PDO::PARAM_INT);
$stmt->execute();
$produse = $stmt->fetchAll(PDO::FETCH_ASSOC);
?>

<!DOCTYPE html>
<html lang="ro">
<head>
    <meta charset="UTF-8" />
    <title>Produse - Pagina <?= $page ?></title>
</head>
<body>

<h1>Produse - Pagina <?= $page ?> / <?= $totalPages ?></h1>

<table border="1" cellpadding="5" cellspacing="0">
    <tr>
        <th>ID</th><th>Nume</th><th>Descriere</th><th>Preț</th>
    </tr>
    <?php foreach ($produse as $produs): ?>
        <tr>
            <td><?= htmlspecialchars($produs['id']) ?></td>
            <td><?= htmlspecialchars($produs['nume']) ?></td>
            <td><?= htmlspecialchars($produs['descriere']) ?></td>
            <td><?= htmlspecialchars($produs['pret']) ?></td>
        </tr>
    <?php endforeach; ?>
</table>

<div style="margin-top:20px;">
    <?php if ($page > 1): ?>
        <a href="?n=<?= $n ?>&page=<?= $page - 1 ?>">&laquo; Anterior</a>
    <?php endif; ?>

    <strong>Pagina <?= $page ?> din <?= $totalPages ?></strong>

    <?php if ($page < $totalPages): ?>
        <a href="?n=<?= $n ?>&page=<?= $page + 1 ?>">Următor &raquo;</a>
    <?php endif; ?>
</div>

<br>
<a href="index.html">Înapoi la selecția numărului de produse pe pagină</a>

</body>
</html>