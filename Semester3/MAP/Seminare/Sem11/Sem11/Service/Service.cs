namespace Sem11;

public class Service<R,ID,E> where R : InFileRepository<ID,E> where E : Entity<ID>
{
    private R repo;

    public Service(R repo)
    {
        this.repo = repo;
    }
    public IEnumerable<E> getAll()
    {
        return repo.getAll();
    }
}