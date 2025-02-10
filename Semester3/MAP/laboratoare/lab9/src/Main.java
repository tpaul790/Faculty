import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws InterruptedException {
//        Runnable runnable = new SimpleThread();
//        Thread thread = new Thread(runnable);
//        thread.start();




//        NumberThread numberThread = new NumberThread();
//
//        Thread th1 = new Thread(numberThread);
//        Thread th2 = new Thread(numberThread);
//        Thread th3 = new Thread(numberThread);
//
//        th1.start();
//        th2.start();
//        th3.start();
//
//        th1.join();
//        th2.join();
//        th3.join();





//        Counter counter = new Counter();
//        Runnable runCounter = () -> {
//            for(int i = 1;i <= 1000; i++)
//                counter.increment();
//        };
//
//        Thread threadCounter1 = new Thread(runCounter);
//        Thread threadCounter2 = new Thread(runCounter);
//        threadCounter1.start();
//        threadCounter2.start();
//
//        threadCounter1.join();
//        threadCounter2.join();
//        System.out.println(counter.getCount());


        ExecutorService service = Executors.newFixedThreadPool(3);
        for(int i = 0; i < 5; i++){
            Runnable task = new Task();
            service.execute(task);
        }
        service.shutdown();
    }

}