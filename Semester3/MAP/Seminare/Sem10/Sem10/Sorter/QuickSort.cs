namespace Sem10;

public class QuickSort : Sorter
{
    public override void sort(int[] array)
    {
        quickSort(array, 0, array.Length - 1);
        Console.WriteLine("Sorted using Quick Sort");
    }

    public void quickSort(int[] array, int left, int right)
    {
        if (left < right)
        {
            int pivotIndex = partition(array, left, right);
            quickSort(array, left, pivotIndex - 1);
            quickSort(array, pivotIndex + 1, right);
        }
    }
    private int partition(int[] array, int left, int right)
    {
        int pivot = array[right];
        int i = left - 1;

        for (int j = left; j < right; j++)
        {
            if (array[j] <= pivot)
            {
                i++;
                swap(array, i, j);
            }
        }

        swap(array, i + 1, right);
        return i + 1;
    }

}