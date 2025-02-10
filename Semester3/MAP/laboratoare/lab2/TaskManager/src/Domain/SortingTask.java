package Domain;

public class SortingTask extends Task {
    private int[] nrs;
    AbstractSorter sorter;

    public SortingTask(String taskId, String description, int[] nrs, AbstractSorter sorter) {
        super(taskId, description);
        this.nrs = nrs;
        this.sorter = sorter;
    }

    @Override
    public void execute() {
        sorter.sort(nrs);
    }
}
