namespace Baschet;

public class MeciFileRepo : FileRepositoryGeneric<Triple<int, int, DateTime>, Meci>
{
    public MeciFileRepo(string filename) : base(filename){ }

    public override Meci stringToEntity(string entity)
    {
        string[] atributes = entity.Split(',');
        int echipa1 = int.Parse(atributes[0]);
        int echipa2 = int.Parse(atributes[1]);
        DateTime date = DateTime.Parse(atributes[2]);
        Meci meci = new Meci();
        meci.Id = new Triple<int, int, DateTime>(echipa1, echipa2, date);
        return meci;
    }
}