namespace lab12;

public class Program
{
    public static void Main()
    {
        Dogs();
        Map();
    }

    public static void Dogs()
    {
        Dog dog = new Dog("Max", 2);
        Dog haski1 = new Haski("Thor", 1, 30.5);
        Dog haski2 = new Haski("Nero", 4, 31.5);
        Dog labrador1 = new Labrador("Diego", 1, "white");
        Dog labrador2 = new Labrador("Jessi", 2, "black");
        
        List<Dog> dogs = new List<Dog> { dog, haski1, haski2, labrador1, labrador2 };
        
        dogs.Where(d => d.GetType() == typeof(Haski)).ToList().ForEach(d => Console.WriteLine(d));
    }

    public static void Map()
    {
        Dictionary<int, string> adresses = new Dictionary<int, string>();
        adresses.Add(1, "Str.Progresului");
        adresses.Add(2, "Str.Libertatii");
        adresses.Add(3, "Str.Louis");
        adresses.Add(4, "Str.Tulcea");
        adresses.Add(5, "Str.Dunarii");
        adresses.Add(6, "Str.Republicii");

        adresses.ToList().ForEach(d => Console.WriteLine(d));
    }
}