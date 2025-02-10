namespace Sem10;

public class BubbleSort : Sorter
{
    public override void sort(int[] array)
    {
        bool ok = true;
        while (ok)
        {
            ok = false;
            for(int i = 0; i < array.Length - 1; i++)
                if (array[i] > array[i + 1])
                {
                    swap(array, i, i + 1);
                    ok = true;
                }
        }
        Console.WriteLine("Sorted using Bubble Sort");
    }
}