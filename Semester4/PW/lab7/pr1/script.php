<?php
// Conectare la baza de date SQLite
try {
    $pdo = new PDO("sqlite:rute.db");
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
} catch (PDOException $e) {
    die("Conexiunea a eșuat: " . $e->getMessage());
}

// Preluare date din formular
$plecare = $_GET['plecare'] ?? '';
$sosire = $_GET['sosire'] ?? '';
$legaturi = isset($_GET['legaturi']);

// Curățare input pentru SQL (folosim prepared statements, deci e safe)
echo "<h2>Rezultate căutare:</h2>";

// Căutare curse directe
$stmt = $pdo->prepare("SELECT * FROM trenuri WHERE localitate_plecare = :plecare AND localitate_sosire = :sosire");
$stmt->execute(['plecare' => $plecare, 'sosire' => $sosire]);

$rez_directe = $stmt->fetchAll();

if (count($rez_directe) > 0) {
    echo "<h3>Curse directe:</h3>";
    foreach ($rez_directe as $row) {
        echo "<p>{$row['nr_tren']} | {$row['tip_tren']} | {$row['ora_plecare']} - {$row['ora_sosire']}</p>";
    }
} else {
    echo "<p>Nu există curse directe între aceste localități.</p>";
}

// Dacă e bifat legături, căutăm curse cu o legătură
if ($legaturi) {
    $stmt = $pdo->prepare("
        SELECT t1.nr_tren AS tren1, t2.nr_tren AS tren2, 
               t1.localitate_plecare AS plecare, t1.localitate_sosire AS legatura, t2.localitate_sosire AS sosire,
               t1.ora_plecare AS ora_plecare1, t1.ora_sosire AS ora_sosire1, 
               t2.ora_plecare AS ora_plecare2, t2.ora_sosire AS ora_sosire2
        FROM trenuri t1
        JOIN trenuri t2 ON t1.localitate_sosire = t2.localitate_plecare
        WHERE t1.localitate_plecare = :plecare AND t2.localitate_sosire = :sosire
    ");

    $stmt->execute(['plecare' => $plecare, 'sosire' => $sosire]);
    $rez_legaturi = $stmt->fetchAll();

    if (count($rez_legaturi) > 0) {
        echo "<h3>Curse cu legături:</h3>";
        foreach ($rez_legaturi as $row) {
            echo "<p>{$row['tren1']} ({$row['ora_plecare1']}-{$row['ora_sosire1']}) -> {$row['legatura']} -> {$row['tren2']} ({$row['ora_plecare2']}-{$row['ora_sosire2']})</p>";
        }
    } else {
        echo "<p>Nu există curse cu legături între aceste localități.</p>";
    }
}
?>