import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ComparatorTest {
    public static void main(String[] args) {
        Comparator<Integer> comparator = (x,y) -> Integer.compare(x, y);
        Comparator<Integer> comparator2 = Integer::compare;

        List<Integer> numere = Arrays.asList(5, 4, 7, 2, 3, 5, 9, 6);
        numere.sort(comparator);
        System.out.println(numere);
        numere.sort(comparator2.reversed());
        System.out.println(numere);
    }
}
