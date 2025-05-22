<?php
header('Content-Type: application/json');

$limit = 3;
$offset = isset($_GET['offset']) ? (int)$_GET['offset'] : 0;

try {
    $pdo = new PDO("sqlite:contacte.db");
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    // Preluăm totalul pentru paginare
    $totalQuery = $pdo->query("SELECT COUNT(*) FROM persoane");
    $total = $totalQuery->fetchColumn();

    // Preluăm înregistrările paginării curente
    $stmt = $pdo->prepare("SELECT nume, prenume, telefon, email FROM persoane LIMIT ? OFFSET ?");
    $stmt->bindValue(1, $limit, PDO::PARAM_INT);
    $stmt->bindValue(2, $offset, PDO::PARAM_INT);
    $stmt->execute();
    $rows = $stmt->fetchAll(PDO::FETCH_ASSOC);

    echo json_encode([
        'contacte' => $rows,
        'offset' => $offset,
        'total' => $total
    ]);

} catch (PDOException $e) {
    echo json_encode(['eroare' => $e->getMessage()]);
}