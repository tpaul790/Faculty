package ppd;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FineGrainedList {
    private Node head, tail;

    public FineGrainedList() {
        head = new Node(Integer.MIN_VALUE, 0, null);
        tail = new Node(Integer.MAX_VALUE, 0, null);
        head.next = tail;
    }

    public void addScore(int id, int nota) {
        head.lock.lock();
        Node pred = head;
        try {
            Node curr = pred.next;
            curr.lock.lock();
            try {
                while (curr.id < id) {
                    pred.lock.unlock();
                    pred = curr;
                    curr = curr.next;
                    curr.lock.lock();
                }

                if (curr.id == id) {
                    if (nota == -1) {
                        curr.isCheater = true;
                    } else {
                        if (!curr.isCheater) {
                            curr.notaTotala += nota;
                        }
                    }
                } else {
                    pred.next = new Node(id, nota, curr);
                }
            } finally {
                curr.lock.unlock();
            }
        } finally {
            pred.lock.unlock();
        }
    }

    public void insertSortedByScore(Node nodeToInsert) {
        nodeToInsert.next = null;

        head.lock.lock();
        Node pred = head;
        try {
            Node curr = pred.next;
            curr.lock.lock();
            try {
                while (curr != tail && curr.notaTotala >= nodeToInsert.notaTotala) {
                    pred.lock.unlock();
                    pred = curr;
                    curr = curr.next;
                    curr.lock.lock();
                }
                nodeToInsert.next = curr;
                pred.next = nodeToInsert;
            } finally {
                curr.lock.unlock();
            }
        } finally {
            pred.lock.unlock();
        }
    }

    public Node removeFirstUserNode() {
        head.lock.lock();
        try {
            Node first = head.next;
            first.lock.lock();
            try {
                if (first == tail) return null;

                head.next = first.next;
                return first;
            } finally {
                first.lock.unlock();
            }
        } finally {
            head.lock.unlock();
        }
    }

    public void writeToFile(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            Node curr = head.next;
            while (curr != tail) {
                writer.write("ID: " + curr.id + ", Nota Finala: " + curr.notaTotala);
                writer.newLine();
                curr = curr.next;
            }
        }
    }
}
