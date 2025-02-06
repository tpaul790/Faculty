package org.example.template.Repository;

import org.example.template.Domain.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class UserRepository {
    private String url;
    private String dbUsername;
    private String dbPassword;

    public UserRepository(String url, String username, String password) {
        this.url = url;
        this.dbUsername = username;
        this.dbPassword = password;
    }

    public Set<User> findAll() {
        Set<User> users = new HashSet<>();
        try(Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM \"Users\"");
            ResultSet resultSet = preparedStatement.executeQuery()){
            while(resultSet.next()){
                String email = resultSet.getString("email");
                String name = resultSet.getString("name");
                users.add(new User(email, name));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return users;
    }
}
