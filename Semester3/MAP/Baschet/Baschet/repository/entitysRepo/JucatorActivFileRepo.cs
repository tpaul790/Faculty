namespace Baschet;

public class JucatorActivFileRepo : FileRepositoryGeneric<Tuple<int, Triple<int, int, DateTime>>, JucatorActiv>
{
    public JucatorActivFileRepo(string filename) : base(filename) { }

    public override JucatorActiv stringToEntity(string entity)
    {
        string[] atributes = entity.Split(',');
        int idJucator = int.Parse(atributes[0]);
        int idEchipa1 = int.Parse(atributes[1]);
        int idEchipa2 = int.Parse(atributes[2]);
        DateTime dateEchipa = DateTime.Parse(atributes[3]);
        Triple<int,int,DateTime> idMeci = new Triple<int, int, DateTime>(idEchipa1, idEchipa2, dateEchipa);
        int nrPuncteInscrise = int.Parse(atributes[4]);
        TipJucator tipJucator;
        if (atributes[5] == "REZERVA")
            tipJucator = TipJucator.REZERVA;
        else
            tipJucator = TipJucator.TITULAR;
        JucatorActiv jucatorActiv = new JucatorActiv(nrPuncteInscrise, tipJucator);
        jucatorActiv.Id = new Tuple<int, Triple<int, int, DateTime>>(idJucator, idMeci);
        return jucatorActiv;
    } 
}