package ppd;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Node {
    int id;
    int notaTotala;
    boolean isCheater;
    Node next;
    final Lock lock = new ReentrantLock();

    public Node(int id, int nota, Node next) {
        this.id = id;
        this.next = next;
        if (nota == -1) {
            this.isCheater = true;
            this.notaTotala = 0;
        } else {
            this.isCheater = false;
            this.notaTotala = nota;
        }
    }
}
