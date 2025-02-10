namespace Sem11;

public class InMemoryRepository<ID, E> : IRepository<ID, E> where E : Entity<ID>
{
    private Dictionary<ID, E> items = new Dictionary<ID, E>();
    
    public IEnumerable<E> getAll()
    {
        return items.Values;    
    }

    public void save(E item)
    {
        items.Add(item.Id, item);
    }
}