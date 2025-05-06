package repository;

import domain.Task;
import domain.validator.RepoException;

import java.util.Optional;

public interface ITaskRepository extends Repository<Integer, Task> {
    Optional<Task> update(Task task) throws RepoException;
}
