namespace Sem10;
public interface Container {
    Task remove();
    void add(Task task);
    int size();
    bool isEmpty();
}