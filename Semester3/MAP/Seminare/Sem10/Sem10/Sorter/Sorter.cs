namespace Sem10;

public abstract class Sorter
{
    public abstract void sort(int[] array);
    
    public void swap(int[] array, int i, int j)
    {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}