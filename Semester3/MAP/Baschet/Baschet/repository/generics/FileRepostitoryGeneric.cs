namespace Baschet;

public abstract class FileRepositoryGeneric<ID,E> : MemoryRepositoryGeneric<ID, E> where E : Entity<ID>{
    private string fileName;

    public FileRepositoryGeneric(string filename)
    {
        this.fileName = filename;
        loadFromFile();
    }

    public virtual void loadFromFile()
    {
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
    
    public abstract E stringToEntity(string entityString);
}