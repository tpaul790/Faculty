package domain;

import java.util.*;

public class User {
    private int id;
    private String username;
    private String email;
    private String password;
    private String role;
    private final Set<Friendship> friendships;
    private final List<Friendship> friendRequests;

    public User(int id, String username, String email, String password, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.friendships = new HashSet<>();
        this.friendRequests = new ArrayList<>();
    }

    /*
    Metoda trimite o cerere de prietenie unui user dat
    Param:
        -otherUser este userul caruia ii trimitem cerere de prietenie
    Se va crea un obiect de tip friendship care se va adauga in lista de request a userului dat
     */
    public void sendFriendRequest(User otherUser){
        Friendship newFriendship = new Friendship(this,otherUser,"pending");
        otherUser.friendRequests.add(newFriendship);
    }

    /*
    Functia sterge userul dat din lista de prieteni a userului care apeleaza metoda
    param:
        -otherUser reprezinta prietenul pe care vreau sa l sterg
    Daca exista o relatie de prietenie intre mine si otherUser atunci o sa sterg
    aceasta relatie din listele amandurora
        -fiecare user are posibilitatea sa stearga un prieten fara acordul lui
     spre deosebire de adaugarea unui prieten care se face doar cu consimtamantul amandurora
     */
    public void removeFriend(User otherUser){
        for(Friendship friendship : otherUser.friendships){
            if(friendship.getUser1().equals(otherUser) || friendship.getUser2().equals(otherUser)){
                friendships.remove(friendship);
                otherUser.friendships.remove(friendship);
            }
        }
    }

    /*
    Metoda accepta ultima cerere primita
    Campul status al obiectului friendship se va seta corespunzator, iar obiectul va fi adaugat in listele
    de prieteni ale celor doi useri implicati
     */
    public void acceptFriendRequest(){
        Friendship friendship = friendRequests.removeLast();//sterg cererea de la request
        friendship.accept();//accept cererea
        friendship.getUser1().friendships.add(friendship);//devin prienten cu celalalt user
        this.friendships.add(friendship);//celalalt user devine prieten cu mine
    }

    public void acceptAllFriendRequests(){
        while(!friendRequests.isEmpty()){
            acceptFriendRequest();
        }
    }

    /*
    Metoda respinge ultima cerere primita
    Se va crea un obiect de tip friendship care se va adauga in lista de request a userului dat
     */
    public void declineFriendRequest(){
        Friendship friendship = friendRequests.removeLast();//sterg cererea de la request
        friendship.decline();//resping cererea
    }

    public void declineAllFriendRequests(){
        while(!friendRequests.isEmpty()){
            declineFriendRequest();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Set<Friendship> getFrienships() {
        return friendships;
    }

    public Set<User> getFriendsSet() {
        Set<User> friendsSet = new HashSet<>();
        for(Friendship friendship : friendships){
            if(friendship.getUser1().equals(this)){
                friendsSet.add(friendship.getUser2());
            }else if(friendship.getUser2().equals(this)){
                friendsSet.add(friendship.getUser1());
            }
        }
        return friendsSet;
    }

    public List<Friendship> getFriendRequests() {
        return friendRequests;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + ", email=" + email + ", password=*****, role=" +role +"]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || obj.getClass() != this.getClass()) return false;
        if(obj == this) return true;
        User user = (User) obj;
        return this.id == user.id;
    }
}
