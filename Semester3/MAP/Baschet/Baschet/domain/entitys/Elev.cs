namespace Baschet;

public class Elev : Entity<int>
{
    private string nume;
    private string scoala;
    
    public Elev(string nume, string scoala) { this.nume = nume; this.scoala = scoala; }
    
    public string Nume { get => nume; set => nume = value; }
    
    public string Scoala { get => scoala; set => scoala = value; }

    public override string ToString()
    {
        return $"{nume} - {scoala}";
    }
}