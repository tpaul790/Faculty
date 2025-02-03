package org.example.restaurant.Repository;

import org.example.restaurant.Domain.Entitati.Order;
import org.example.restaurant.Domain.Entitati.Table;
import org.example.restaurant.Domain.Entitati.Validatori.OrderValidator;
import org.example.restaurant.Domain.Entitati.Validatori.TableValidator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class TablesRepository {
    private String url;
    private String dbUsername;
    private String dbPassword;
    private TableValidator validator;

    public TablesRepository(String url, String dbUsername, String dbPassword, TableValidator validator) {
        this.url = url;
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
        this.validator = validator;
    }

    public Set<Table> findAll() {
        Set<Table> items = new HashSet<>();
        try(Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM \"Tables\"");
            ResultSet resultSet = preparedStatement.executeQuery()){
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                Table table = new Table(id);
                if(validator.validate(table))
                    items.add(table);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return items;
    }
}
