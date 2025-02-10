package Domain;

public interface Container {
    Task remove();
    void add(Task task);
    boolean isEmpty();
    int size();
}
