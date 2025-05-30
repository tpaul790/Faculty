<?php
// Conexiune la baza SQLite
$db = new PDO('sqlite:note.db');
$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
?>