package domain;

import java.util.ArrayList;

public class OperatieInmultire extends ComplexExpression{

    public OperatieInmultire(ArrayList<NumarComplex> args){
        super(args);
    }

    @Override
    public NumarComplex excuteOneOperation(NumarComplex nr1, NumarComplex nr2) {
        return nr1.inmultire(nr2);
    }


}
