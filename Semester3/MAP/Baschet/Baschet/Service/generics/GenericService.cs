namespace Baschet;

public class GenericService<ID,E,R> where R : IRepository<ID, E> where E : Entity<ID>
{
    private R repository;
    
    public GenericService(R repository) => this.repository = repository;

    public IEnumerable<E> findAll()
    {
        return repository.findAll();
    }
}