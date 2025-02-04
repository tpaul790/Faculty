package org.example.template.Repository;

import javafx.collections.ObservableList;
import org.example.template.Domain.Person;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class PersoaneRepository {
    private String url;
    private String dbUsername;
    private String dbPassword;

    public PersoaneRepository(String url, String username, String password) {
        this.url = url;
        this.dbUsername = username;
        this.dbPassword = password;
    }

    public void save(Person person) {
        try(Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO \"Persons\" (nume, prenume, username, parola, oras, strada, numar, telefon) VALUES(?,?,?,?,?,?,?,?)")){
            preparedStatement.setString(1,person.getNume());
            preparedStatement.setString(2,person.getPrenume());
            preparedStatement.setString(3,person.getUsername());
            preparedStatement.setString(4,person.getPassword());
            preparedStatement.setString(5,person.getOras().toString());
            preparedStatement.setString(6,person.getStrada());
            preparedStatement.setString(7,person.getNumar());
            preparedStatement.setString(8,person.getTelefon());
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public Set<Person> findAll() {
        Set<Person> persoane = new HashSet<>();
        try(Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM \"Persons\"");
            ResultSet resultSet = preparedStatement.executeQuery()){
            while(resultSet.next()){
                long id = resultSet.getLong("id");
                String nume = resultSet.getString("nume");
                String prenume = resultSet.getString("prenume");
                String username = resultSet.getString("username");
                String parola = resultSet.getString("parola");
                String oras = resultSet.getString("oras");
                String strada = resultSet.getString("strada");
                String numar = resultSet.getString("numar");
                String telefon = resultSet.getString("telefon");
                Person person = new Person(id,nume,prenume,username,parola,Person.orasFromString(oras),strada,numar,telefon);
                persoane.add(person);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return persoane;
    }

    public Person findOne(long id) {
        try(Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM \"Persons\" WHERE id = ?")){
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                String nume = resultSet.getString("nume");
                String prenume = resultSet.getString("prenume");
                String username = resultSet.getString("username");
                String parola = resultSet.getString("parola");
                String oras = resultSet.getString("oras");
                String strada = resultSet.getString("strada");
                String numar = resultSet.getString("numar");
                String telefon = resultSet.getString("telefon");
                return new Person(id,nume,prenume,username,parola,Person.orasFromString(oras),strada,numar,telefon);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
