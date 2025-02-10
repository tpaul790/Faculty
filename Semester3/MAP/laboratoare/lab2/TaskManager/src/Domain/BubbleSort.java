package Domain;

public class BubbleSort extends AbstractSorter{

    @Override
    public void sort(int[] nrs) {
        boolean sortat = false;
        while(!sortat){
            sortat = true;
            for (int i = 0; i < nrs.length - 1; i++) {
                if(nrs[i]>nrs[i+1]){
                    int temp = nrs[i];
                    nrs[i] = nrs[i+1];
                    nrs[i+1] = temp;
                    sortat = false;
                }
            }
        }
        System.out.print("Sirul sortat folosid buubble sort este: ");
        for(int i=0;i<nrs.length;i++){
            System.out.print(nrs[i]+" ");
        }
    }
}
