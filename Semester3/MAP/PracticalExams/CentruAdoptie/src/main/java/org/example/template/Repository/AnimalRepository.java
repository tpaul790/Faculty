package org.example.template.Repository;

import org.example.template.Domain.Animal;
import org.example.template.Domain.FiltruDto;
import org.example.template.Domain.Type;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class AnimalRepository {
    private String url;
    private String dbUsername;
    private String dbPassword;

    public AnimalRepository(String url, String username, String password) {
        this.url = url;
        this.dbUsername = username;
        this.dbPassword = password;
    }

    public Set<Animal> findAll() {
        Set<Animal> animale = new HashSet<>();
        try(Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement("select * from \"Animal\"");
            ResultSet resultSet = preparedStatement.executeQuery()){
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String nume = resultSet.getString("nume");
                int centruId = resultSet.getInt("centruId");
                String tip = resultSet.getString("tip");
                Animal animal = new Animal(nume,centruId, Type.valueOf(tip));
                animal.setId(id);
                animale.add(animal);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return animale;
    }

    public Set<Animal> findAllFiltred(FiltruDto dto) {
        Set<Animal> animale = new HashSet<>();
        try(Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement("select * from \"Animal\" where \"centruId\" = ? and tip = ?")){
            preparedStatement.setInt(1, dto.getCentruId());
            preparedStatement.setString(2,dto.getTip().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String nume = resultSet.getString("nume");
                Animal animal = new Animal(nume,dto.getCentruId(), dto.getTip());
                animal.setId(id);
                animale.add(animal);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return animale;
    }

    public void update(Animal entity) {
        try(Connection connection = DriverManager.getConnection(url,dbUsername,dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement("update \"Animal\" set \"centruId\" = ? where id = ?")){
            preparedStatement.setInt(1,entity.getCentruId());
            preparedStatement.setInt(2,entity.getId());
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
