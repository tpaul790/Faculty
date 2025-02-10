namespace Sem11;

public class Tuple<T1, T2>
{
    private T1 first;
    private T2 second;

    public Tuple(T1 first, T2 second)
    {
        this.first = first;
        this.second = second;
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
}