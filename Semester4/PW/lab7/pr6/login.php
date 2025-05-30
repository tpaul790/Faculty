<?php
require 'db.php';
session_start();

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $user = $_POST['username'] ?? '';
    $pass = $_POST['parola'] ?? '';

    $stmt = $db->prepare("SELECT * FROM admin WHERE username = ?");
    $stmt->execute([$user]);
    $admin = $stmt->fetch();

    if ($admin && $admin['parola'] == $pass) {
        $_SESSION['admin'] = true;
        header('Location: modereaza.php');
        exit;
    } else {
        $mesaj = "Date greșite!";
    }
}
?>

<!DOCTYPE html>
<html lang="ro">
<head>
    <meta charset="UTF-8">
    <title>Login Admin</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <h1>Login Admin</h1>
    <form method="POST">
        <input type="text" name="username" placeholder="Username" required><br>
        <input type="password" name="parola" placeholder="Parola" required><br>
        <button>Autentificare</button>
    </form>
    <p><?= $mesaj ?? '' ?></p>
</body>
</html>
