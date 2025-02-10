package service;

import domain.User;
import exception.RepoException;
import repo.Network;
import utils.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NetworkService {
    private Network network;

    public NetworkService(Network network) {
        this.network = network;
    }

    public void addUser(int id, String username, String email, String password, String status) throws RepoException {
        User user = new User(id, username, email, password, status);
        network.add(user);
    }

    public void removeUser(int id) throws RepoException {
        User user = network.findById(id);
        network.remove(user);
    }

    public void updateUser(int id, String username, String email, String password, String status) throws RepoException {
        User user = network.findById(id);
        User newUser = new User(id, username, email, password, status);
        network.update(user, newUser);
    }

    public User findUser(int id) throws RepoException {
        User user = network.findById(id);
        return user;
    }

    public void sendFriendRequest(int id1, int id2) throws RepoException {
        User user1 = findUser(id1);
        User user2 = findUser(id2);
        user1.sendFriendRequest(user2);
    }

    public void acceptAllFriendRequests(int id) throws RepoException {
        User user = findUser(id);
        user.acceptAllFriendRequests();
    }

    public void declineAllFriendRequests(int id) throws RepoException {
        User user = findUser(id);
        user.declineAllFriendRequests();
    }

    public Set<User> getAllFriends(int id) throws RepoException {
        User user = findUser(id);
        return user.getFriendsSet();
    }

    /*
    Functia atribuie pentru fiecare User din multimea de useri un cod unic din [1,n], unde n este numarul de useri
    Se returneaza un map in care cheia este userul iar valoarea este codul unic
    Aceste coduri reprezinta, pentru fiecare user, nodul care ii corespunde in graf
     */
    public Map<User,Integer> getCodification(){
        Map<User,Integer> map = new HashMap<>();
        int cnt = -1;
        for(User user : getAllUsers()){
            cnt++;
            map.put(user,cnt);
        }
        return map;
    }

    /*
    Functia returneaza matricea de adiacenta a prieteniilor bazandu se pe codificare
    facuta de functia getCodification()
     */
    public int[][] getFriendshipsAdjMatrix(){
        int n = getAllUsers().size();
        int[][] matrix = new int[n][n];
        Map<User,Integer> codification = getCodification();
        for(User user : getAllUsers()){
            int i = codification.get(user);
            for(User friend : user.getFriendsSet()){
                int j = codification.get(friend);
                matrix[i][j] = 1;
            }
        }
        return matrix;
    }

    public int numberOfSocialComunities(){
        return Utils.numberOfConnectedComponents(getFriendshipsAdjMatrix());
    }

    public Set<User> getAllUsers(){
        return network.getAll();
    }
}
