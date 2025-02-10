namespace Baschet;

public class Ui
{
    private Service service;
    public Ui(Service service)
    {
        this.service = service;
    }
    public void printMenu()
    {
        Console.WriteLine("Welcome to Baschet!");
        Console.WriteLine("1. Vezi toti jucatorii unei echipe!");
        Console.WriteLine("2. Vezi toti jucatorii activi ai unei echipe la un meci!");
        Console.WriteLine("3. Vezi toate meciurile dintr-o anumita perioada calendaristica!");
        Console.WriteLine("4. Vezi scorul de la un meci!");
    }

    public void seeAll<T>(IEnumerable<T> list)
    {
        foreach (var el in list)
        {
            Console.WriteLine(el);
        }
    }

    public void findAllPlayersForTeamUi()
    {
        Console.Write("Introduceti id-ul echipei: ");
        int id = int.Parse(Console.ReadLine());
        seeAll(service.findAllPlayersForTeam(id));
    }

    public void findAllTitularsForTeamAtMatchUi()
    {
        Console.Write("Introduceti id-ul echipei: ");
        int idEchipa1 = int.Parse(Console.ReadLine());
        Console.Write("Introduceti id-ul echipei rivale: ");
        int idEchipa2 = int.Parse(Console.ReadLine());
        Console.Write("Introduceti data meciului: ");
        DateTime dataMeciului = DateTime.Parse(Console.ReadLine());
        seeAll(service.findAllTitularsForTeamAtMatch(idEchipa1, new Triple<int, int ,DateTime>(idEchipa1, idEchipa2, dataMeciului)));
        
    }

    public void findAllMeciBetweenUi()
    {
        Console.Write("Introduceti data de inceput a perioadei: ");
        DateTime dataInceput = DateTime.Parse(Console.ReadLine());
        Console.Write("Introduceti data de sfarsit a perioadei: ");
        DateTime dataSfarsit = DateTime.Parse(Console.ReadLine());
        seeAll(service.findAllMeciBetween(dataInceput, dataSfarsit));
    }

    public void findScorForMeciUi()
    {
        Console.Write("Introduceti id-ul primei echipe: ");
        int id1 = int.Parse(Console.ReadLine());
        Console.Write("Introduceti id-ul celei de-a doua echipe: ");
        int id2 = int.Parse(Console.ReadLine());
        Console.Write("Introduceti data meciului: ");
        DateTime dataMeciului = DateTime.Parse(Console.ReadLine());
        Console.WriteLine(service.findScorForMeci(id1, id2, dataMeciului));
    }
    public void start()
    {
        printMenu(); ;

        while (true)
        {
            Console.Write("Alege o optiune: ");
            string optiune = Console.ReadLine();
            switch (optiune)
            {
                case "m":
                    printMenu();
                    break;
                case "1":
                    findAllPlayersForTeamUi();
                    break;
                case "2":
                    findAllTitularsForTeamAtMatchUi();
                    break;
                case "3":
                    findAllMeciBetweenUi();
                    break;
                case "4":
                    findScorForMeciUi();
                    break;
                case "e":
                    break;
                default:
                    Console.WriteLine("Optiune invalida!");
                    break;
            }
        }
    }
}