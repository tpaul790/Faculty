package domain.validator;

import domain.Employee;
import domain.Task;

import java.time.LocalDateTime;

public class TaskValidator extends Validator<Task> {

    @Override
    public String validate(Task entity) {
        StringBuilder errors = new StringBuilder();
        errors.append(validateDescription(entity.getDescription()));
        errors.append("\n");
        errors.append(validateAssignedEmployee(entity.getAssignetEmployee()));
        errors.append("\n");
        errors.append(validateDates(entity.getCreateTime(),entity.getSolveTime()));
        return errors.toString();
    }

    private String validateDescription(String description) {
        if (description == null)
            return "";
        if(description.isEmpty())
            return "Description is empty";
        if(description.length() <= 10)
            return "Description should be more than 10 characters";
        return "";
    }

    private String validateAssignedEmployee(Employee assignedEmployee) {
        if (assignedEmployee == null)
            return "";
        if(assignedEmployee.getId() < 0)
            return "The id of the assigned employee can not be negative";
        return "";
    }

    private String validateDates(LocalDateTime createTime, LocalDateTime solveTime) {
        if(createTime == null || solveTime == null) {
            return "";
        }
        if(createTime.isAfter(solveTime)) {
            return "The date of the solve time can not be before the created time";
        }
        return "";
    }
}
