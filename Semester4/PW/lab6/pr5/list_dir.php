<?php
header('Content-Type: application/json');

$base = realpath(__DIR__ . '/root'); // doar acest director poate fi accesat

// Vizualizare fișier
if (isset($_GET['file'])) {
    $file = realpath($base . '/' . $_GET['file']);
    if (strpos($file, $base) !== 0 || !is_file($file)) {
        http_response_code(403);
        exit("Acces interzis.");
    }
    header("Content-Type: text/plain");
    echo file_get_contents($file);
    exit;
}

// Listare director
$path = isset($_GET['path']) ? $_GET['path'] : '';
$dir = realpath($path === 'root' ? $base : $base . '/' . $path);
if (strpos($dir, $base) !== 0 || !is_dir($dir)) {
    http_response_code(403);
    echo json_encode([]);
    exit;
}

$files = scandir($dir);
$result = [];

foreach ($files as $file) {
    if ($file === '.' || $file === '..') continue;

    $fullPath = $dir . '/' . $file;
    $relPath = substr($fullPath, strlen($base) + 1);

    $result[] = [
        "name" => $file,
        "type" => is_dir($fullPath) ? "directory" : "file",
        "path" => $relPath
    ];
}

echo json_encode($result);