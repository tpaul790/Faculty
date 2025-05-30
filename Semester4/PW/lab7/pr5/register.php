<?php
require 'db.php';
$mesaj = '';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $email = $_POST['email'] ?? '';
    $parola = $_POST['parola'] ?? '';

    if (filter_var($email, FILTER_VALIDATE_EMAIL) && strlen($parola) >= 1) {
        $hash = password_hash($parola, PASSWORD_DEFAULT);
        $stmt = $db->prepare('INSERT INTO utilizatori (email, parola) VALUES (?, ?)');
        try {
            $stmt->execute([$email, $hash]);
            $mesaj = "Cont creat cu succes";
        } catch (PDOException $e) {
            $mesaj = "Email deja folosit.";
        }
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
    <h1>Registration</h1>
    <form method="POST">
        <input name="email" placeholder="Email"><br>
        <input type="password" name="parola" placeholder="Parolă"><br>
        <button>Înregistrează-te</button>
    </form>
    <p>Ai deja cont? <a href='login.php'><button>LogIn</button></a></p>
    <p><?php echo $mesaj; ?></p>
</body>