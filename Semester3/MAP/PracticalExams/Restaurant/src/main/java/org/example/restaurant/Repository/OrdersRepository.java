package org.example.restaurant.Repository;

import org.example.restaurant.Domain.Entitati.MenuItem;
import org.example.restaurant.Domain.Entitati.Order;
import org.example.restaurant.Domain.Entitati.Tuple;
import org.example.restaurant.Domain.Entitati.Validatori.MenuItemValidator;
import org.example.restaurant.Domain.Entitati.Validatori.OrderValidator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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

    public Set<Tuple<Integer,Integer>> findAllItems() {
        Set<Tuple<Integer,Integer>> items = new HashSet<>();
        try(Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM \"OrderItems\"");
            ResultSet resultSet = preparedStatement.executeQuery()){
            while(resultSet.next()){
                int order = resultSet.getInt("orderId");
                int item = resultSet.getInt("menuItemId");
                items.add(new Tuple<>(order,item));
            }
            return items;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return items;
    }

    public Order findOne(int idOrder) {
        try(Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM \"Orders\" WHERE id = ?")){
            preparedStatement.setInt(1, idOrder);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                int table = resultSet.getInt("tableId");
                LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();
                String status = resultSet.getString("status");
                Order order = new Order(id,table,Order.statusFromString(status),date);
                if(validator.validate(order))
                    return order;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }


    public void save(int tableId) {
        try(Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO \"Orders\" (\"tableId\", date, status) VALUES(?,?,?)")){
            preparedStatement.setInt(1,tableId);
            preparedStatement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setString(3,"PLACED");
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void saveItems(int idOrder, List<Integer> itemIds){
        try(Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO \"OrderItems\" (\"orderId\", \"menuItemId\") VALUES(?,?)")){
            for(Integer itemId : itemIds) {
                preparedStatement.setInt(1, idOrder);
                preparedStatement.setInt(2, itemId);
                preparedStatement.executeUpdate();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
