namespace Baschet;

public class EchipaFileRepo : FileRepositoryGeneric<int, Echipa>
{
    public EchipaFileRepo(string filename) : base(filename) { }

    public override Echipa stringToEntity(string entity)
    {
        string[] atributes = entity.Split(',');
        int id = int.Parse(atributes[0]);
        string name = atributes[1];
        Echipa echipa = new Echipa(name);
        echipa.Id = id;
        return echipa;
    }
}