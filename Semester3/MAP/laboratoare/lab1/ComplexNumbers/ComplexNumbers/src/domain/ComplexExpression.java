package domain;

import java.util.ArrayList;

public abstract class ComplexExpression {
    private ArrayList<NumarComplex> args;

    public ComplexExpression( ArrayList<NumarComplex> args) {
        this.args = args;
    }

    public abstract NumarComplex excuteOneOperation(NumarComplex nr1, NumarComplex nr2);

    public NumarComplex execute() {
        NumarComplex result = args.removeFirst();
        while(!args.isEmpty()) {
            result = excuteOneOperation(result, args.removeFirst());
        }
        return result;
    }

}
