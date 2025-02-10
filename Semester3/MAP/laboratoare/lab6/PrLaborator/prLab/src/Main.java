import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        problema1();

        problema2(createDogList());

        problema3(createIntegerList());

        problema4(createMallList());
    }

    public static List<Dog> createDogList(){
        List<Dog> dogs = new ArrayList<>();
        dogs.add(new Dog("Sam",11));
        dogs.add(new Dog("Bob",12));
        dogs.add(new Dog("Jack",6));
        dogs.add(new Dog("Jhon",14));
        dogs.add(new Dog("Dizi",4));
        return dogs;
    }

    public static List<Integer> createIntegerList(){
        List<Integer> integers = new ArrayList<>();
        integers.add(1);
        integers.add(2);
        integers.add(3);
        integers.add(4);
        integers.add(5);
        return integers;
    }

    public static List<Mall> createMallList(){
        List<Mall> malls = new ArrayList<>();
        malls.add(new Mall("Iulius","Boc",2001,1200));
        malls.add(new Mall("Pallas","Iohani",2002,730));
        malls.add(new Mall("Vivo","Emil",2007,840));
        malls.add(new Mall("Misha","Petrica",1999,905));
        return malls;
    }

    public static void problema1(){
        System.out.println("Problema1");
        List<String> words = new ArrayList<>();
        words.add("ana");
        words.add("are");
        words.add("mere");
        words = words.stream()
                .map(String::toUpperCase)
                .toList();
        words.forEach(System.out::println);
    }

    public static void problema2(List<Dog> dogs){
        System.out.println();
        System.out.println("Problema2 -a)");
        dogs.stream()
                .filter(dog -> dog.getAge()>10)
                .forEach(System.out::println);

        System.out.println();
        System.out.println("Problema2 -b)");
        Comparator<Dog> comparator = Comparator.comparingInt(Dog::getAge);
        dogs.sort(comparator);
        dogs.forEach(System.out::println);


        System.out.println();
        System.out.println("Problema2 -c)");
        dogs.stream()
                .filter(dog -> dog.getName().equals("Sam"))
                .findFirst()
                .ifPresent(System.out::println);

        System.out.println();
        System.out.println("Problema2 -d)");
        dogs.forEach(x -> System.out.println(x.getName()));
    }

    public static void problema3(List<Integer> integers){
        System.out.println();
        System.out.println("Problema3");
        List<Integer> newList;
        Function<Integer,Integer> function = x -> x*x;
        newList = integers.stream()
                .map(function)
                .toList();
        newList.forEach(x -> System.out.print(x+" "));
    }

    public static void problema4(List<Mall> malls){
        System.out.println();
        System.out.println("Problema4");
        malls.forEach(x -> x.setProfit(x.getProfit()*3));
        Optional<Mall> mall = malls.stream()
                .max(Comparator.comparingDouble(m -> m.getProfit()*3));

        mall.ifPresent(System.out::println);

    }
}