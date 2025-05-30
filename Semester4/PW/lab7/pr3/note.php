<?php
session_start();
require 'db.php';

if (!isset($_SESSION['profesor_id'])) {
    header('Location: login.php');
    exit;
}

$profesor_id = $_SESSION['profesor_id'];
$mesaj = '';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $student_id = $_POST['student_id'] ?? null;
    $materie_id = $_POST['materie_id'] ?? null;
    $nota = $_POST['nota'] ?? null;

    if ($student_id && $materie_id && $nota !== null && is_numeric($nota) && $nota >= 1 && $nota <= 10) {
        // Inseram nota (sau actualizam daca exista deja pentru acel student, materie, profesor)
        // Pentru simplitate, inseram fara update
        $stmt = $db->prepare('INSERT INTO note (student_id, materie_id, profesor_id, nota) VALUES (?, ?, ?, ?)');
        $stmt->execute([$student_id, $materie_id, $profesor_id, $nota]);
        $mesaj = 'Nota a fost inregistrata.';
    } else {
        $mesaj = 'Date invalide.';
    }
}

// Preluam lista de studenti si materii
$studenti = $db->query('SELECT * FROM studenti')->fetchAll(PDO::FETCH_ASSOC);
$materii = $db->query('SELECT * FROM materii')->fetchAll(PDO::FETCH_ASSOC);

?>

<!DOCTYPE html>
<html lang="ro">
<head>
    <meta charset="UTF-8" />
    <title>Completare Note</title>
    <link rel="stylesheet" href="style.css" />
</head>
<body>
    <h1>Bine ai venit, <?=htmlspecialchars($_SESSION['profesor_username'])?></h1>
    <a href="logout.php">Deconectare</a>
    
    <?php if ($mesaj): ?>
        <p><?=htmlspecialchars($mesaj)?></p>
    <?php endif; ?>

    <form method="POST" action="note.php">
        <label>Student:
            <select name="student_id" required>
                <option value="">Selectează student</option>
                <?php foreach ($studenti as $student): ?>
                    <option value="<?= $student['id'] ?>">
                        <?= htmlspecialchars($student['nume'] . ' ' . $student['prenume']) ?>
                    </option>
                <?php endforeach; ?>
            </select>
        </label><br/>

        <label>Materie:
            <select name="materie_id" required>
                <option value="">Selectează materie</option>
                <?php foreach ($materii as $materie): ?>
                    <option value="<?= $materie['id'] ?>">
                        <?= htmlspecialchars($materie['denumire']) ?>
                    </option>
                <?php endforeach; ?>
            </select>
        </label><br/>

        <label>Nota (1-10): <input type="number" name="nota" min="1" max="10" required></label><br/>
        <button type="submit">Adaugă Nota</button>
    </form>
</body>
</html>
