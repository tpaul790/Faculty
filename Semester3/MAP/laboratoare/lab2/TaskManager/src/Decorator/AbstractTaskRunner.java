package Decorator;

import Domain.Task;

public abstract class AbstractTaskRunner implements TaskRunner {
    private TaskRunner taskRunner;

    public AbstractTaskRunner(TaskRunner taskRunner) {
        this.taskRunner = taskRunner;
    }

    @Override
    public void executeOneTask() {
        taskRunner.executeOneTask();
    }

    @Override
    public void executeAll() {
        while(hasTask())
            executeOneTask();
    }

    @Override
    public void addTask(Task task) {
        taskRunner.addTask(task);
    }

    @Override
    public boolean hasTask() {
        return taskRunner.hasTask();
    }
}
