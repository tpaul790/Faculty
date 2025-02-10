namespace Sem11;

public class Sarcina : Entity<String>
{
    private Dificultate dificultate;
    private int nrOreEstimate;

    public Sarcina(Dificultate dificultate, int nrOreEstimate)
    {
        this.dificultate = dificultate;
        this.nrOreEstimate = nrOreEstimate;
    }

    public Dificultate Dificultate { get => dificultate; set => dificultate = value; }
    
    public int NrOreEstimate { get => nrOreEstimate; set => nrOreEstimate = value; }

    public override string ToString()
    {
        return $"{Dificultate} - {NrOreEstimate}";
    }
}