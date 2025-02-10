package domain;

import java.util.ArrayList;

public class OperatieAdunare extends ComplexExpression{

    public OperatieAdunare(ArrayList<NumarComplex> args){
        super(args);
    }

    @Override
    public NumarComplex excuteOneOperation(NumarComplex nr1, NumarComplex nr2) {
        return nr1.adunare(nr2);
    }
}
