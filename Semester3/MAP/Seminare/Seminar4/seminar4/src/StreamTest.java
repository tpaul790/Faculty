import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class StreamTest {
    public static <E> List<E> filterGeneric(List<E> lista, Predicate<E> p) {
        return lista.stream().filter(p).collect(Collectors.toList());
    }

    public static <E> List<E> filterGeneric(List<E> lista, Predicate<E> p, Comparator<E> c){
        return lista
                .stream()
                .filter(p)
                .sorted(c)
                .collect(Collectors.toList());
    }

    public static void main(String[] args){
        List<Integer> lista = new ArrayList<>();
        lista.add(4);
        lista.add(5);
        lista.add(6);
        lista.add(12);
        lista.add(423);
        lista.add(42);
        Predicate<Integer> predicate = i -> i%2 == 0;
        System.out.println(filterGeneric(lista,predicate));
        Comparator<Integer> comparator = Integer::compare;
        System.out.println(filterGeneric(lista,predicate,comparator.reversed()));
    }
}
