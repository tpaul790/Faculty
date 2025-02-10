package factory;
import domain.*;

import java.util.ArrayList;

public class ExpressionFactory {
    private static ExpressionFactory instance = null;
    private Operation operation;
    private ArrayList<NumarComplex> args;

    private ExpressionFactory(Operation operation, ArrayList<NumarComplex> args) {
        this.operation = operation;
        this.args = args;
    }

    public static ExpressionFactory getInstance(Operation operation, ArrayList<NumarComplex> args) {
        if(instance == null){
            instance = new ExpressionFactory(operation, args);
        }
        return instance;
    }

    public ComplexExpression createExpression(){
        if(operation.equals(Operation.ADDITION)){
            return new OperatieAdunare(args);
        }else if(operation.equals(Operation.SUBTRACTION)){
            return new OperatieScadere(args);
        }else if(operation.equals(Operation.MULTIPLICATION)){
            return new OperatieInmultire(args);
        }else if(operation.equals(Operation.DIVISION)){
            return new OperatieImpartire(args);
        }else{
            return null;
        }
    }

}
