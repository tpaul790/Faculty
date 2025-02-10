package Domain;

import javax.swing.text.DateFormatter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimerTask;

public class MessageTask extends Task{
    private String message,from,to;
    private LocalDateTime date;
    public static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public MessageTask(String taskId, String description, String message, String from, String to, LocalDateTime date) {
        super(taskId, description);
        this.message = message;
        this.from = from;
        this.to = to;
        this.date = date;
    }

    @Override
    public void execute() {
        System.out.println(date.format(dateFormatter)+": "+message);
    }

    @Override
    public String toString() {
        return "Task: " + getTaskId() + " | Description: " + getDescription() + " | From: " + from + " | To: " + to + " | Date: " + date.format(dateFormatter);
    }
}


