namespace Baschet;

public class MemoryRepositoryGeneric<ID,E> : IRepository<ID, E> where E : Entity<ID>
{
    private Dictionary<ID, E> items = new Dictionary<ID, E>();
    
    public E findOne(ID id)
    {
        return items[id];
    }

    public IEnumerable<E> findAll()
    {
        return items.Values;
    }

    public void save(E entity)
    {
        items.Add(entity.Id, entity);
    }

    public bool delete(E entity)
    {
        return items.Remove(entity.Id);
    }

    public void update(E entity)
    {
        items[entity.Id] = entity;
    }
}