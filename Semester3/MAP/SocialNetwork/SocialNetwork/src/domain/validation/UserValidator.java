package domain.validation;

import domain.User;

public class UserValidator implements Validator<User> {
    private static UserValidator instance = null;

    private UserValidator(){}

    public static UserValidator getInstance(){
        if(instance == null){
            instance = new UserValidator();
        }
        return instance;
    }

    @Override
    public void validate(User user) throws ValidationException {
        StringBuilder builder = new StringBuilder();
        if(user.getUsername().length()<5)
            builder.append("Username should be at least 5 characters!\n");
        if(user.getPassword().length()<4)
            builder.append("Password should be at least 4 characters!\n");
        if(!user.getEmail().contains("@gmail.com"))
            builder.append("Email isn't valid!\n");
        if(!user.getRole().equals("admin") && !user.getRole().equals("member"))
            builder.append("Role should be 'admin' or 'member'!\n");
        if(!builder.isEmpty())
            builder.delete(builder.length()-1, builder.length());
        if(!builder.toString().isEmpty())
            throw new ValidationException(builder.toString());
    }
}
