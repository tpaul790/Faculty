
namespace Sem11;

public class Ui
{
    private AngajatiService angajatiService;
    private SarcinaService sarcinaService;

    public Ui(AngajatiService angajatiService, SarcinaService sarcinaService)
    {
        this.angajatiService = angajatiService;
        this.sarcinaService = sarcinaService;
    }

    public void printMenu()
    {
        Console.WriteLine("Welcome to the Angajati Menu");
        Console.WriteLine("p. Print Menu");
        Console.WriteLine("a. Print angajati");
        Console.WriteLine("s. Print sarcini");
        Console.WriteLine("0. Exit");
    }

    public void printAll<T>(IEnumerable<T> all)
    {
        foreach (var item in all)
        {
            Console.WriteLine(item);
        }
    }

    public void start()
    {
        printMenu();
        while (true)
        {
            Console.Write("Option: ");
            string option = Console.ReadLine();
            if (option == "0")
            {
                break;
            }else if (option == "p"){
                printMenu();
            }else if (option == "a")
            {
                printAll(angajatiService.getAll());
            }else if (option == "s")
            {
                printAll(sarcinaService.getAll());
            }
        }
    }
}