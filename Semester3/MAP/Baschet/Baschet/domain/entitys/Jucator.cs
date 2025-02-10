namespace Baschet;

public class Jucator : Elev
{
    private int idEchipa;

    public Jucator(string nume, string scoala, int echipa) : base(nume, scoala) { this.idEchipa = echipa; }

    public int Echipa { get => idEchipa; set => idEchipa = value; }
    
}