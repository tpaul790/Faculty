import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        List<Circle> circles = new ArrayList<>();
        double r = Math.random();
        while(r>=0.01){
            Circle c = new Circle(r);
            circles.add(c);
            r = Math.random();
        }
        int cnt = 1;
        for (Circle c : circles) {
            System.out.println("Aria cercului " + cnt + " este " + c.getArea());
            cnt++;
        }

        Map<String,String> angajati = new HashMap<>();
        angajati.put("a1234","Steve Jobs");
        angajati.put("a1235","Scot McNealy");
        angajati.put("a1236","Jeff Bezos");
        angajati.put("a1237","Lary Ellison");
        angajati.put("a1238","Bill Gates");
        afisare(angajati,"A1234");
    }

    public static void afisare(Map<String,String> angajati, String key){
        key = key.toLowerCase();
        System.out.println(angajati.get(key));
    }
}