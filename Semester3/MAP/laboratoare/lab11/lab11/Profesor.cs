namespace lab11;

public class Profesor : Student
{
    private string subject;

    public Profesor(string subject) : base()
    {
        this.subject = subject;
    }

    public void Explain()
    {
        Console.WriteLine("Explanation begin");
    }
}