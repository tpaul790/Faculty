package ui;

import exception.RepoException;
import service.NetworkService;

import java.util.Scanner;
import java.util.Set;

public class Ui {
    Scanner scanner = new Scanner(System.in);
    private NetworkService networkService;

    public Ui(NetworkService networkService) {
        this.networkService = networkService;
    }

    public void printMenu(){
        System.out.println("m. Print menu");
        System.out.println("p. Print all users");
        System.out.println("1. Add a new user");
        System.out.println("2. Delete a user");
        System.out.println("3. Edit a user");
        System.out.println("4. Send a friend request");
        System.out.println("5. Accept all friend requests");
        System.out.println("6. Decline all friend requests");
        System.out.println("7. Print all friends");
        System.out.println("8. Number of social comunities");
        System.out.println("9. The most sociable comunity");
        System.out.println("0. Exit");
    }

    public void seeAll(Set<?> all){
        for(var obj : all){
            System.out.println(obj);
        }
    }

    public void addUserUi(){
        int id;
        String username,email,password,role;
        System.out.print("Please enter your id: ");
        id = scanner.nextInt();
        System.out.print("Please enter your username: ");
        username = scanner.next();
        System.out.print("Please enter your email: ");
        email = scanner.next();
        System.out.print("Please enter your password: ");
        password = scanner.next();
        System.out.print("Please enter your role: ");
        role = scanner.next();
        try{
            networkService.addUser(id,username,email,password,role);
        } catch (RepoException e) {
            System.out.println(e.getMessage());
        }
    }

    public void removeUserUi(){
        int id;
        System.out.print("Please enter your id: ");
        id = scanner.nextInt();
        try{
            networkService.removeUser(id);
        }catch (RepoException e) {
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
            networkService.updateUser(id,username,email,password,role);
        }catch (RepoException e) {
            System.out.println(e.getMessage());
        }
    }

    public void numberOfSocialComunitiesUi(){
        int nr = networkService.numberOfSocialComunities();
        System.out.println("There are " +nr +" social grups!");
    }

    public void sendFriendRequestUi(){
        int id1,id2;
        System.out.print("Please enter your id: ");
        id1 = scanner.nextInt();
        System.out.print("Please enter the id of the user you want to send a friend request: ");
        id2 = scanner.nextInt();
        try{
            networkService.sendFriendRequest(id1,id2);
            System.out.println("Friend request sent!");
        }catch (RepoException e) {
            System.out.println(e.getMessage());
        }
    }

    public void acceptAllFriendRequestUi(){
        int id;
        System.out.print("Please enter your id: ");
        id = scanner.nextInt();
        try{
            networkService.acceptAllFriendRequests(id);
            System.out.println("All friend requests accepted!");
        }catch (RepoException e) {
            System.out.println(e.getMessage());
        }
    }

    public void declineAllFriendRequestUi(){
        int id;
        System.out.print("Please enter your id: ");
        id = scanner.nextInt();
        try{
            networkService.declineAllFriendRequests(id);
            System.out.println("All friend requests declined!");
        }catch (RepoException e) {
            System.out.println(e.getMessage());
        }
    }

    public void seeAllFriendsUi(){
        int id;
        System.out.print("Please enter your id: ");
        id = scanner.nextInt();
        try {
            var friends = networkService.getAllFriends(id);
            if(friends.isEmpty())
                System.out.println("No friends found!");
            else {
                System.out.println("Your friends are: ");
                seeAll(friends);
            }
        }catch (RepoException e) {
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
                case "p":
                    seeAll(networkService.getAllUsers());
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
                    numberOfSocialComunitiesUi();
                    break;
                case "5":
                    sendFriendRequestUi();
                    break;
                case "6":
                    acceptAllFriendRequestUi();
                    break;
                case "7":
                    declineAllFriendRequestUi();
                    break;
                case "8":
                    seeAllFriendsUi();
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
