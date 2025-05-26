<?php
header('Content-Type: application/json');

$db = new PDO("sqlite:notebooks.db");
$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

$conditions = [];
$params = [];

foreach (['producator', 'procesor', 'memorie', 'hdd', 'placa_video'] as $field) {
    if (!empty($_GET[$field])) {
        $conditions[] = "$field = :$field";
        $params[":$field"] = $_GET[$field];
    }
}

$sql = "SELECT * FROM notebooks";
if ($conditions) {
    $sql .= " WHERE " . implode(" AND ", $conditions);
}

$stmt = $db->prepare($sql);
foreach ($params as $key => $val) {
    $stmt->bindValue($key, $val);
}
$stmt->execute();
$results = $stmt->fetchAll(PDO::FETCH_ASSOC);

echo json_encode($results);