namespace Sem11;

public class SarcinaInFileRepository : InFileRepository<string, Sarcina>
{
    public SarcinaInFileRepository(string fileName) : base(fileName) { }

    public override Sarcina stringToEntity(string input)
    {
        var lines = input.Split(',');
        string id = lines[0];
        int nrOreEstimate = int.Parse(lines[2]);
        string dificultateStr = lines[1];
        Dificultate dificultate = new Dificultate();
        if (dificultateStr == "USOARA")
        {
            dificultate = Dificultate.USOARA;
        }else if (dificultateStr == "MEDIE")
        {
            dificultate = Dificultate.MEDIE;
        }else if (dificultateStr == "GREA")
        {
            dificultate = Dificultate.GREA;
        }
        Sarcina sarcina = new Sarcina(dificultate, nrOreEstimate);
        sarcina.Id = id;
        return sarcina;
    }
}