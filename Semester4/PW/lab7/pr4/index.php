<?php
session_start();
if (!isset($_SESSION['user'])) {
    header('Location: login.php');
    exit;
}
?>

<!DOCTYPE html>
<html lang="ro">
<head>
    <meta charset="UTF-8">
    <title>Dashboard</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <h1>Bine ai venit, <?= htmlspecialchars($_SESSION['user']) ?>!</h1>
    <a href="logout.php">Deconectare</a>
</body>
</html>