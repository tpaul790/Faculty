<?php
session_start();
require 'db.php';
$mesaj = '';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $email = $_POST['email'] ?? '';
    $parola = $_POST['parola'] ?? '';

    $stmt = $db->prepare('SELECT * FROM utilizatori WHERE email = ? AND activat = 1');
    $stmt->execute([$email]);
    $user = $stmt->fetch(PDO::FETCH_ASSOC);

    if ($user && password_verify($parola, $user['parola'])) {
        $_SESSION['user'] = $user['email'];
        header('Location: index.php');
        exit;
    } else {
        $mesaj = "Date greșite sau cont neactivat.";
    }
}
?>

<!DOCTYPE html>
<html lang="ro">
<head>
    <meta charset="UTF-8">
    <title>Autentificare</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <h1>Autentificare</h1>
    <form method="POST">
        <label>Email: <input type="email" name="email" required></label><br>
        <label>Parola: <input type="password" name="parola" required></label><br>
        <button>Login</button>
    </form>
    <p>Nu ai cont? <a href="register.php"><button>Înregistrează-te</button></a></p>
    <p><?= $mesaj ?></p>
</body>
</html>