package factory;

import com.sun.nio.sctp.InvalidStreamException;
import domain.ComplexExpression;
import domain.NumarComplex;
import domain.Operation;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressionParser {
    String[] args;
    StringBuilder expression;

    public ExpressionParser(String[] args) {
        this.args = args;
        expression = new StringBuilder();
        for(String arg : args) {
            expression.append(arg).append(" ");
        }
    }

    public boolean isValid(){
        String regex = "^( ?-?([1-9]*[0-9]+)?[+-]?([1-9]*[0-9]+)?[*]?i( [+-/*])?)+ ?$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(expression.toString());

        return matcher.matches();
    }

    public ComplexExpression parse(){
        if(isValid()){
            //construiesc vectorul de numere complexe
            ArrayList<NumarComplex> numbers = new ArrayList<>();
            for(int i=0;i<args.length;i+=2){
                numbers.add(new NumarComplex(args[i]));
            }
            //gasesc tipul de operator
            String operator = args[1];
            Operation operation = switch (operator) {
                case "+" -> Operation.ADDITION;
                case "-" -> Operation.SUBTRACTION;
                case "*" -> Operation.MULTIPLICATION;
                case "/" -> Operation.DIVISION;
                default -> null;
            };
            ExpressionFactory factory = ExpressionFactory.getInstance(operation, numbers);
            return factory.createExpression();
        }else{
            throw new InvalidStreamException("Expresie invalida");
        }
    }
}
