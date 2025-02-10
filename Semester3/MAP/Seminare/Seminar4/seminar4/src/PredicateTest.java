import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class PredicateTest {
    public static <E> void print(List<E> list, Predicate<E> predicate) {
        list.forEach(x->{
            if(predicate.test(x))
                System.out.println(x);
        });
    }

    public static void main(String[] args){
        Circle c1 = new Circle(2);
        Circle c2 = new Circle(3);
        Circle c3 = new Circle(4);
        Circle c4 = new Circle(5);
        Circle c5 = new Circle(6);

        List<Circle> list = new ArrayList<>();
        list.add(c1);
        list.add(c2);
        list.add(c3);
        list.add(c4);
        list.add(c5);

        Predicate<Circle> predicate = x-> x.getRadius()<5;
        Predicate<Circle> predicate1 = predicate.negate();
        Predicate<Circle> predicate2 = predicate.and(x -> x.getRadius()%2==0);

        print(list, predicate);
        System.out.println();
        print(list, predicate1);
        System.out.println();
        print(list, predicate2);
    }
}
