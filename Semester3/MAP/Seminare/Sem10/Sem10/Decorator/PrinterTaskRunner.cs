namespace Sem10;

public class PrinterTaskRunner : AbstractTaskRunner
{
    public PrinterTaskRunner(TaskRunner runner) : base(runner){ }

    public override void executeOneTask()
    { 
        base.executeOneTask();
        Console.WriteLine("Task executed at : " + DateTime.Now);
    }
    
}