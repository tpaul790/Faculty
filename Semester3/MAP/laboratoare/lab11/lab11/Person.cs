namespace lab11;

public class Person
{
    private int age;

    public Person() { }

    public void Greet()
    {
        Console.WriteLine($"Hello!");
    }

    public void setAge(int age)
    {  
        this.age = age;
    }

    public int getAge()
    {
        return this.age;
    }
}