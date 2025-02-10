package domain;

import java.util.ArrayList;

public class OperatieImpartire extends ComplexExpression{

    public OperatieImpartire(ArrayList<NumarComplex> args){
        super(args);
    }

    @Override
    public NumarComplex excuteOneOperation(NumarComplex nr1, NumarComplex nr2) {
        return nr1.impartire(nr2);
    }
}
