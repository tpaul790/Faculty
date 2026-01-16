package ppd;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.concurrent.*;


public class Main {
    static final String DB_URL = "jdbc:postgresql://localhost:5432/ppd";
    static final String DB_USER = "admin";
    static final String DB_PASS = "admin";

    static final int NUM_PROJECTS = 10;
    static final int MAX_QUEUE = 50;

    static final List<Integer> listaTrisori = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) throws Exception {
//        DatabaseSetup.initDB();

        int p_r = 4;
        int p_w = 8;

        FineGrainedList mainList = new FineGrainedList();
        FineGrainedList sortedList = new FineGrainedList();
        ScoreProcessingQueue queue = new ScoreProcessingQueue(MAX_QUEUE);

        long startTime = System.currentTimeMillis();

        ExecutorService readerPool = Executors.newFixedThreadPool(p_r);

        for (int i = 1; i <= NUM_PROJECTS; i++) {
            final String tableName = "proiect_" + i;
            readerPool.submit(() -> {
                try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                     Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT id_student, nota FROM " + tableName)) {

                    while (rs.next()) {
                        int id = rs.getInt("id_student");
                        int nota = rs.getInt("nota");
                        queue.put(new ScoreDto(id, nota));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        Thread[] workers = new Thread[p_w];
        CountDownLatch workersLatch = new CountDownLatch(p_w);

        for (int i = 0; i < p_w; i++) {
            workers[i] = new Thread(() -> {
                try {
                    while (true) {
                        ScoreDto p = queue.get();
                        if (p == null) break;
                        mainList.addScore(p.id, p.nota);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    workersLatch.countDown();
                }
            });
            workers[i].start();
        }

        readerPool.shutdown();
        readerPool.awaitTermination(2, TimeUnit.MINUTES);
        queue.setProducersFinished();
        workersLatch.await();

        System.out.println("Faza 1 (DB -> Lista) completa.");

        CountDownLatch sortLatch = new CountDownLatch(p_w);

        for (int i = 0; i < p_w; i++) {
            new Thread(() -> {
                while (true) {
                    Node n = mainList.removeFirstUserNode();
                    if (n == null) break;

                    if (n.isCheater) {
                        listaTrisori.add(n.id);
                    } else {
                        sortedList.insertSortedByScore(n);
                    }
                }
                sortLatch.countDown();
            }).start();
        }

        sortLatch.await();

        sortedList.writeToFile("rezultate.txt");
        writeCheaters("trisori.txt");

        long endTime = System.currentTimeMillis();
        System.out.println("Gata! Timp: " + (endTime - startTime) + " ms");
        System.out.println("Nr. studenti respinsi (copiat): " + listaTrisori.size());
    }

    static void writeCheaters(String filename) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            bw.newLine();
            Collections.sort(listaTrisori);
            for (Integer id : listaTrisori) {
                bw.write("Student ID: " + id);
                bw.newLine();
            }
        }
    }
}

