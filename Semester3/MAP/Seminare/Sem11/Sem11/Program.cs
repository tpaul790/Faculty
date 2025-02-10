using Sem11;

namespace Sem11;

public class Program
{
    public static void Main(string[] args)
    {
        AngajatiInFileRepository angajatiInFileRepository = new AngajatiInFileRepository("/Users/titienipaul/Desktop/Facultate/sem3/MAP/Sem11/Sem11/data/angajati.txt");
        AngajatiService angajatiService = new AngajatiService(angajatiInFileRepository);
        
        SarcinaInFileRepository sarcinaInFileRepository = new SarcinaInFileRepository("/Users/titienipaul/Desktop/Facultate/sem3/MAP/Sem11/Sem11/data/sarcini.txt");
        SarcinaService sarcinaService = new SarcinaService(sarcinaInFileRepository);
        
        Ui ui = new Ui(angajatiService, sarcinaService);
        
        ui.start();

        
        
    }
}