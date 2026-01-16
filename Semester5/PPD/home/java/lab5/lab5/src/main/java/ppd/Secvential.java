package ppd;

import java.sql.*;
import java.util.*;
import java.io.*;

public class Secvential {
    static final String DB_URL = "jdbc:postgresql://localhost:5432/ppd";
    static final String DB_USER = "admin";
    static final String DB_PASS = "admin";

    static class Node {
        int id;
        int notaTotala;
        boolean isCheater;
        Node next;

        Node(int id, int nota) {
            this.id = id;
            if (nota == -1) {
                this.isCheater = true;
                this.notaTotala = 0;
            } else {
                this.isCheater = false;
                this.notaTotala = nota;
            }
        }
    }

    private Node head = null;

    public void readAllFromDB() throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            for (int i = 1; i <= 10; i++) {
                String tableName = "proiect_" + i;
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT id_student, nota FROM " + tableName)) {

                    while (rs.next()) {
                        addScore(rs.getInt("id_student"), rs.getInt("nota"));
                    }
                }
            }
        }
    }

    private void addScore(int id, int nota) {
        if (head == null) {
            head = new Node(id, nota);
            return;
        }
        Node curr = head;
        Node pred = null;
        while (curr != null && curr.id < id) {
            pred = curr;
            curr = curr.next;
        }
        if (curr != null && curr.id == id) {
            if (nota == -1) curr.isCheater = true;
            else if (!curr.isCheater) curr.notaTotala += nota;
        } else {
            Node newNode = new Node(id, nota);
            if (pred == null) {
                newNode.next = head;
                head = newNode;
            } else {
                newNode.next = curr;
                pred.next = newNode;
            }
        }
    }

    public void processAndSave() throws IOException {
        List<Integer> trisori = new ArrayList<>();
        Node sortedHead = null;

        Node curr = head;
        while (curr != null) {
            Node next = curr.next;
            if (curr.isCheater) {
                trisori.add(curr.id);
            } else {
                sortedHead = insertSorted(sortedHead, curr);
            }
            curr = next;
        }

        try (PrintWriter pw = new PrintWriter("rezultate_secv.txt")) {
            Node temp = sortedHead;
            while (temp != null) {
                pw.println("ID: " + temp.id + ", Nota Finala: " + temp.notaTotala);
                temp = temp.next;
            }
        }

        try (PrintWriter pw = new PrintWriter("trisori_secv.txt")) {
            Collections.sort(trisori);
            for (Integer id : trisori) pw.println("Student ID: " + id);
        }
    }

    private Node insertSorted(Node sHead, Node newNode) {
        if (sHead == null || sHead.notaTotala <= newNode.notaTotala) {
            newNode.next = sHead;
            return newNode;
        }
        Node current = sHead;
        while (current.next != null && current.next.notaTotala > newNode.notaTotala) {
            current = current.next;
        }
        newNode.next = current.next;
        current.next = newNode;
        return sHead;
    }

//    public static void main(String[] args) {
//        Secvential app = new Secvential();
//        try {
//            long startTime = System.currentTimeMillis();
//
//            app.readAllFromDB();
//            app.processAndSave();
//
//            long endTime = System.currentTimeMillis();
//            System.out.println("Timp Secvential (DB): " + (endTime - startTime) + " ms");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}