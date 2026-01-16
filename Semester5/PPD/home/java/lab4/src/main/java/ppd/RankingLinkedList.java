package ppd;

import java.util.LinkedList;

public class RankingLinkedList {
    private final LinkedList<Node> list = new LinkedList<>();

    public void save(int id, int points) {
        for (var node: list) {
            if (node.id == id) {
                node.score += points;
                list.remove(node);
                insertInOrder(node);
                return;
            }
        }
        insertInOrder(new Node(id, points));
    }

    private void insertInOrder(Node newNode) {
        if (list.isEmpty() || list.getFirst().score < newNode.score || (list.getFirst().score == newNode.score && list.getFirst().id < newNode.id)) {
            list.addFirst(newNode);
            return;
        }

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).score < newNode.score || (list.get(i).score == newNode.score && list.get(i).id < newNode.id)) {
                list.add(i, newNode);
                return;
            }
        }

        list.addLast(newNode);
    }

    protected void remove(int id) {
        list.removeIf(node -> node.id == id);
    }

    public LinkedList<Node> getRanking() {
        return list;
    }
}
