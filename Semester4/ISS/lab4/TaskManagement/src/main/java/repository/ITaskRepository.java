package repository;

import domain.Employee;
import domain.Task;

public interface ITaskRepository extends Repository<Integer, Task> {
    void setAllAssignedIdToNull(Employee employee);
}
