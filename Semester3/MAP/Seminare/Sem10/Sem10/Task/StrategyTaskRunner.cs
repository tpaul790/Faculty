namespace Sem10;

public class StrategyTaskRunner : TaskRunner
{
    private Container container;

    public StrategyTaskRunner(Strategy strategy)
    {
        container = TaskContainerFactory.getInstance().createContainer(strategy);
    }

    public void executeOneTask()
    {
        if (!container.isEmpty())
        {
            Task task = container.remove();
            task.execute();
        }
    }

    public void executeAll()
    {
        while(!container.isEmpty())
            executeOneTask();
    }

    public void addTask(Task task)
    {
        container.add(task);
    }

    public bool hasTasks()
    {
        return !container.isEmpty();
    }
    
}