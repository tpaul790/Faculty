namespace Baschet;

public class ElevFileRepo : FileRepositoryGeneric<int,Elev>
{
    public ElevFileRepo(string filename) : base(filename) { }
    public override Elev stringToEntity(string entity) 
    {
        string[] atributes = entity.Split(',');
        int id = int.Parse(atributes[0]);
        string name = atributes[1];
        string school = atributes[2];
        Elev elev = new Elev(name, school);
        elev.Id = id;
        return elev;
    }
}