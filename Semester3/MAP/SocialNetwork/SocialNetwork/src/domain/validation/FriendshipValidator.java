package domain.validation;

import domain.Friendship;
import service.FriendshipService;

public class FriendshipValidator implements Validator<Friendship> {
    private static FriendshipValidator instance = null;

    private FriendshipValidator() {};

    public static FriendshipValidator getInstance() {
        if (instance == null) {
            instance = new FriendshipValidator();
        }
        return instance;
    }

    @Override
    public void validate(Friendship friendship) throws ValidationException {
        StringBuilder builder = new StringBuilder();
        if(friendship.getId() == null)
            builder.append("Friendship id can't be null!\n");
        if(friendship.getId().getFirst().equals(friendship.getId().getSecond()))
            builder.append("You can't be friend with yourself!\n");
        if(friendship.getAcceptDate() != null && friendship.getAcceptDate().isBefore(friendship.getCreateDate()))
            builder.append("You can't accept a friend request before creating it!\n");
        if(!friendship.getStatus().equals("accepted") && !friendship.getStatus().equals("pending"))
            builder.append("Status should be 'accepted' or 'pending'!\n");
        if(!builder.isEmpty())
            builder.delete(builder.length()-1, builder.length());
        if(!builder.toString().isEmpty())
            throw new ValidationException(builder.toString());
    }
}
