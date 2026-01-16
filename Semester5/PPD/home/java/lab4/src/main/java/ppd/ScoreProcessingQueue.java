package ppd;

import java.util.LinkedList;
import java.util.Queue;

class ScoreProcessingQueue {
    private final Queue<ScoreRecord> queue = new LinkedList<>();

    public synchronized void enqueue(ScoreRecord record) throws InterruptedException {
        queue.add(record);
        notifyAll();
    }

    public synchronized ScoreRecord dequeue() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }
        return queue.poll();
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
}
