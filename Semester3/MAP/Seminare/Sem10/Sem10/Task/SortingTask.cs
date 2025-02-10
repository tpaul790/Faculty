namespace Sem10;

public class SortingTask : Task
{
    private Sorter sorter;
    private int[] array;

    public SortingTask(string id, string description, int[] array, SortStrategy strategy) : base(id, description)
    {
        this.array = array;
        sorter = SorterFactory.getInstance().createSorter(strategy);
    }
    
    public override void execute()
    {
        sorter.sort(array);
        foreach (var nr in array)
        {
            Console.Write(nr + " ");
        }
        Console.WriteLine();
    }
}