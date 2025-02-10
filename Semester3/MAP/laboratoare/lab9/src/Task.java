import java.util.Random;
import java.util.random.RandomGenerator;

public class Task implements Runnable {
    private Random random = new Random();
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " is running");
        int sec = random.nextInt(1,3);
        sec *= 1000;
        try {
            Thread.sleep(sec);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
