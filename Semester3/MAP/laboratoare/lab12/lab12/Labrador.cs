namespace lab12;

public class Labrador : Dog
{
    private string color;

    public Labrador(string name, int age, string color) : base(name, age)
    {
        this.color = color;
    }
    
    public string Color
    {
        get => color;
        set => color = value;
    }

    public override string ToString()
    {
        return base.ToString()+$" | Color: {color}";
    }
}