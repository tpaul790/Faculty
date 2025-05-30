<?php
require 'db.php';
$mesaj = '';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $email = $_POST['email'] ?? '';
    $parola = $_POST['parola'] ?? '';

    if (filter_var($email, FILTER_VALIDATE_EMAIL) && strlen($parola) >= 1) {
        $hash = password_hash($parola, PASSWORD_DEFAULT);
        $cod = bin2hex(random_bytes(16));

        try {
            $stmt = $db->prepare('INSERT INTO utilizatori (email, parola, cod_activare) VALUES (?, ?, ?)');
            $stmt->execute([$email, $hash, $cod]);

            $link = "http://localhost:8000/confirm.php?email=" . urlencode($email) . "&cod=$cod"; // Schimbă cu domeniul tău dacă e nevoie

            // Trimitere email
            // $subiect = "Activare cont";
            // $mesaj_email = "Salut! Te-ai înregistrat pe site-ul nostru.\nAccesează linkul pentru activare:\n$link";
            // $headers = "From: no-reply@siteul-tau.ro\r\n";

            // if (mail($email, $subiect, $mesaj_email, $headers)) {
            //     $mesaj = "Email trimis! Verifică inbox-ul și activează contul.";
            // } else {
            //     $mesaj = "Eroare la trimiterea emailului.";
            // }

            $mesaj = "Cont creat! Accesează <a href=\"$link\">acest link</a> pentru activare.";

        } catch (PDOException $e) {
            $mesaj = "Eroare: " . $e->getMessage();
        }
    } else {
        $mesaj = "Email invalid sau parolă prea scurtă.";
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
    <h1>Înregistrare</h1>
    <form method="POST">
        <label>Email: <input type="email" name="email" required></label><br>
        <label>Parola: <input type="password" name="parola" required></label><br>
        <button>Înregistrează-te</button>
    </form>
    <p>Ai deja cont? <a href="login.php"><button>Autentifică-te</button></a></p>
    <p><?= $mesaj ?></p>
</body>
</html>