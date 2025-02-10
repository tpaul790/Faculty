package repository.database;

import domain.Friendship;
import domain.User;
import domain.validation.RepoException;
import domain.validation.ValidationException;
import domain.validation.Validator;
import repository.Repository;
import repository.Repository2;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public class UserDbRepo implements Repository2<Integer,User> {
    private String url;
    private String dbUsername;
    private String dbPassword;
    private Validator<User> validator;

    public UserDbRepo(String url, String username, String password,Validator<User> validator) {
        this.url = url;
        this.dbUsername = username;
        this.dbPassword = password;
        this.validator = validator;
    }

    @Override
    public Optional<User> findOne(Integer integer) {
        Optional<User> opUser = Optional.empty();
        try(Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM \"users\" WHERE id=?")){
            preparedStatement.setInt(1, integer);
            ResultSet result = preparedStatement.executeQuery();
            while(result.next()) {
                int id = result.getInt("id");
                String username = result.getString("username");
                String email = result.getString("email");
                String password = result.getString("password");
                String role = result.getString("role");
                opUser = Optional.of(new User(id, username, email, password, role));
            }
            result.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return opUser;
    }

    public Optional<User> findOne(String username){
        Optional<User> opUser = Optional.empty();
        try(Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM \"users\" WHERE \"username\"=?")){
            preparedStatement.setString(1, username);
            ResultSet result = preparedStatement.executeQuery();
            while(result.next()) {
                int id = result.getInt("id");
                String userName = result.getString("username");
                String email = result.getString("email");
                String password = result.getString("password");
                String role = result.getString("role");
                opUser = Optional.of(new User(id, username, email, password, role));
            }
            result.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return opUser;
    }

    @Override
    public Iterable<User> findAll() {
        Set<User> users = new HashSet<>();
        try(Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM \"users\"");
            ResultSet resultSet = preparedStatement.executeQuery()){
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");
                String role = resultSet.getString("role");
                users.add(new User(id,username,password,email,role));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public Optional<User> save(User entity) {
        int rez = -1;
        validator.validate(entity);
        Optional<User> opUser = findOne(entity.getUsername());
        if(opUser.isPresent())
            return opUser;
        try(Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO \"users\"(username,email,password,role) VALUES (?,?,?,?)")){
            preparedStatement.setString(1,entity.getUsername());
            preparedStatement.setString(2,entity.getEmail());
            preparedStatement.setString(3,entity.getPassword());
            preparedStatement.setString(4,entity.getRole());
            rez = preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
        if(rez > 0)
            return Optional.empty();
        return Optional.of(entity);
    }

    @Override
    public Optional<User> delete(Integer id) {
        Optional<User> user = Optional.empty();
        try(Connection connection = DriverManager.getConnection(url,dbUsername,dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM \"users\" WHERE id = ?")){
            preparedStatement.setInt(1,id);
            user = findOne(id);
            if(user.isPresent())
                preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return user; //empty daca nu exista userul si cu userul daca il sterge
    }

    @Override
    public Optional<User> update(User entity) {
        validator.validate(entity);
        Optional<User> opUser = Optional.empty();
        int rez = -1;
        try(Connection connection = DriverManager.getConnection(url,dbUsername,dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE \"users\" SET username=?,email=?,password=?,role=? WHERE id = ?")){
            preparedStatement.setString(1,entity.getUsername());
            preparedStatement.setString(2,entity.getEmail());
            preparedStatement.setString(3,entity.getPassword());
            preparedStatement.setString(4,entity.getRole());
            preparedStatement.setInt(5,entity.getId());
            rez = preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
        if(rez > 0)
            return Optional.empty();
        return Optional.of(entity);
    }

    @Override
    public int size() {
        try(Connection connection = DriverManager.getConnection(url,dbUsername,dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM \"users\"");
            ResultSet result = preparedStatement.executeQuery()){
            return result.getInt(1);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

}
