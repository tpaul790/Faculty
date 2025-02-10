namespace Sem10;

public class Queue : AbstractContainer
{
    public override Task remove()
    {
        Task task = tasks.FirstOrDefault();
        tasks.RemoveAt(0);
        return task;
    }
    
}