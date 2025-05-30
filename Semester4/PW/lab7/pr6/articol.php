<?php
require 'db.php';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $nume = trim($_POST['nume'] ?? '');
    $comentariu = trim($_POST['comentariu'] ?? '');

    if ($nume && $comentariu) {
        $stmt = $db->prepare("INSERT INTO comentarii (nume, text) VALUES (?, ?)");
        $stmt->execute([$nume, $comentariu]);
        $mesaj = "Comentariul tău a fost trimis și așteaptă aprobarea.";
    } else {
        $mesaj = "Completează toate câmpurile!";
    }
}
?>

<!DOCTYPE html>
<html lang="ro">
<head>
    <meta charset="UTF-8">
    <title>Articol Sportiv</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <h1>Super Articol Sportiv</h1>
    <p>Acesta este un articol foarte interesant despre sport...</p>

    <h2>Lasă un comentariu:</h2>
    <form method="POST">
        <input type="text" name="nume" placeholder="Numele tău" required><br>
        <textarea name="comentariu" placeholder="Comentariul tău" required></textarea><br>
        <button>Trimite</button>
    </form>
    <p><?= $mesaj ?? '' ?></p>

    <h2>Comentarii:</h2>
    <?php
    $comentarii = $db->query("SELECT * FROM comentarii WHERE aprobat = 1 ORDER BY id DESC");
    foreach ($comentarii as $c) {
        echo "<p><strong>" . htmlspecialchars($c['nume']) . ":</strong> " . htmlspecialchars($c['text']) . "</p>";
    }
    ?>
</body>
</html>
