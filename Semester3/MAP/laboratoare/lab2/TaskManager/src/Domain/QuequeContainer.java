package Domain;

import java.util.ArrayList;

public class QuequeContainer extends AbstractContainer {

    public QuequeContainer(){
        super();
    }

    @Override
    public Task remove() {
        if(!super.getTasks().isEmpty()){
            return super.getTasks().removeFirst();
        }
        return null;
    }

}
