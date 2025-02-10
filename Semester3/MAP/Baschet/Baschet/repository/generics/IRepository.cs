namespace Baschet;

public interface IRepository<ID, E> where E : Entity<ID>
{
    E findOne(ID id);
    
    void save(E entity);
    
    bool delete(E entity);
    
    void update(E entity);
    
    IEnumerable<E> findAll();
}