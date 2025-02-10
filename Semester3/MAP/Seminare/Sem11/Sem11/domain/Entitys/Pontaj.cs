namespace Sem11;

public class Pontaj : Entity<Tuple<string, string>>
{
    private Angajat angajat;
    private Sarcina sarcina;
    private DateTime date;

    public Pontaj(Angajat angajat, Sarcina sarcina, DateTime date)
    {
        this.angajat = angajat;
        this.sarcina = sarcina;
        this.date = date;
    }
    
    public Angajat Angajat { get => angajat; set => angajat = value; }
    
    public Sarcina Sarcina { get => sarcina; set => sarcina = value; }
    
    public DateTime Date { get => date; set => date = value; }
}