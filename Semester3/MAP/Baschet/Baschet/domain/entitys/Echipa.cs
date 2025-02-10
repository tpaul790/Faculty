namespace Baschet;

public class Echipa : Entity<int>
{
    private string nume;

    public Echipa(string nume) { this.nume = nume; }
    
    public string Nume { get => nume; set => nume = value; }

    public override string ToString()
    {
        return nume;
    }
}