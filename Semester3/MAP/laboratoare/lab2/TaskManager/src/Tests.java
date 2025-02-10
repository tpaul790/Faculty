import Decorator.DelayTaskRunner;
import Decorator.PrinterTaskRunner;
import Decorator.StrategyTaskRunner;
import Domain.BubbleSort;
import Domain.MessageTask;
import Domain.QuickSort;
import Domain.SortingTask;
import Factory.Strategy;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Tests {

    public static void main(String[] args) {
//        showMessages(createMessages());
//        sortTest();
        if(args.length > 0) {
            System.out.println("Strategy task runner:");
            strategyTaskRunnerTest(args[0]);

            System.out.println();
            System.out.println("Printer task runner:");
            decoratorPrinterTaskRunnerTest(args[0]);

            System.out.println();
            System.out.println("Delay task runner:");
            decoratorDelayTaskRunnerTest(args[0]);
        }
    }

    public static ArrayList<MessageTask> createMessages(){
        MessageTask msg1 = new MessageTask("1", "feedback lab 2", "Te-ai descurcat bine1", "teacher", "student", LocalDateTime.now());
        MessageTask msg2 = new MessageTask("2", "feedback lab 3", "Te-ai descurcat bine2", "teacher", "student", LocalDateTime.now());
        MessageTask msg3 = new MessageTask("3", "feedback lab 4", "Te-ai descurcat bine3", "teacher", "student", LocalDateTime.now());
        MessageTask msg4 = new MessageTask("4", "feedback lab 5", "Te-ai descurcat bine4", "teacher", "student", LocalDateTime.now());
        MessageTask msg5 = new MessageTask("5", "feedback lab 6", "Te-ai descurcat bine5", "teacher", "student", LocalDateTime.now());

        ArrayList<MessageTask> messages = new ArrayList<>();
        messages.add(msg1);
        messages.add(msg2);
        messages.add(msg3);
        messages.add(msg4);
        messages.add(msg5);
        return messages;
    }

    public static void showMessages(ArrayList<MessageTask> messages){
        for(MessageTask m : messages){
            System.out.println(m.toString());
        }
    }

    public static void sortTest(){
        BubbleSort bubbleSort = new BubbleSort();
        QuickSort quickSort = new QuickSort();
        int[] nrs1 = new int[]{3,5,2,1,6,3,9};
        SortingTask sortingTask1 = new SortingTask("1","Number sorter",nrs1,bubbleSort);
        sortingTask1.execute();

        System.out.println();

        int[] nrs2 = new int[]{3,5,2,1,6,3,9};
        SortingTask sortingTask2 = new SortingTask("2","Number sorter",nrs2,quickSort);
        sortingTask2.execute();

    }

    public static void strategyTaskRunnerTest(String strategy){
        ArrayList<MessageTask> messages = createMessages();
        StrategyTaskRunner taskRunner = new StrategyTaskRunner(Strategy.valueOf(strategy));
        for(MessageTask m : messages){
            taskRunner.addTask(m);
        }
        taskRunner.executeAll();
    }

    public static void decoratorPrinterTaskRunnerTest(String strategy){
        ArrayList<MessageTask> messages = createMessages();
        StrategyTaskRunner strategyTaskRunner = new StrategyTaskRunner(Strategy.valueOf(strategy));
        for(MessageTask m : messages){
            strategyTaskRunner.addTask(m);
        }
        PrinterTaskRunner printerTaskRunner = new PrinterTaskRunner(strategyTaskRunner);
        printerTaskRunner.executeAll();
    }

    public static void decoratorDelayTaskRunnerTest(String strategy){
        ArrayList<MessageTask> messages = createMessages();
        StrategyTaskRunner strategyTaskRunner = new StrategyTaskRunner(Strategy.valueOf(strategy));
        for(MessageTask m : messages){
            strategyTaskRunner.addTask(m);
        }
        PrinterTaskRunner printerTaskRunner = new PrinterTaskRunner(strategyTaskRunner);
        DelayTaskRunner delayTaskRunner = new DelayTaskRunner(printerTaskRunner);
        delayTaskRunner.executeAll();
    }
}
