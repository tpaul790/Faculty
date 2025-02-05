package org.example.template.Repository;

import org.example.template.Domain.AdoptionCentre;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class AdoptionCentreRepository {
    private String url;
    private String dbUsername;
    private String dbPassword;

    public AdoptionCentreRepository(String url, String username, String password) {
        this.url = url;
        this.dbUsername = username;
        this.dbPassword = password;
    }

    public Set<AdoptionCentre> findAll() {
        Set<AdoptionCentre> centre = new HashSet<>();
        try(Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement("select * from \"AdoptionCentre\"");
            ResultSet resultSet = preparedStatement.executeQuery()){
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String nume = resultSet.getString("nume");
                String locatie = resultSet.getString("locatie");
                int capacitate = resultSet.getInt("capacitate");
                AdoptionCentre centru = new AdoptionCentre(nume,locatie,capacitate);
                centru.setId(id);
                centre.add(centru);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return centre;
    }


    public Set<AdoptionCentre> findAllByLocation(String location) {
        Set<AdoptionCentre> centre = new HashSet<>();
        try(Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement("select * from \"AdoptionCentre\" where locatie = ?")){
            preparedStatement.setString(1,location);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String nume = resultSet.getString("nume");
                String locatie = resultSet.getString("locatie");
                int capacitate = resultSet.getInt("capacitate");
                AdoptionCentre centru = new AdoptionCentre(nume,locatie,capacitate);
                centru.setId(id);
                centre.add(centru);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return centre;
    }
}
