package Decorator;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PrinterTaskRunner extends AbstractTaskRunner {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    public PrinterTaskRunner(TaskRunner taskRunner) {
        super(taskRunner);
    }

    @Override
    public void executeOneTask(){
        super.executeOneTask();
        System.out.println("Task executed at: " + LocalDateTime.now().format(formatter));
    }
}
