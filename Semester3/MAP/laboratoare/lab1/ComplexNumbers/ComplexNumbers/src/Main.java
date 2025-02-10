import domain.*;
import factory.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        if(args.length == 0) {
            System.out.println("Va rog sa introduceti o expresie de numere complexe!");
        }else{
            ExpressionParser parser = new ExpressionParser(args);
            if(parser.isValid()){
                ComplexExpression expression = parser.parse();
                System.out.println("Rezultatul expresiei este: " + expression.execute().toString());
            }else{
                System.out.println("Expresia nu este valida!");
            }
        }
    }
}