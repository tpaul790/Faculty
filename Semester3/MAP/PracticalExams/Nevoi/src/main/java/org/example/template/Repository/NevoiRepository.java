package org.example.template.Repository;

import org.example.template.Domain.Nevoie;
import org.example.template.Domain.Person;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class NevoiRepository {
    private String url;
    private String dbUsername;
    private String dbPassword;

    public NevoiRepository(String url, String username, String password) {
        this.url = url;
        this.dbUsername = username;
        this.dbPassword = password;
    }

    public void save(Nevoie nevoie) {
        try(Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO \"Nevoi\" (titlu, descriere, \"omInNevoie\", status, deadline) VALUES(?,?,?,?,?)")){
            preparedStatement.setString(1,nevoie.getTitlu());
            preparedStatement.setString(2,nevoie.getDescriere());
            preparedStatement.setLong(3,nevoie.getOmInNevoi());
            preparedStatement.setString(4,nevoie.getStatus().toString());
            preparedStatement.setTimestamp(5,Timestamp.valueOf(nevoie.getDeadline()));
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public Set<Nevoie> findAll() {
        Set<Nevoie> nevoi = new HashSet<>();
        try(Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM \"Nevoi\"");
            ResultSet resultSet = preparedStatement.executeQuery()){
            while(resultSet.next()){
                long id = resultSet.getLong("id");
                String titlu = resultSet.getString("titlu");
                String descriere = resultSet.getString("descriere");
                LocalDateTime deadline = resultSet.getTimestamp("deadline").toLocalDateTime();
                String status = resultSet.getString("status");
                long om = resultSet.getLong("omInNevoie");
                long erou = resultSet.getLong("omSalvator");
                nevoi.add(new Nevoie(id,titlu,descriere,deadline,om,erou,Nevoie.statusFromString(status)));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return nevoi;
    }
}
