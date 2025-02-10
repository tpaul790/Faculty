namespace lab11;

public class Student : Person
{
    public Student() : base() { }

    public void ShowAge()
    {
        Console.WriteLine($"My age is {base.getAge()} years old.");
    }
}