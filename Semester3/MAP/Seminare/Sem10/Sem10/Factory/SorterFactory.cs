namespace Sem10;

public class SorterFactory{
    private static SorterFactory? instance;
    
    private SorterFactory() { }

    public static SorterFactory getInstance()
    {
        if (instance == null)
            instance = new SorterFactory();
        return instance;
    }

    public Sorter createSorter(SortStrategy strategy)
    {
        if (strategy == SortStrategy.QUICK)
            return new QuickSort();
        return new BubbleSort();
    }
}