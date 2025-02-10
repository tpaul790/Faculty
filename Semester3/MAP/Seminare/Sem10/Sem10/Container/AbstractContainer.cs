namespace Sem10;

public abstract class AbstractContainer : Container
{
    protected List<Task> tasks = new List<Task>();
    
    public abstract Task remove();

    public void add(Task task)
    {
        tasks.Add(task);
    }

    public int size()
    {
        return tasks.Count;
    }

    public bool isEmpty()
    {
        return size() == 0;
    }
}