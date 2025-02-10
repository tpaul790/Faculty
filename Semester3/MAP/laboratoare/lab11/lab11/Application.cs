namespace lab11;

public class Application
{
    public static void Main()
    {
        Person person = new Person();
        person.Greet();
        
        Student student = new Student();
        student.setAge(21);
        student.ShowAge();

        Profesor profesor = new Profesor("mate");
        profesor.setAge(30);
        profesor.Explain();
        profesor.Greet();

    }
}