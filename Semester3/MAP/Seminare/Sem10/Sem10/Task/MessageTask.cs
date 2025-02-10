namespace Sem10;

public class MessageTask : Task
{
    private string message, from, to;
    private DateTime date;

    public MessageTask(string id, string description, string message, string from, string to, DateTime date) : base(id, description)
    {
        this.message = message;
        this.from = from;
        this.to = to;
        this.date = date;
    }
    
    public override void execute()
    {
        Console.WriteLine($"Message: {message} | Date: {date}");
    }

    public override string ToString()
    {
        return base.ToString() + $"| Message: {message} | From: {from} | To: {to} | Date: {date}";
    }
}