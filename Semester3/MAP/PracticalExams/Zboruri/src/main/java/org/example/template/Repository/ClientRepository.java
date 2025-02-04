package org.example.template.Repository;

import org.example.template.Domain.Client;
import org.example.template.Domain.Validator.Validator;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class ClientRepository {
    private String url;
    private String dbUsername;
    private String dbPassword;
    private Validator<Client> validator;

    public ClientRepository(String url, String username, String password, Validator<Client> validator) {
        this.url = url;
        this.dbUsername = username;
        this.dbPassword = password;
        this.validator = validator;
    }

    public Set<Client> findAll() {
        Set<Client> clienti = new HashSet<>();
        try(Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM \"Client\"");
            ResultSet resultSet = preparedStatement.executeQuery()){
            while(resultSet.next()){
                String username = resultSet.getString("username");
                String name = resultSet.getString("name");
                Client client = new Client(username, name);
                clienti.add(client);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return clienti;
    }
}
