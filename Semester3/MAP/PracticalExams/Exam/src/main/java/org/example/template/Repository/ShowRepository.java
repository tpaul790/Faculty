package org.example.template.Repository;

import org.example.template.Domain.Show;
import org.example.template.Domain.User;
import org.example.template.Domain.Validator.Validator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class ShowRepository {
    private String url;
    private String dbUsername;
    private String dbPassword;

    public ShowRepository(String url, String username, String password) {
        this.url = url;
        this.dbUsername = username;
        this.dbPassword = password;
    }

    public void save(Show entity) {
        try(Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement("insert into \"Shows\" (\"name\",\"durationMinutes\", \"startDate\", \"numberOfSeats\", \"creationDate\") VALUES (?,?,?,?,?)")){
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setInt(2, entity.getDurationMinutes());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(entity.getStartDate()));
            preparedStatement.setInt(4, entity.getNrOfSeats());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public Set<Show> findAll() {
        Set<Show> shows = new HashSet<>();
        try(Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM \"Shows\"");
            ResultSet resultSet = preparedStatement.executeQuery()){
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int durationMinutes = resultSet.getInt("durationMinutes");
                LocalDateTime startDate = resultSet.getTimestamp("startDate").toLocalDateTime();
                int numberOfSeats = resultSet.getInt("numberOfSeats");
                LocalDateTime creationDate = resultSet.getTimestamp("creationDate").toLocalDateTime();
                Show show = new Show(name,durationMinutes,startDate,numberOfSeats,creationDate);
                show.setId(id);
                shows.add(show);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return shows;
    }

    public Set<Show> findAllOnPage(int curentPage, int pageSize) {
        Set<Show> shows = new LinkedHashSet<>();
        try(Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement("select * from \"Shows\" order by \"startDate\" LIMIT ? OFFSET ?")){
            preparedStatement.setInt(1, pageSize);
            preparedStatement.setInt(2, (curentPage - 1) * pageSize);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int durationMinutes = resultSet.getInt("durationMinutes");
                LocalDateTime startDate = resultSet.getTimestamp("startDate").toLocalDateTime();
                int numberOfSeats = resultSet.getInt("numberOfSeats");
                LocalDateTime creationDate = resultSet.getTimestamp("creationDate").toLocalDateTime();
                Show show = new Show(name,durationMinutes,startDate,numberOfSeats,creationDate);
                show.setId(id);
                shows.add(show);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return shows;
    }
}
