import java.io.*;
import java.net.*;
import java.util.Arrays;

public class Main{
    public static int removeDuplicates(int[] nums) {
        int ant = nums[0];
        int n = nums.length;
        for(int i=1;i<n;i++){
            int k=i;
            while(i<n && ant == nums[i])
                i++;
            if(i-k>0){
                for(int j=i;j<n;j++)
                    nums[j-i+k] = nums[j];
                n = n-(i-k);
                i=k-1;
            }
            ant = nums[i];
        }
        return n;
    }

    public static void main(String[] args) {
//        Server.runServer();
    }


}