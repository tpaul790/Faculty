package Domain;

public class QuickSort extends AbstractSorter{
    @Override
    public void sort(int[] nrs) {
        quickSort(nrs, 0, nrs.length - 1);
        System.out.print("Sirul sortat folosind quicksort este: ");
        for(int i=0;i<nrs.length;i++){
            System.out.print(nrs[i]+" ");
        }
    }

    public static void quickSort(int[] nrs, int lo, int hi) {
        if(lo < hi){
            int pi = partition(nrs, lo, hi);
            quickSort(nrs, lo, pi-1);
            quickSort(nrs, pi+1, hi);
        }
    }

    public static int partition(int[] nrs, int lo, int hi) {
        int pivot = nrs[hi];
        int i = lo-1;
        for (int j = lo; j < hi; j++) {
            if(nrs[j] <= pivot){
                i++;
                int temp = nrs[i];
                nrs[i] = nrs[j];
                nrs[j] = temp;
            }
        }
        int temp = nrs[i+1];
        nrs[i+1] = nrs[hi];
        nrs[hi] = temp;

        return i+1;
    }
}
