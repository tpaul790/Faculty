package domain;

import java.util.ArrayList;

public class OperatieScadere extends ComplexExpression{

    public OperatieScadere(ArrayList<NumarComplex> args){
        super(args);
    }

    @Override
    public NumarComplex excuteOneOperation(NumarComplex nr1, NumarComplex nr2) {
        return nr1.scadere(nr2);
    }
}
