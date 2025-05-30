<?php
require 'db.php';
$mesaj = '';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $email = $_POST['email'];
    $parola = $_POST['parola'];
    $stmt = $db->prepare('SELECT * FROM utilizatori WHERE email = ?');
    $stmt->execute([$email]);
    $user = $stmt->fetch();
    if ($user && password_verify($parola, $user['parola'])) {
        $_SESSION['user_id'] = $user['id'];
        header('Location: profil.php');
        exit;
    } else {
        $mesaj = "Date invalide.";
    }
}
?>
<!DOCTYPE html>
<html lang="ro">
<head>
    <meta charset="UTF-8">
    <title>Înregistrare</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <h1>LogIn</h1>
    <form method="POST">
        <input name="email" placeholder="Email"><br>
        <input type="password" name="parola" placeholder="Parolă"><br>
        <button>Autentificare</button>
    </form> 
    <p>Nu ai cont? <a href='register.php'><button>Register</button></a></p>
    <p><?php echo $mesaj; ?></p>
</body>
