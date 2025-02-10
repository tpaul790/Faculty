namespace Sem11;

public class AngajatiInFileRepository : InFileRepository<string, Angajat>
{
    public AngajatiInFileRepository(string fileName) : base(fileName) { }

    public override Angajat stringToEntity(string input)
    {
        var lines = input.Split(',');
        string id = lines[0];
        string nume = lines[1];
        double venitPeOra = double.Parse(lines[2]);
        string nivelStr = lines[3];
        Nivel nivel = new Nivel();
        if (nivelStr == "SENIOR")
        {
            nivel = Nivel.SENIOR;
        }else if (nivelStr == "JUNIOR")
        {
            nivel = Nivel.JUNIOR;
        }else if (nivelStr == "MEDIUM")
        {
            nivel = Nivel.MEDIUM;
        }
        Angajat angajat = new Angajat(nume, venitPeOra, nivel);
        angajat.Id = id;
        return angajat;
    }
}