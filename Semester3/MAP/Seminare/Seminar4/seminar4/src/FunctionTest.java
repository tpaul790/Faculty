import java.util.function.Function;

public class FunctionTest {
    public static void main(String[] args) {
        Function<String,Integer> stringToInteger = x -> Integer.valueOf(x);
        Function<String,Integer> stringToInteger2 = Integer::valueOf;

        System.out.println(stringToInteger.apply("12"));
        System.out.println(stringToInteger2.apply("13"));

        Function<Integer,Integer> square = x -> x * x;
        System.out.println(stringToInteger2.andThen(square).apply("12"));
    }
}
