namespace Sem10;

public class DelayTaskRunner : AbstractTaskRunner
{
    public DelayTaskRunner(TaskRunner runner) : base(runner) { }

    public override void executeOneTask()
    {
        Thread.Sleep(TimeSpan.FromSeconds(2));
        base.executeOneTask();
    }
}