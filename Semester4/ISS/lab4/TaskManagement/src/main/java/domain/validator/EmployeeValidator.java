package domain.validator;

import domain.Employee;

public class EmployeeValidator extends Validator<Employee> {
    @Override
    public String validate(Employee entity) {
        StringBuilder errs = new StringBuilder();
        errs.append(validateName(entity.getName()));
        errs.append("\n");
        errs.append(validateUsername(entity.getUsername()));
        errs.append("\n");
        errs.append(validatePassword(entity.getPassword()));
        return errs.toString();
    }

    private String validateName(String name) {
        if (name == null)
            return "";
        if(name.isEmpty())
            return  "Name cannot be empty";
        if(name.length() < 4 || name.length() > 10)
            return  "Name must be between 4 and 10 characters";
        return "";
    }

    private String validateUsername(String username) {
        if(username == null)
            return "";
        if (username.isEmpty())
            return  "Username cannot be empty";
        if(username.length() < 4 || username.length() > 10)
            return  "Username must be between 4 and 10 characters";
        return "";
    }

    private String validatePassword(String password) {
        if(password == null)
            return "";
        if (password.isEmpty()) {
            return  "Password cannot be empty";
        }
        if (password.length() < 4 || password.length() > 16)
            return  "Password must be between 4 and 16 characters";
        return "";
    }
}
