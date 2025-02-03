package org.example.restaurant.Repository;

import org.example.restaurant.Domain.Entitati.MenuItem;
import org.example.restaurant.Domain.Entitati.Order;
import org.example.restaurant.Domain.Entitati.Validatori.MenuItemValidator;
import org.example.restaurant.Domain.Entitati.Validatori.OrderValidator;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MenuItemsRepository {
    private String url;
    private String dbUsername;
    private String dbPassword;
    private MenuItemValidator validator;

    public MenuItemsRepository(String url, String dbUsername, String dbPassword, MenuItemValidator validator) {
        this.url = url;
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
        this.validator = validator;
    }

    public MenuItem findOne(int id) {
        MenuItem menuItem = null;
        try (Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM \"Menu\" WHERE id = ?");) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String category = resultSet.getString("category");
                String item = resultSet.getString("item");
                Float price = resultSet.getFloat("price");
                String currency = resultSet.getString("currency");
                menuItem = new MenuItem(id, category, item, price, currency);
                if (validator.validate(menuItem))
                    return menuItem;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return menuItem;
    }

    public Set<MenuItem> findAll() {
        Set<MenuItem> items = new HashSet<>();
        try(Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM \"Menu\"");
            ResultSet resultSet = preparedStatement.executeQuery()){
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String category = resultSet.getString("category");
                String item = resultSet.getString("item");
                Float price = resultSet.getFloat("price");
                String currency = resultSet.getString("currency");
                MenuItem menuItem = new MenuItem(id, category, item, price, currency);
                if(validator.validate(menuItem))
                    items.add(menuItem);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return items;
    }
}
