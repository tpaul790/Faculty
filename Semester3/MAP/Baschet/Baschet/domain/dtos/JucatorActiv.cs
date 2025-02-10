

namespace Baschet;

public class JucatorActiv : Entity<Tuple<int, Triple<int, int, DateTime>>>
{
    private int nrPuncteInscrise;
    private TipJucator tipJucator;

    public JucatorActiv(int nrPuncteInscrise, TipJucator jucator)
    {
        this.nrPuncteInscrise = nrPuncteInscrise;
        tipJucator = jucator;
    }

    public Tuple<int, Triple<int, int, DateTime>> Id { get => base.Id; set => base.Id = value; }
    public int  IdJucator { get => base.Id.First; set => base.Id.First = value; }
    
    public int NrPuncteInscrise{ get => nrPuncteInscrise; set => nrPuncteInscrise = value; }
    
    public TipJucator TipJucator { get => tipJucator; set => tipJucator = value; }
    
    public Triple<int, int, DateTime> IdMeci { get => base.Id.Second; set => base.Id.Second = value; }

    public override string ToString()
    {
        return $"IdJucator: {IdJucator}, IdMeci: {IdMeci}, NrPuncteInscrise: {NrPuncteInscrise}";
    }
}