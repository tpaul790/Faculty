<?php
require 'db.php';
if (!isset($_SESSION['user_id'])) {
    header('Location: login.php');
    exit;
}

$mesaj = '';

if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_FILES['poza'])) {
    $nume = uniqid() . '_' . $_FILES['poza']['name'];
    if (move_uploaded_file($_FILES['poza']['tmp_name'], 'imagini/' . $nume)) {
        $stmt = $db->prepare('INSERT INTO poze (user_id, fisier) VALUES (?, ?)');
        $stmt->execute([$_SESSION['user_id'], $nume]);
        header('Location: profil.php');
        exit;
    } else {
        $mesaj = "Eroare la upload.";
    }
}
?>

<h1>Upload poză</h1>
<form method="POST" enctype="multipart/form-data">
    <input type="file" name="poza" required><br>
    <button>Încarcă</button>
</form>
<p><?php echo $mesaj; ?></p>
