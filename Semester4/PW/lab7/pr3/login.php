<?php
session_start();
require 'db.php';

$error = '';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $username = $_POST['username'] ?? '';
    $parola = $_POST['parola'] ?? '';

    $stmt = $db->prepare('SELECT * FROM profesori WHERE username = ? AND parola = ?');
    $stmt->execute([$username, $parola]);
    $profesor = $stmt->fetch(PDO::FETCH_ASSOC);

    if ($profesor) {
        $_SESSION['profesor_id'] = $profesor['id'];
        $_SESSION['profesor_username'] = $profesor['username'];
        header('Location: note.php');
        exit;
    } else {
        $error = 'Date de autentificare invalide.';
    }
}
?>

<!DOCTYPE html>
<html lang="ro">
<head>
    <meta charset="UTF-8" />
    <title>Autentificare Profesor</title>
    <link rel="stylesheet" href="style.css" />
</head>
<body>
    <h1>Autentificare Profesor</h1>
    <?php if ($error): ?>
        <p class="error"><?= htmlspecialchars($error) ?></p>
    <?php endif; ?>
    <form method="POST" action="login.php">
        <label>Username: <input type="text" name="username" required></label><br/>
        <label>Parola: <input type="password" name="parola" required></label><br/>
        <button type="submit">Autentificare</button>
    </form>
</body>
</html>
