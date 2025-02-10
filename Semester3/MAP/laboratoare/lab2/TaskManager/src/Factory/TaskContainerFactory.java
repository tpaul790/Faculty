package Factory;

import Domain.Container;
import Domain.QuequeContainer;
import Domain.StackContainer;
import Domain.Task;
import Factory.Strategy;

public class TaskContainerFactory implements Factory{
    private static TaskContainerFactory instance = null;

    private TaskContainerFactory() {}

    @Override
    public Container createContainer(Strategy strategy) {
        if(strategy == Strategy.LIFO) {
            return new StackContainer();
        }
        else if(strategy == Strategy.FIFO) {
            return new QuequeContainer();
        }
        return null;
    }

    public static TaskContainerFactory getInstance() {
        if(instance == null){
            instance = new TaskContainerFactory();
        }
        return instance;
    }
}
