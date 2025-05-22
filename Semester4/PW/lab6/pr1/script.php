<?php
header('Content-Type: application/json');

$plecare = isset($_GET['plecare']) ? $_GET['plecare'] : '';

if ($plecare === '') {
    echo json_encode([]);
    exit;
}

try {
    $pdo = new PDO('sqlite:trenuri.db');
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    // Selectăm destinațiile în funcție de orașul de plecare
    $stmt = $pdo->prepare("SELECT sosire FROM rute WHERE plecare = ?");
    $stmt->execute([$plecare]);
    $destinatii = $stmt->fetchAll(PDO::FETCH_COLUMN);

    echo json_encode($destinatii);

} catch (PDOException $e) {
    echo json_encode(['eroare' => $e->getMessage()]);
}
?>