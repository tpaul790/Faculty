package ppd;

import java.sql.*;
import java.util.Random;

public class DatabaseSetup {
    public static void initDB() {
        try (Connection conn = DriverManager.getConnection(Main.DB_URL, Main.DB_USER, Main.DB_PASS);
             Statement stmt = conn.createStatement()) {

            Random rand = new Random();
            for (int i = 1; i <= Main.NUM_PROJECTS; i++) {
                String table = "proiect_" + i;
                stmt.execute("DROP TABLE IF EXISTS " + table);
                stmt.execute("CREATE TABLE " + table + " (id_student INT, nota INT)");

                PreparedStatement ps = conn.prepareStatement("INSERT INTO " + table + " VALUES (?, ?)");
                int studCount = 80 + rand.nextInt(120);
                for (int j = 0; j < studCount; j++) {
                    int id = rand.nextInt(500);
                    int nota = rand.nextInt(11);

                    if (rand.nextInt(100) < 5) nota = -1;

                    ps.setInt(1, id);
                    ps.setInt(2, nota);
                    ps.addBatch();
                }
                ps.executeBatch();
            }
            System.out.println("Database tables created & populated.");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Eroare la setup baza de date (ai driverul JDBC?)");
        }
    }
}
