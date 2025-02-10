namespace Baschet;

public class Triple<T1, T2, T3>
{
    private T1 first;
    private T2 second;
    private T3 third;

    public Triple(T1 first, T2 second, T3 third)
    {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public T1 First
    {
        get => first;
        set => first = value;
    }

    public T2 Second
    {
        get => second;
        set => second = value;
    }

    public T3 Third
    {
        get => third;
        set => third = value;
    }
    
    public override string ToString()
    {
        return $"({first} - {second} : {third})";
    }
}