package ppd.data;

import ppd.config.WorkshopConfig;
import ppd.dto.Reservation;
import ppd.dto.Transaction;
import ppd.dto.ReservationStatus;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Database {
    private static final String URL = "jdbc:postgresql://localhost:5432/workshop_db";
    private static final String USER = "admin";
    private static final String PASS = "admin";

    private static final ReentrantLock[][] workshopLocks =
            new ReentrantLock[WorkshopConfig.NUM_CENTRES + 1][WorkshopConfig.NUM_WORKSHOPS + 1];

    static {
        for (int center = 1; center <= WorkshopConfig.NUM_CENTRES; center++) {
            for (int workshop = 1; workshop <= WorkshopConfig.NUM_WORKSHOPS; workshop++) {
                workshopLocks[center][workshop] = new ReentrantLock(true); //Fair lock
            }
        }
    }

    public static ReentrantLock getLock(int centerId, int workshopId) {
        return workshopLocks[centerId][workshopId];
    }

    public static void lockAll() {
        for (int c = 1; c <= WorkshopConfig.NUM_CENTRES; c++) {
            for (int w = 1; w <= WorkshopConfig.NUM_WORKSHOPS; w++) {
                workshopLocks[c][w].lock();
            }
        }
    }

    public static void unlockAll() {
        for (int c = WorkshopConfig.NUM_CENTRES; c >= 1; c--) {
            for (int w = WorkshopConfig.NUM_WORKSHOPS; w >= 1; w--) {
                workshopLocks[c][w].unlock();
            }
        }
    }


    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }


    public static void insertReservation(Reservation res) {
        String sql = "INSERT INTO reservations (id, client_name, cnp, center_id, workshop_id, hour, status, cost, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, res.getId());
            pstmt.setString(2, res.getClientName());
            pstmt.setString(3, res.getCnp());
            pstmt.setInt(4, res.getCenterId());
            pstmt.setInt(5, res.getWorkshopId());
            pstmt.setInt(6, res.getHour());
            pstmt.setString(7, res.getStatus().name());
            pstmt.setDouble(8, res.getCost());
            pstmt.setTimestamp(9, Timestamp.valueOf(res.getCreationTimestamp()));

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static Reservation getReservationById(String id) {
        String sql = "SELECT * FROM reservations WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Reservation res = new Reservation(
                        rs.getString("client_name"),
                        rs.getString("cnp"),
                        rs.getInt("center_id"),
                        rs.getInt("workshop_id"),
                        rs.getInt("hour"),
                        rs.getDouble("cost")
                );
                res.setId(rs.getString("id"));
                res.setStatus(ReservationStatus.valueOf(rs.getString("status")));
                res.setCreationTimestamp(rs.getTimestamp("created_at").toLocalDateTime());
                return res;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void updateReservationStatus(String id, ReservationStatus newStatus) {
        String sql = "UPDATE reservations SET status = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newStatus.name());
            pstmt.setString(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static long countActiveReservations(int centerId, int workshopId, int targetHour) {
        int durationHours = WorkshopConfig.getDurationMinutes(workshopId) / 60;

        String sql = "SELECT COUNT(*) FROM reservations " +
                "WHERE center_id = ? AND workshop_id = ? " +
                "AND (status = 'PENDING' OR status = 'PAID') " +
                "AND hour > ? AND hour <= ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, centerId);
            pstmt.setInt(2, workshopId);
            pstmt.setInt(3, targetHour - durationHours);
            pstmt.setInt(4, targetHour);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static List<Reservation> getAllReservations() {
        List<Reservation> list = new ArrayList<>();
        String sql = "SELECT * FROM reservations";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Reservation res = new Reservation(
                        rs.getString("client_name"),
                        rs.getString("cnp"),
                        rs.getInt("center_id"),
                        rs.getInt("workshop_id"),
                        rs.getInt("hour"),
                        rs.getDouble("cost")
                );
                res.setId(rs.getString("id"));
                res.setStatus(ReservationStatus.valueOf(rs.getString("status")));
                res.setCreationTimestamp(rs.getTimestamp("created_at").toLocalDateTime());
                list.add(res);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    public static void insertTransaction(Transaction tx) {
        String sql = "INSERT INTO transactions (id, reservation_id, type, amount, created_at) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, tx.getId());
            pstmt.setString(2, tx.getReservationId());
            pstmt.setString(3, String.valueOf(tx.getType()));
            pstmt.setDouble(4, tx.getAmount());
            pstmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static double getTotalTransactionAmountForCenter(int centerId) {
        String sql = "SELECT SUM(t.amount) FROM transactions t " +
                "JOIN reservations r ON t.reservation_id = r.id " +
                "WHERE r.center_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, centerId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public static void cleanUpDatabase(){
        System.out.println("Cleaning up database...");
        String sql1 = "DELETE FROM transactions";
        try(Connection conn = getConnection();
           PreparedStatement pstmt = conn.prepareStatement(sql1)){
            pstmt.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        String sql2 = "DELETE FROM reservations";
        try(Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql2)){
            pstmt.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
}