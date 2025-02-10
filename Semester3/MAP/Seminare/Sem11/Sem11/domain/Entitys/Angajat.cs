namespace Sem11;

public class Angajat : Entity<String>
{
    private string nume;
    private double venitPeOra;
    private Nivel nivel;

    public Angajat(string nume, double venitPeOra, Nivel nivel)
    {
        this.nume = nume;
        this.venitPeOra = venitPeOra;
        this.nivel = nivel;
    }
    
    public string Nume { get => nume; set => nume = value; }
    
    public double VenitPeOra { get => venitPeOra; set => venitPeOra = value; }
    
    public Nivel Nivel { get => nivel; set => nivel = value; }

    public override string ToString()
    {
        return $"{nume}: {venitPeOra} : {nivel}";
    }
}