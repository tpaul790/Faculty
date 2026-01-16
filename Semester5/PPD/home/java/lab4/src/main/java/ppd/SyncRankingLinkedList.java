package ppd;

import java.util.LinkedList;

public class SyncRankingLinkedList {
    private Node head = null;

    public synchronized void save(int id, int points) {
        Node prev = null, current = head;
        while (current != null) {
            if (current.id == id) {
                current.score += points;

                if (prev != null) {
                    prev.next = current.next;
                } else {
                    head = current.next;
                }
                insertInOrder(current);
                return;
            }
            prev = current;
            current = current.next;
        }
        Node newNode = new Node(id, points);
        insertInOrder(newNode);
    }

    protected synchronized void insertInOrder(Node newNode) {
        if (head == null || head.score < newNode.score || (head.score == newNode.score && head.id < newNode.id)) {
            newNode.next = head;
            head = newNode;
            return;
        }

        Node current = head;
        while (current.next != null &&
                (current.next.score > newNode.score || current.next.score == newNode.score && current.next.id > newNode.id)) {
            current = current.next;
        }
        newNode.next = current.next;
        current.next = newNode;
    }

    public LinkedList<Node> getRanking() {
        LinkedList<Node> list = new LinkedList<>();
        Node current = head;
        while (current != null) {
            list.add(new Node(current.id, current.score));
            current = current.next;
        }
        return list;
    }
}
