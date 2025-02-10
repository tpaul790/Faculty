namespace Sem10;

public class TaskContainerFactory : Factory
{
    private static TaskContainerFactory instance;
    
    private TaskContainerFactory() { }

    public static TaskContainerFactory getInstance()
    {
        if (instance == null)
            instance = new TaskContainerFactory();
        return instance;
    }
    
    public AbstractContainer createContainer(Strategy strategy)
    {
        if (Strategy.FIFO == strategy)
            return new Queue();
        return new Stack();
    }
}