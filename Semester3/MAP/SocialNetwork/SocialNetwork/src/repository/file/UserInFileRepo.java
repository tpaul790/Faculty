package repository.file;

import domain.Friendship;
import domain.Tuple;
import domain.User;
import domain.validation.RepoException;
import domain.validation.UserValidator;
import domain.validation.ValidationException;
import domain.validation.Validator;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class UserInFileRepo extends AbstratFileRepo<Integer, User> {

    public UserInFileRepo(Validator<User> validator, String fileName) {
        super(validator, fileName);
    }

    @Override
    public User entityFromString(String line) {
        Function<String[],User> stringListToUser = fields ->{
            int id = Integer.parseInt(fields[0]);
            String name = fields[1];
            String password = fields[2];
            String email = fields[3];
            String role = fields[4];
            return new User(id, name, password, email, role);
        };
        String[] fields = line.split(",");
        return stringListToUser.apply(fields);
    }

    @Override
    public String entityToString(User user) {
        return user.getId()+","+user.getUsername()+","+user.getEmail()+","+user.getPassword()+","+user.getRole();
    }

    @Override
    public String makeHeader() {
        return "Id,Username,Email,Password,Role";
    }

//    /**
//     * Load for every user, that take part of a friendship, his friends or his friend requests
//     * @param friendships - an iterable object that contains all friendships
//     */
//    public void loadFriends(Iterable<Friendship> friendships){
//        for(Friendship friendship : friendships) {
//            Optional<User> optionalUser1 = findOne(friendship.getId().getFirst());
//            Optional<User> optionalUser2 = findOne(friendship.getId().getSecond());
//            Predicate<Friendship> acceptedFriendship = f -> f.getStatus().equals("accepted");
//            if(acceptedFriendship.test(friendship)) {
//                if(optionalUser1.isPresent() && optionalUser2.isPresent()) {
//                    optionalUser1.get().getFriends().add(optionalUser2.get());
//                    optionalUser2.get().getFriends().add(optionalUser1.get());
//                }
//            }else if(acceptedFriendship.negate().test(friendship)) {
//                optionalUser2.ifPresent(x -> x.getFriendRequests().add(friendship));
//            }
//        }
//    }
//
//    /**
//     *
//     * @param user1 - first user
//     * @param user2 - second user
//     * @throws domain.validation.ValidationException - if one of the users are invalid
//     */
//    public void addFriend(User user1, User user2) throws IllegalArgumentException {
//        super.getValidator().validate(user1);
//        super.getValidator().validate(user2);
//        user1.getFriends().add(user2);
//        user2.getFriends().add(user1);
//    }
//
//    /**
//     *
//     * @param user1 - first user
//     * @param user2 - second user
//     * @throws domain.validation.ValidationException - if one of the users are invalid
//     */
//    public void removeFriend(User user1, User user2) throws ValidationException {
//        super.getValidator().validate(user1);
//        super.getValidator().validate(user2);
//        user1.getFriends().remove(user2);
//        user2.getFriends().remove(user1);
//    }
//
//
//    /**
//     *
//     * @param friendRequest - friendship we want to add
//     * @throws RepoException - if the friendship already exist
//     */
//    public void addFriendRequest(Friendship friendRequest) throws RepoException {
//        serchForFriends(friendRequest);
//        Optional<User> optionalReceiver = findOne(friendRequest.getId().getSecond());
//        if(optionalReceiver.isPresent()) {
//            if (optionalReceiver.get().getFriendRequests().contains(friendRequest)) {
//                throw new RepoException("Friend request already exists");
//            }
//            optionalReceiver.get().getFriendRequests().add(friendRequest);
//        }
//    }
//
//
//    public void removeFriendRequest(Friendship friendRequest) throws RepoException {
//        serchForFriends(friendRequest);
//        Optional<User> optionalReceiver = findOne(friendRequest.getId().getSecond());
//        if(optionalReceiver.isPresent()) {
//            if (!optionalReceiver.get().getFriendRequests().contains(friendRequest)) {
//                throw new RepoException("Friend request not found");
//            }
//            optionalReceiver.get().getFriendRequests().remove(friendRequest);
//        }
//    }
//
//    /**
//     * Check if the friendship is valid, if the users that make the friendship exist
//     * @param friendRequest - friendship that we want to check
//     * @throws RepoException - if one of the users isn't find
//     */
//    private void serchForFriends(Friendship friendRequest) throws RepoException {
//        Optional<User> sender = findOne(friendRequest.getId().getFirst());
//        Optional<User> receiver = findOne(friendRequest.getId().getSecond());
//        Predicate<Optional<User>> optionalNullUser = Optional::isEmpty;
//        if(optionalNullUser.test(sender)) {
//            throw new RepoException("Sender not found");
//        }
//        if(optionalNullUser.test(receiver)) {
//            throw new RepoException("Receiver not found");
//        }
//    }
//
//    public List<User> getAllFriends(User user) {
//        return user.getFriends();
//    }
}
