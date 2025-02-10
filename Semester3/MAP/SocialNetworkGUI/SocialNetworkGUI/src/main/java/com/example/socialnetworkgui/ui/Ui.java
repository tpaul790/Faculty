package com.example.socialnetworkgui.ui;

import com.example.socialnetworkgui.domain.Friendship;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.domain.validation.RepoException;
import com.example.socialnetworkgui.domain.validation.ServiceException;
import com.example.socialnetworkgui.domain.validation.ValidationException;
import com.example.socialnetworkgui.service.FriendshipService;
import com.example.socialnetworkgui.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Ui {
    Scanner scanner = new Scanner(System.in);
    private final UserService userService;
    private final FriendshipService friendshipService;

    public Ui(UserService userService,FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
        this.userService = userService;
    }

    public void printMenu(){
        System.out.println("m. Print menu");
        System.out.println("u. Print all users");
        System.out.println("g. Print friendship groups");
        System.out.println("f. Print all friends");
        System.out.println("1. Add a new user");
        System.out.println("2. Delete a user");
        System.out.println("3. Edit a user");
        System.out.println("4. Send a friend request");
        System.out.println("5. Accept all friend requests");
        System.out.println("6. Decline all friend requests");
        System.out.println("7. Remove a friend");
        System.out.println("8. Number of social comunities");
        System.out.println("9. Print the most social comunity");
        System.out.println("0. Exit");
    }

    public void seeAll(Iterable<?> all, Predicate<Friendship> predicate){
        Consumer<Object> consumer = obj -> {
            if(predicate == null || predicate.test((Friendship)obj)){
                System.out.println(obj);
            }
        };
        all.forEach(consumer);
    }

    public void addUserUi(){
        int id;
        String username,email,password,role;
        System.out.print("Please enter your username: ");
        username = scanner.next();
        System.out.print("Please enter your email: ");
        email = scanner.next();
        System.out.print("Please enter your password: ");
        password = scanner.next();
        System.out.print("Please enter your role: ");
        role = scanner.next();
        try {
            userService.save(username, email, password, role);
            System.out.println("User successfully added");
        }catch (ValidationException | ServiceException e){
            System.out.println(e.getMessage());
        }
    }

    public void removeUserUi(){
        int id;
        System.out.print("Please enter the id of the user you would like to remove: ");
        id = scanner.nextInt();
        try{
//            friendshipService.deleteAllFriendshipsForUser(id);
            userService.delete(id);
            System.out.println("User successfully removed");
        }catch (ValidationException | ServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateUserUi(){
        int id;
        String username,email,password,role;
        System.out.print("Please enter your id: ");
        id = scanner.nextInt();
        System.out.print("Please enter the new username: ");
        username = scanner.next();
        System.out.print("Please enter the new email: ");
        email = scanner.next();
        System.out.print("Please enter the new password: ");
        password = scanner.next();
        System.out.print("Please enter the new role: ");
        role = scanner.next();
        try{
            userService.update(id,username,email,password,role,"","");
            System.out.println("User successfully updated");
        }catch (ValidationException | ServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    public void numberOfSocialComunitiesUi(){
        int nr = friendshipService.numberOfSocialComunities();
        System.out.println("There are " +nr +" social grups!");
    }

    public void mostSocialComunityUi(){
        Set<User> comunity = friendshipService.mostSocialComunity();
        comunity.forEach(System.out::println);
    }

    public void sendFriendRequestUi(){
        int id1,id2;
        System.out.print("Please enter your id: ");
        id1 = scanner.nextInt();
        System.out.print("Please enter the id of the user you want to send a friend request: ");
        id2 = scanner.nextInt();
        try{
            friendshipService.sendFriendRequest(id1,id2);
            System.out.println("Friend request sent!");
        }catch (RepoException | ValidationException | ServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    public void acceptAllFriendRequestUi() {
        int id;
        System.out.print("Please enter your id: ");
        id = scanner.nextInt();
        try{
            int nr = friendshipService.acceptAllFriendRequests(id);
            System.out.println("You had " + nr+ " friend requests and all was accepted!");
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    public void declineAllFriendRequestUi(){
        int id;
        System.out.print("Please enter your id: ");
        id = scanner.nextInt();
        try{
            friendshipService.declineAllFriendRequests(id);
            System.out.println("All of your friend requests was declined!");
        }catch (RepoException | ServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    private void removeFriendUi() {
        int id1,id2;
        System.out.print("Please enter your id: ");
        id1 = scanner.nextInt();
        System.out.print("Please enter the id of the friend you want to remove: ");
        id2 = scanner.nextInt();
        try{
            friendshipService.delete(id1,id2);
            System.out.println("Friend successfully removed!");
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    public void seeAllFriendsUi(){
        int id;
        System.out.print("Please enter your id: ");
        id = scanner.nextInt();
        try {
            Set<User> friends = (Set<User>) friendshipService.getAllFriends(id);
            if(friends.isEmpty())
                System.out.println("You have no friends!");
            else {
                System.out.println("Your friends are: ");
                seeAll(friends,null);
            }
        }catch (RepoException | ServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    public void run(){
        String option;
        printMenu();
        label:
        while(true){
            System.out.print("Please enter your option: ");
            option = scanner.next();
            switch (option) {
                case "m":
                    printMenu();
                    break;
                case "u":
                    seeAll(userService.findAll(),null);
                    break;
                case "g":
                    Predicate<Friendship> predicate = x->x.getStatus().equals("accepted");
                    seeAll(friendshipService.findAll(),predicate);
                    break;
                case "f":
                    seeAllFriendsUi();
                    break;
                case "1":
                    addUserUi();
                    break;
                case "2":
                    removeUserUi();
                    break;
                case "3":
                    updateUserUi();
                    break;
                case "4":
                    sendFriendRequestUi();
                    break;
                case "5":
                    acceptAllFriendRequestUi();
                    break;
                case "6":
                    declineAllFriendRequestUi();
                    break;
                case "7":
                    removeFriendUi();
                    break;
                case "8":
                    numberOfSocialComunitiesUi();
                    break;
                case "9":
                    mostSocialComunityUi();
                    break;
                case "0":
                    break label;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }

    }

}
