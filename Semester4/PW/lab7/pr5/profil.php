<?php
require 'db.php';
if (!isset($_SESSION['user_id'])) {
    header('Location: login.php');
    exit;
}

// Poze ale tuturor utilizatorilor
$poze = $db->query("SELECT p.id, p.fisier, u.email, p.user_id FROM poze p JOIN utilizatori u ON p.user_id = u.id")->fetchAll(PDO::FETCH_ASSOC);
?>

<h1>Galerie utilizatori</h1>
<a href="upload.php">Încarcă o poză</a> | <a href="logout.php">Logout</a>
<hr>

<?php foreach ($poze as $poza): ?>
    <div>
        <p><strong><?php echo htmlspecialchars($poza['email']); ?></strong></p>
        <img src="imagini/<?php echo htmlspecialchars($poza['fisier']); ?>" width="200"><br>
        <?php if ($poza['user_id'] == $_SESSION['user_id']): ?>
            <form method="POST" action="delete.php">
                <input type="hidden" name="id" value="<?php echo $poza['id']; ?>">
                <button>Șterge</button>
            </form>
        <?php endif; ?>
    </div>
<?php endforeach; ?>
