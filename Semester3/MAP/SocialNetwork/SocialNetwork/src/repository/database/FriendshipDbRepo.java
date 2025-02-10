package repository.database;

import domain.Friendship;
import domain.Tuple;
import domain.User;
import domain.validation.Validator;
import repository.Repository2;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public class FriendshipDbRepo implements Repository2<Tuple<Integer,Integer>, Friendship> {
    private String url;
    private String dbUsername;
    private String dbPassword;
    private Validator<Friendship> validator;

    public FriendshipDbRepo(String url, String dbUsername, String dbPassword, Validator<Friendship> validator) {
        this.url = url;
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
        this.validator = validator;
    }

    @Override
    public Optional<Friendship> findOne(Tuple<Integer, Integer> id) {
        Optional<Friendship> opFriendship = Optional.empty();
        try (Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM \"friendship\" WHERE (\"idUser1\" = ? AND \"idUser2\" = ?) OR (\"idUser1\" = ? AND \"idUser2\" = ?)")) {
            preparedStatement.setInt(1, id.getFirst());
            preparedStatement.setInt(2, id.getSecond());
            preparedStatement.setInt(3, id.getSecond());
            preparedStatement.setInt(4, id.getFirst());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int idUser1 = resultSet.getInt("idUser1");
                int idUser2 = resultSet.getInt("idUser2");
                LocalDate acceptDate = null;
                if(resultSet.getDate("acceptDateTime") != null)
                    acceptDate = resultSet.getDate("acceptDateTime").toLocalDate();
                LocalDate createDate = resultSet.getDate("createDateTime").toLocalDate();
                String status = resultSet.getString("status");
                opFriendship = Optional.of(new Friendship(new Tuple<>(idUser1,idUser2),createDate,acceptDate,status));
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return opFriendship;
    }

    @Override
    public Iterable<Friendship> findAll() {
        Set<Friendship> friendships = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM \"friendship\"");
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int id1 = resultSet.getInt("idUser1");
                int id2 = resultSet.getInt("idUser2");
                LocalDate acceptDate = null;
                LocalDate createDate = resultSet.getDate("createDateTime").toLocalDate();
                String status = resultSet.getString("status");
                if(status.equals("accepted")){
                    acceptDate = resultSet.getDate("acceptDateTime").toLocalDate();
                }
                friendships.add(new Friendship(new Tuple<>(id1, id2), createDate, acceptDate, status));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendships;
    }

    @Override
    public Optional<Friendship> save(Friendship friendship) {
        validator.validate(friendship);
        Optional<Friendship> opReverseFriendship = findOne(friendship.getId());
        if(opReverseFriendship.isPresent())
            return Optional.of(friendship);
        int rez = -1;
        try (Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO \"friendship\" VALUES (?,?,?,?,?)")){
            preparedStatement.setInt(1, friendship.getId().getFirst());
            preparedStatement.setInt(2, friendship.getId().getSecond());
            preparedStatement.setDate(3, Date.valueOf(friendship.getCreateDate()));
            if(friendship.getAcceptDate() == null)
                preparedStatement.setDate(4, null);
            else
                preparedStatement.setDate(4, Date.valueOf(friendship.getAcceptDate()));
            preparedStatement.setString(5, friendship.getStatus());
            rez = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (rez > 0)
            return Optional.empty();
        return Optional.of(friendship);
    }

    @Override
    public Optional<Friendship> delete(Tuple<Integer, Integer> id) {
        Optional<Friendship> optionalFriendship = Optional.empty();
        try (Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM \"friendship\" WHERE (\"idUser1\" = ? AND \"idUser2\" = ?) OR (\"idUser1\" = ? AND \"idUser2\" = ?)")) {
            preparedStatement.setInt(1, id.getFirst());
            preparedStatement.setInt(2, id.getSecond());
            preparedStatement.setInt(3, id.getSecond());
            preparedStatement.setInt(4, id.getFirst());
            optionalFriendship = findOne(id);
            if (optionalFriendship.isPresent())
                preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return optionalFriendship;
    }

    @Override
    public Optional<Friendship> update(Friendship friendship) {
        int rez = -1;
        try(Connection connection = DriverManager.getConnection(url,dbUsername,dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE \"friendship\" SET \"acceptDateTime\" = ?, \"status\" = ? WHERE \"idUser1\" = ? AND \"idUser2\" = ?")){
            preparedStatement.setDate(1, Date.valueOf(friendship.getAcceptDate()));
            preparedStatement.setString(2, friendship.getStatus());
            preparedStatement.setInt(3, friendship.getId().getFirst());
            preparedStatement.setInt(4, friendship.getId().getSecond());
            rez = preparedStatement.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        if(rez > 0)
            return Optional.empty();
        return Optional.of(friendship);
    }

    @Override
    public int size() {
        try (Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM \"friendship\"");
             ResultSet result = preparedStatement.executeQuery()) {
             return result.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Iterable<Friendship> findAllFriendRequests(int id) {
        Set<Friendship> friendRequests = new HashSet<>();
        try(Connection connection = DriverManager.getConnection(url,dbUsername,dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM \"friendship\" WHERE \"idUser2\" = ? AND \"status\" = ?")){
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, "pending");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int idUser1 = resultSet.getInt("idUser1");
                int idUser2 = resultSet.getInt("idUser2");
                LocalDate createDate = resultSet.getDate("createDateTime").toLocalDate();
                friendRequests.add(new Friendship(new Tuple<>(idUser1, idUser2), createDate, null, "pending"));
            }
            resultSet.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return friendRequests;
    }

    public Iterable<User> findAllFriends(int idUser) {
        Set<User> friends = new HashSet<>();
        try(Connection connection = DriverManager.getConnection(url,dbUsername,dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT DISTINCT (U.id),U.username,U.email,U.password,U.role FROM users U INNER JOIN friendship F ON (((\"idUser1\" = id AND \"idUser2\" = ?) OR (\"idUser1\" = ? AND \"idUser2\" = id)) AND id != ?) WHERE \"status\" = 'accepted'")){
            preparedStatement.setInt(1, idUser);
            preparedStatement.setInt(2, idUser);
            preparedStatement.setInt(3, idUser);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                String role = resultSet.getString("role");
                User user = new User(id, username, email, password, role);
                friends.add(user);
            }
            resultSet.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return friends;
    }

    public int numberOfFriends(int id){
        Set<User> friends = (Set<User>) findAllFriends(id);
        return friends.size();
    }

    public void deleteAllFriendRequests(int id) {
        try(Connection connection = DriverManager.getConnection(url,dbUsername,dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM \"friendship\" WHERE \"idUser2\" = ? AND \"status\" = ?")){
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, "pending");
            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
