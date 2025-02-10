namespace Sem10;

public abstract class AbstractTaskRunner : TaskRunner
{
    protected TaskRunner runner;

    public AbstractTaskRunner(TaskRunner runner)
    {
        this.runner = runner;
    }

    public virtual void executeOneTask()
    {
        runner.executeOneTask();
    }

    public void executeAll()
    {
        while(runner.hasTasks())
            executeOneTask();
    }

    public void addTask(Task task)
    {
        runner.addTask(task);
    }

    public bool hasTasks()
    {
        return runner.hasTasks();
    }
}