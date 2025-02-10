namespace Baschet;

public class JucatorFileRepo : FileRepositoryGeneric<int, Jucator>
{
    public JucatorFileRepo(string filename) : base(filename) { }
    
    public override Jucator stringToEntity(string entity) 
    {
        string[] atributes = entity.Split(',');
        int id = int.Parse(atributes[0]);
        string name = atributes[1];
        string school = atributes[2];
        int idTeam = int.Parse(atributes[3]);
        Jucator jucator = new Jucator(name, school, idTeam);
        jucator.Id = id;
        return jucator;
    }
}