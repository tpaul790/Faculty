<?php
require 'db.php';

$email = $_GET['email'] ?? '';
$cod = $_GET['cod'] ?? '';
$mesaj = '';

if ($email && $cod) {
    $stmt = $db->prepare('SELECT * FROM utilizatori WHERE email = ? AND cod_activare = ? AND activat = 0');
    $stmt->execute([$email, $cod]);
    $user = $stmt->fetch(PDO::FETCH_ASSOC);

    if ($user) {
        $update = $db->prepare('UPDATE utilizatori SET activat = 1 WHERE id = ?');
        $update->execute([$user['id']]);
        $mesaj = "Cont activat! Poți <a href='login.php'>intra în cont</a>.";
    } else {
        $mesaj = "Link invalid sau cont deja activat.";
    }
} else {
    $mesaj = "Date lipsă.";
}
?>

<!DOCTYPE html>
<html lang="ro">
<head>
    <meta charset="UTF-8">
    <title>Confirmare cont</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <h1>Confirmare</h1>
    <p><?= $mesaj ?></p>
</body>
</html>