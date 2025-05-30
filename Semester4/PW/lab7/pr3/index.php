<?php
require 'db.php';

// Afisam notele grupate pe student si materie
$rezultat = $db->query('
    SELECT s.nume, s.prenume, m.denumire, n.nota
    FROM note n
    JOIN studenti s ON n.student_id = s.id
    JOIN materii m ON n.materie_id = m.id
    ORDER BY s.nume, m.denumire
')->fetchAll(PDO::FETCH_ASSOC);
?>

<!DOCTYPE html>
<html lang="ro">
<head>
    <meta charset="UTF-8" />
    <title>Note Studenti</title>
    <link rel="stylesheet" href="style.css" />
</head>
<body>
    <h1>Note Studenti</h1>

    <table border="1" cellpadding="5" cellspacing="0">
        <thead>
            <tr>
                <th>Student</th>
                <th>Materie</th>
                <th>Nota</th>
            </tr>
        </thead>
        <tbody>
            <?php foreach ($rezultat as $row): ?>
                <tr>
                    <td><?=htmlspecialchars($row['nume'] . ' ' . $row['prenume'])?></td>
                    <td><?=htmlspecialchars($row['denumire'])?></td>
                    <td><?=htmlspecialchars($row['nota'])?></td>
                </tr>
            <?php endforeach; ?>
        </tbody>
    </table>
</body>
</html>
