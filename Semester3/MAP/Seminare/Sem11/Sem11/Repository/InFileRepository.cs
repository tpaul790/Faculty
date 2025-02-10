namespace Sem11;

public abstract class InFileRepository<ID, E> : InMemoryRepository<ID, E> where E : Entity<ID>
{
    private string fileName;

    public InFileRepository(string fileName)
    {
        this.fileName = fileName;
        loadFromFile();
    }
    
    public virtual void loadFromFile(){
        try
        {
            string[] lines = System.IO.File.ReadAllLines(fileName);
            foreach (var line in lines)
            {
                E entity = stringToEntity(line);
                base.save(entity);
            }
        }
        catch (Exception e)
        {
            Console.WriteLine(e);
        }
    }
    
    public abstract E stringToEntity(string line);
}