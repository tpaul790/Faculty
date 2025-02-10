package service;

import domain.Friendship;
import domain.Tuple;
import domain.User;
import domain.validation.ServiceException;
import repository.database.FriendshipDbRepo;
import repository.database.UserDbRepo;
import repository.file.FriendshipInFileRepo;
import repository.file.UserInFileRepo;
import utils.Utils;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;

public class FriendshipService extends Service<Tuple<Integer,Integer>,Friendship,FriendshipDbRepo> {
    private UserDbRepo userDbRepo;

    public FriendshipService(FriendshipDbRepo repository, UserDbRepo userDbRepo) {
        super(repository);
        this.userDbRepo = userDbRepo;
    }


    public void delete(int id1, int id2) throws ServiceException {
        Optional<Friendship> rez = super.getRepository().delete(new Tuple<>(id1,id2));
        if(rez.isEmpty()){
            throw new ServiceException("No friendship found");
        }
    }

    public void sendFriendRequest(int id1, int id2) throws ServiceException {
        Friendship friendship = new Friendship(new Tuple<>(id1,id2),"pending");
        Optional<Friendship> opFriendship = getRepository().save(friendship);
        if(opFriendship.isPresent())
            throw new ServiceException("Friendship already exists");
    }


    /**
     * The friend request that the user with id userId1 send to the user with id userId2 will be accepted
     * and this two users will become friends
     *
     * @param userId1 - id of the first user
     * @param userId2 - id of the second user
     */
    public void acceptFriendRequest(int userId1, int userId2) {

    }

    /**
     * The friend request that the user with id userId1 send to the user with id userId2 will be declined
     * @param userId1 - id of the first user
     * @param userId2 - id of the second user
     */
    public void declineFriendRequest(int userId1, int userId2){

    }

    /**
     *
     * @param id - the id of the user for which I want to accept all friend requests
     * @return - the number of friend requests that was accepted
     * @throws ServiceException - if user isn't t find or if there aren't t friend requests
     */
    public int acceptAllFriendRequests(int id) throws ServiceException {
        Set<Friendship> friendshipSet = (Set<Friendship>) getRepository().findAllFriendRequests(id);
        if(friendshipSet.isEmpty()) {
            throw new ServiceException("You don't have friend requests!");
        }
        int size = friendshipSet.size();
        for(Friendship friendship : friendshipSet) {
            friendship.setStatus("accepted");
            friendship.setAcceptDate(LocalDate.now());
            getRepository().update(friendship);
        }
        friendshipSet.clear();
        return size;
    }

    /**
     *
     * @param id - the id of the user for which I want to decline all friend requests
     * @return - the number of friend requests that was declined
     * @throws ServiceException - if there aren't friend requests
     */
    public void declineAllFriendRequests(int id) throws ServiceException {
        getRepository().deleteAllFriendRequests(id);
    }

    public Iterable<User> getAllFriends(int id) throws ServiceException {
        return getRepository().findAllFriends(id);
    }

    /**
     * Calculate the number of friendships between a set of users
     * @param users - the set of users
     * @return - the number of friendships
     */
    public int numberOfFriendships(Set<User> users){
        int rez = 0;
        for(User user : users){
            rez += getRepository().numberOfFriends(user.getId());
        }
        return rez/2;
    }

    /**
     * Count the number of connected components from the friendship graph
     * @return the number of connected components
     */
    public int numberOfSocialComunities(){
        Map<Integer,Boolean> visited = new HashMap<>();
        int rez = 0;

        for(User user : userDbRepo.findAll()){
            if(visited.get(user.getId()) == null) {
                rez++;
                Stack<Integer> stack = new Stack<>();
                stack.push(user.getId());
                while (!stack.isEmpty()) {
                    int curent = stack.pop();
                    visited.put(curent, true);
                    for(User friend : getRepository().findAllFriends(curent)){
                        if(visited.get(friend.getId()) == null) {
                            stack.push(friend.getId());
                        }
                    }
                }
            }
        }
        return rez;
    }

    /**
     * Find the most social comunity, with maximum friendships
     * @return - a set with all users that are part of the most social comunity
     */
    public Set<User> mostSocialComunity(){
        Map<User,Boolean> visited = new HashMap<>();
        Set<User> comunity = new HashSet<>();
        int maxi = -1;

        for(User user : userDbRepo.findAll()){
            if(visited.get(user) == null) {
                Set<User> users = new HashSet<>();
                Stack<User> stack = new Stack<>();
                stack.push(user);
                while (!stack.isEmpty()) {
                    User curent = stack.pop();
                    users.add(curent);
                    visited.put(curent, true);
                    for(User friend : getRepository().findAllFriends(curent.getId())){
                        if(visited.get(friend) == null) {
                            stack.push(friend);
                        }
                    }
                    System.out.println(curent);
                }
                int nrOfFriendships = numberOfFriendships(users);
                if(nrOfFriendships>maxi){
                    maxi = nrOfFriendships;
                    comunity = users;
                }
            }

        }
        return comunity;
    }
}
