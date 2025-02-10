namespace lab12;

public class Haski : Dog
{
    private double speed;

    public Haski(string name, int age, double speed) : base(name, age)
    {
        this.speed = speed;
    }

    public void Run()
    {
        Console.WriteLine("Haski is running.");
    }

    public double Speed
    {
        get => speed;
        set => speed = value;
    }

    public override string ToString()
    {
        return base.ToString() + $" | Speed: {speed}";
    }
}