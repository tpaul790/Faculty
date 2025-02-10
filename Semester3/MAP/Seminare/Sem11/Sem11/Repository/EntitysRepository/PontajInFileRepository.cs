namespace Sem11;

public class PontajInFileRepository : InFileRepository<Tuple<string, string>, Pontaj>
{
    public PontajInFileRepository(string filename) : base(filename){ }

    public override Pontaj stringToEntity(string item)
    {
        return null;
    }
}