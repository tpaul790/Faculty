package ppd.utils;

import ppd.config.TestScenarioConfig;
import ppd.config.WorkshopConfig;
import ppd.data.Database;
import ppd.dto.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class PeriodicChecker implements Runnable {

    @Override
    public void run() {
        Database.lockAll();

        StringBuilder report = new StringBuilder();
        try {
            LocalDateTime now = LocalDateTime.now();
            report.append("\n--- Verification Report [").append(now).append("] ---\n");

            List<Reservation> all = Database.getAllReservations();

            // Check if some reservations expired
            for (Reservation r : all) {
                if (r.getStatus() == ReservationStatus.PENDING) {
                    if (Duration.between(r.getCreationTimestamp(), now).getSeconds() > TestScenarioConfig.PAYMENT_TIMEOUT_SEC) {
                        Database.updateReservationStatus(r.getId(), ReservationStatus.EXPIRED);
                        String msg = "EXPIRED: Reservation " + r.getId();
                        report.append(msg).append("\n");
                    }
                }
            }

            // Check Sums (Consistency)
            for (int c = 1; c <= WorkshopConfig.NUM_CENTRES; c++) {
                int finalC = c;

                double expected = all.stream()
                        .filter(r -> r.getCenterId() == finalC && r.getStatus() == ReservationStatus.PAID)
                        .mapToDouble(Reservation::getCost)
                        .sum();

                double actual = Database.getTotalTransactionAmountForCenter(c);

                if (Math.abs(expected - actual) > 0) {
                    String err = String.format("CORRUPTION Center %d: Expected %.2f, Found %.2f", c, expected, actual);
                    report.append(err).append("\n");
                    System.err.println(err);
                } else {
                    String msg = String.format("Center %d: Balance OK (%.2f)", c, actual);
                    report.append(msg).append("\n");
                }
            }

            // Check Overlaps
            boolean overlapFound = false;
            for (int c = 1; c <= WorkshopConfig.NUM_CENTRES; c++) {
                for (int w = 1; w <= WorkshopConfig.NUM_WORKSHOPS; w++) {
                    for (int h = 9; h < 17; h++) {
                        long activeCount = Database.countActiveReservations(c, w, h);
                        int capacity = WorkshopConfig.getCapacity(c, w);

                        if (activeCount > capacity) {
                            String err = String.format("OVERLAP ERROR: C%d W%d Hour %d -> Active: %d > Cap: %d",
                                    c, w, h, activeCount, capacity);
                            report.append(err).append("\n");
                            System.err.println(err);
                            overlapFound = true;
                        }
                    }
                }
            }
            if(!overlapFound) {
                report.append("Capacity Checks: OK\n");
            }

            writeToFile(report.toString());

        } finally {
            Database.unlockAll();
        }
    }

    private void writeToFile(String content) {
        try (PrintWriter out = new PrintWriter(new FileWriter("verification_report.txt", true))) {
            out.print(content);
        } catch (IOException e) {
            System.err.println("Could not write verification report: " + e.getMessage());
        }
    }
}