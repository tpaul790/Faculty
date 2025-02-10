package Domain;

import java.util.ArrayList;

public abstract class AbstractContainer implements Container{
    private ArrayList<Task> tasks = new ArrayList<>();

    public abstract Task remove();

    @Override
    public void add(Task task) {
        tasks.add(task);
    }

    @Override
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    @Override
    public int size() {
        return tasks.size();
    }

    protected ArrayList<Task> getTasks() {
        return tasks;
    }
}
