namespace Baschet;

public class Entity<ID>
{
    private ID id;

    public ID Id { get => id; set => id = value; }
}