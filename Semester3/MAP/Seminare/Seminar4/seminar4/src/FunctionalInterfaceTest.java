import java.util.List;

public class FunctionalInterfaceTest {
    public static <E> void printArie(List<E> list, Arie<E> f){
        list.forEach(x -> System.out.println(f.calculate(x)));
    }

    public static void main(String[] args){
        Arie<Circle> arieCerc = x -> Math.PI*x.getRadius()*x.getRadius();
        Circle c1 = new Circle(2);
        Circle c2 = new Circle(3);
        Circle c3 = new Circle(4);
        Circle c4 = new Circle(5);

        List<Circle> list = List.of(c1, c2, c3, c4);

        printArie(list, arieCerc);

        Arie<Square> ariePatrat = x -> Math.pow(x.getRow(),2);
        Square s1 = new Square(5);
        Square s2 = new Square(6);
        Square s3 = new Square(7);
        Square s4 = new Square(8);
        Square s5 = new Square(9);

        List<Square> list2 = List.of(s1, s2, s3, s4);

        printArie(list2, ariePatrat);
    }
}
