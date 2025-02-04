package org.example.template.Repository;

import org.example.template.Domain.Flight;
import org.example.template.Domain.Ticket;
import org.example.template.Domain.Validator.Validator;

import java.sql.*;
import java.util.Optional;

public class TicketRepository {
    private String url;
    private String dbUsername;
    private String dbPassword;
    private Validator<Ticket> validator;

    public TicketRepository(String url, String username, String password, Validator<Ticket> validator) {
        this.url = url;
        this.dbUsername = username;
        this.dbPassword = password;
        this.validator = validator;
    }

    public void save(Ticket entity) {
        validator.validate(entity);
        try(Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement("insert into \"Ticket\" (username, \"flightId\", \"purchaseTime\") values (?,?,?)")){
            preparedStatement.setString(1, entity.getUsername());
            preparedStatement.setLong(2, entity.getFlightId());
            preparedStatement.setTimestamp(3,Timestamp.valueOf(entity.getPurchaseTime()));
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
