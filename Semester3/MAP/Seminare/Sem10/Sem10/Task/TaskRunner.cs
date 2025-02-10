namespace Sem10;

public interface TaskRunner
{
    void executeOneTask();
    void executeAll();
    void addTask(Task task);
    bool hasTasks();
}