namespace Sem10;

public abstract class Task
{
    private string id;
    private string description;

    public abstract void execute();
    public Task(string id, string description)
    {
        this.id = id;
        this.description = description;
    }

    public string Id
    {
        get => id;
        set => id = value;
    }

    public string Description
    {
        get => description;
        set => description = value;
    }

    public override string ToString()
    {
        return $"Task {id}: {description}";
    }
    
}