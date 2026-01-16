package ppd;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ScoreProcessingQueue {
    private final Queue<ScoreDto> queue = new LinkedList<>();
    private final int capacity;
    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();
    private boolean producersFinished = false;

    public ScoreProcessingQueue(int capacity) { this.capacity = capacity; }

    public void put(ScoreDto p) throws InterruptedException {
        lock.lock();
        try {
            while (queue.size() == capacity)
                notFull.await();
            queue.add(p);
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public ScoreDto get() throws InterruptedException {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                if (producersFinished)
                    return null;
                notEmpty.await();
            }
            ScoreDto p = queue.poll();
            notFull.signal();
            return p;
        } finally { lock.unlock(); }
    }

    public void setProducersFinished() {
        lock.lock();
        try {
            producersFinished = true;
            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }
}
