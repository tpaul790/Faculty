namespace Baschet;

public static class Program
{
    public static void Main(string[] args)
    {
        JucatorFileRepo jucatorFileRepo = new JucatorFileRepo("/Users/titienipaul/Desktop/Facultate/sem3/MAP/Baschet/Baschet/data/jucatori.txt");
        JucatorActivFileRepo jucatorActivFileRepo = new JucatorActivFileRepo("/Users/titienipaul/Desktop/Facultate/sem3/MAP/Baschet/Baschet/data/jucatoriActivi.txt");
        MeciFileRepo meciFileRepo = new MeciFileRepo("/Users/titienipaul/Desktop/Facultate/sem3/MAP/Baschet/Baschet/data/meci.txt");
        EchipaFileRepo echipaFileRepo = new EchipaFileRepo("/Users/titienipaul/Desktop/Facultate/sem3/MAP/Baschet/Baschet/data/echipa.txt");
        Service service = new Service();
        service.setJucatorRepo(jucatorFileRepo);
        service.setJucatorActivRepo(jucatorActivFileRepo);
        service.setMeciFileRepo(meciFileRepo);
        service.setEchipaRepo(echipaFileRepo);
        Ui ui = new Ui(service);
        ui.start();
    }
}