namespace lab12;

public class Dog
{
    private string name;
    private int age;

    public Dog(string name, int age)
    {
        this.name = name;
        this.age = age;
    }

    public void Bark()
    {
        Console.WriteLine("Bark");
    }

    public void Eat()
    {
        Console.WriteLine("Eat");
    }
    
    public string Name
    {
        get => name;
        set => name = value;
    }

    public int Age
    {
        get => age;
        set => age = value;
    }

    public override string ToString()
    {
        return $"Name: {name} | Age: {age}";
    }
}