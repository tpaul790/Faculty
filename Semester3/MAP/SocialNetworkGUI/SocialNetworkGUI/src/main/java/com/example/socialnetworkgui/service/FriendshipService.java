package com.example.socialnetworkgui.service;

import com.example.socialnetworkgui.domain.*;
import com.example.socialnetworkgui.domain.validation.ServiceException;
import com.example.socialnetworkgui.repository.database.FriendshipDbRepo;
import com.example.socialnetworkgui.repository.database.UserDbRepo;
import com.example.socialnetworkgui.utils.events.ChangeEventType;
import com.example.socialnetworkgui.utils.events.FriendshipEntityChangeEvent;
import com.example.socialnetworkgui.utils.events.FriendshipEventType;
import com.example.socialnetworkgui.utils.observer.Observable;
import com.example.socialnetworkgui.utils.observer.Observer;


import java.time.LocalDate;
import java.util.*;

public class FriendshipService extends Service<Tuple<Integer,Integer>,Friendship,FriendshipDbRepo> implements Observable<FriendshipEntityChangeEvent> {
    private UserDbRepo userDbRepo;
    private List<Observer<FriendshipEntityChangeEvent>> observers;

    public FriendshipService(FriendshipDbRepo repository, UserDbRepo userDbRepo) {
        super(repository);
        this.userDbRepo = userDbRepo;
        this.observers = new ArrayList<>();
    }


    public void delete(int id1, int id2) throws ServiceException {
        Optional<Friendship> rez = super.getRepository().delete(new Tuple<>(id1,id2));
        if(rez.isEmpty()){
            throw new ServiceException("No friendship found");
        }
        notify(new FriendshipEntityChangeEvent(FriendshipEventType.DELETE, rez.get()));
    }

    public void sendFriendRequest(int id1, int id2) throws ServiceException {
        Friendship friendship = new Friendship(new Tuple<>(id1,id2),"pending");
        Optional<Friendship> opFriendship = getRepository().save(friendship);
        if(opFriendship.isPresent())
            throw new ServiceException("Friendship already exists");
        notify(new FriendshipEntityChangeEvent(FriendshipEventType.SEND, friendship));
    }


    /**
     * The friend request that the user with id userId1 send to the user with id userId2 will be accepted
     * and this two users will become friends
     *
     * @param userId1 - id of the first user
     * @param userId2 - id of the second user
     */
    public void acceptFriendRequest(int userId1, int userId2) {
        Optional<Friendship> opFriendship = getRepository().findOne(new Tuple<>(userId1,userId2));
        if(opFriendship.isEmpty()){
            throw new ServiceException("No friendship found");
        }
        Friendship newFriendship = new Friendship(opFriendship.get().getId(),opFriendship.get().getCreateDate(),LocalDate.now(),"accepted");
        getRepository().update(newFriendship);
        notify(new FriendshipEntityChangeEvent(FriendshipEventType.ACCEPT, newFriendship,opFriendship.get()));
    }

    /**
     * The friend request that the user with id userId1 send to the user with id userId2 will be declined
     * @param userId1 - id of the first user
     * @param userId2 - id of the second user
     */
    public void declineFriendRequest(int userId1, int userId2){
        Optional<Friendship> opFriendship = getRepository().findOne(new Tuple<>(userId1,userId2));
        if(opFriendship.isEmpty()){
            throw new ServiceException("No friendship found");
        }
        getRepository().delete(opFriendship.get().getId());
        notify(new FriendshipEntityChangeEvent(FriendshipEventType.DECLINE, opFriendship.get()));
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

    public Page<User> getAllFriendsOnPage(int id, Pageable pageable) throws ServiceException {
        return getRepository().findAllFriendsOnPage(id, pageable, null);
    }

    public int numberOfFriends(int id) throws ServiceException {
        return getRepository().numberOfFriends(id);
    }

    public Iterable<Friendship> getAllFriendRequests(int id){
        return super.getRepository().findAllFriendRequests(id);
    }

    public Iterable<User> getAllUsersThatAreNotFriends(Integer id) {
        Set<User> allUsers = (Set<User>) userDbRepo.findAll();
        Set<User> friends = (Set<User>) getRepository().findAllFriends(id);
        Set<Friendship> friendRequests = (Set<Friendship>) getRepository().findAllFriendRequests(id);
        Set<Friendship> allFriendRequests = (Set<Friendship>) getRepository().findAll();
        allUsers.removeIf(user -> {
            //userul conectat se sterge
            if(user.getId().equals(id))
                return true;
            //userii la care le-am trimis eu cereri se sterg
            for(Friendship friendRequest : allFriendRequests)
                if(friendRequest.getId().getFirst().equals(id) && friendRequest.getId().getSecond().equals(user.getId()))
                    return true;
            //tot useri de la care am cererei se sterg
            for(Friendship friendship : friendRequests)
                if(friendship.getId().getFirst().equals(user.getId()))
                    return true;
            //userii care imi sunt prieteni se sterg
            for(User friend : friends)
               if(user.getId().equals(friend.getId()))
                   return true;
           return false;
        });
        return allUsers;
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

    @Override
    public void addObserver(Observer<FriendshipEntityChangeEvent> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<FriendshipEntityChangeEvent> observer) {
        observers.remove(observer);
    }

    @Override
    public void notify(FriendshipEntityChangeEvent event) {
        observers.forEach(observer -> observer.update(event));
    }
}
