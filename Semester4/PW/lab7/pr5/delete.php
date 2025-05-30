<?php
require 'db.php';
if (!isset($_SESSION['user_id'])) {
    header('Location: login.php');
    exit;
}

if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['id'])) {
    $id = $_POST['id'];
    $stmt = $db->prepare('SELECT * FROM poze WHERE id = ? AND user_id = ?');
    $stmt->execute([$id, $_SESSION['user_id']]);
    $poza = $stmt->fetch();
    if ($poza) {
        unlink('imagini/' . $poza['fisier']);
        $db->prepare('DELETE FROM poze WHERE id = ?')->execute([$id]);
    }
}
header('Location: profil.php');
