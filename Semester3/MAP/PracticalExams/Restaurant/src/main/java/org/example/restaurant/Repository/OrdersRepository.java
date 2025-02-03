package org.example.restaurant.Repository;

import org.example.restaurant.Domain.Entitati.MenuItem;
import org.example.restaurant.Domain.Entitati.Order;
import org.example.restaurant.Domain.Entitati.Validatori.MenuItemValidator;
import org.example.restaurant.Domain.Entitati.Validatori.OrderValidator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class OrdersRepository {
    private String url;
    private String dbUsername;
    private String dbPassword;
    private OrderValidator validator;

    public OrdersRepository(String url, String dbUsername, String dbPassword, OrderValidator validator) {
        this.url = url;
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
        this.validator = validator;
    }

    public Set<Order> findAll() {
        Set<Order> items = new HashSet<>();
        try(Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM \"Orders\"");
            ResultSet resultSet = preparedStatement.executeQuery()){
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                int table = resultSet.getInt("tableId");
                LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();
                String status = resultSet.getString("status");
                Order order = new Order(id,table,Order.statusFromString(status),date);
                if(validator.validate(order))
                    items.add(order);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return items;
    }
}
