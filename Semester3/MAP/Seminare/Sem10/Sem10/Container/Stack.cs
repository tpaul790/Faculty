namespace Sem10;

public class Stack : AbstractContainer
{
    public override Task remove()
    {
        Task task = tasks.LastOrDefault();
        tasks.RemoveAt(tasks.Count - 1);
        return task;
    }
}