<?php
require 'db.php';
session_start();
if (!isset($_SESSION['admin'])) {
    header('Location: login.php');
    exit;
}

if (isset($_GET['aproba'])) {
    $id = (int)$_GET['aproba'];
    $db->prepare("UPDATE comentarii SET aprobat = 1 WHERE id = ?")->execute([$id]);
}

if (isset($_GET['sterge'])) {
    $id = (int)$_GET['sterge'];
    $db->prepare("DELETE FROM comentarii WHERE id = ?")->execute([$id]);
}

$comentarii = $db->query("SELECT * FROM comentarii WHERE aprobat = 0")->fetchAll();
?>

<!DOCTYPE html>
<html lang="ro">
<head>
    <meta charset="UTF-8">
    <title>Moderează Comentarii</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <h1>Comentarii de moderat</h1>
    <p><a href="logout.php">Logout</a></p>

    <?php foreach ($comentarii as $c): ?>
        <div>
            <p><strong><?= htmlspecialchars($c['nume']) ?>:</strong> <?= htmlspecialchars($c['text']) ?></p>
            <button><a href="?aproba=<?= $c['id'] ?>">Aprobă</a></button>
            <button><a href="?sterge=<?= $c['id'] ?>">Șterge</a></button>
        </div>
    <?php endforeach; ?>
</body>
</html>
