package com.example.socialnetworkgui.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class User extends Entity<Integer> {
    private String username;
    private String email;
    private String password;
    private String role;
    private String profilePicture;
    private String aboutMe;
    private List<User> friends;
    private List<Friendship> friendRequests;

    public User(Integer id, String username, String email, String password, String role) {
        super.setId(id);
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.friends = new ArrayList<>();
        this.friendRequests = new ArrayList<>();
    }

    public User(String username, String email, String password, String role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.friends = new ArrayList<>();
        this.friendRequests = new ArrayList<>();
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

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public List<User> getFriends() {
        return friends;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    public List<Friendship> getFriendRequests() {
        return friendRequests;
    }

    public void setFriendRequests(Stack<Friendship> friendRequests) {
        this.friendRequests = friendRequests;
    }

    @Override
    public String toString() {
        return "User [username=" + username + ", email=" + email + ", password=" + password + ", role=" + role+"]";
    }
}
