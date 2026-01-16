package ppd;

public class Node {
    int id;
    int score;
    Node next;

    public Node(int id, int score) {
        this.id = id;
        this.score = score;
        this.next = null;
    }

    public int getId() {
        return id;
    }

    public int getScore() {
        return score;
    }

    public Node getNext() {
        return next;
    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", score=" + score +
                '}';
    }
}