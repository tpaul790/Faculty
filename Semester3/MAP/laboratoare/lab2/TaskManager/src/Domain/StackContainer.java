package Domain;

import java.util.ArrayList;

public class StackContainer extends AbstractContainer {

    public StackContainer() {
        super();
    }

    StackContainer(Task[] tasks) {}

    @Override
    public Task remove() {
        if(!super.getTasks().isEmpty()) {
            return super.getTasks().removeLast();
        }
        return null;
    }

}
