<?php
header('Content-Type: application/json');
$pdo = new PDO("sqlite:persoane.db");

if ($_SERVER['REQUEST_METHOD'] === 'GET') {
    if ($_GET['action'] === 'get_ids') {
        $stmt = $pdo->query("SELECT id FROM persoane");
        echo json_encode($stmt->fetchAll(PDO::FETCH_COLUMN));
    } elseif ($_GET['action'] === 'get_data' && isset($_GET['id'])) {
        $stmt = $pdo->prepare("SELECT nume, prenume, telefon FROM persoane WHERE id = ?");
        $stmt->execute([$_GET['id']]);
        echo json_encode($stmt->fetch(PDO::FETCH_ASSOC));
    }
} elseif ($_SERVER['REQUEST_METHOD'] === 'POST' && $_POST['action'] === 'save') {
    $stmt = $pdo->prepare("UPDATE persoane SET nume = ?, prenume = ?, telefon = ? WHERE id = ?");
    $stmt->execute([
        $_POST['nume'],
        $_POST['prenume'],
        $_POST['telefon'],
        $_POST['id']
    ]);
    echo json_encode(['status' => 'ok']);
}