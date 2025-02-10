namespace Sem11;

public interface IRepository<ID, E> where E : Entity<ID>
{ 
    IEnumerable<E> getAll();
}