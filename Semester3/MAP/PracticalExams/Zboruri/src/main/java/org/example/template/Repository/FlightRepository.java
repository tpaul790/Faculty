package org.example.template.Repository;

import org.example.template.Domain.FiltruDto;
import org.example.template.Domain.Flight;
import org.example.template.Domain.Validator.Validator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class FlightRepository {
    private String url;
    private String dbUsername;
    private String dbPassword;
    private Validator<Flight> validator;

    public FlightRepository(String url, String username, String password, Validator<Flight> validator) {
        this.url = url;
        this.dbUsername = username;
        this.dbPassword = password;
        this.validator = validator;
    }

    public Set<Flight> findAll() {
        Set<Flight> flights = new HashSet<>();
        try(Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM \"Flights\"");
            ResultSet resultSet = preparedStatement.executeQuery()){
            while(resultSet.next()){
                long id = resultSet.getLong("flightId");
                String from = resultSet.getString("from");
                String to = resultSet.getString("to");
                LocalDateTime departure = resultSet.getTimestamp("departureTime").toLocalDateTime();
                LocalDateTime landing = resultSet.getTimestamp("landingTime").toLocalDateTime();
                int seats = resultSet.getInt("seats");
                Flight flight = new Flight(id, from, to, departure, landing,seats);
                flights.add(flight);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return flights;
    }

    public Set<Flight> findAllFiltred(FiltruDto dto) {
        Set<Flight> flights = new HashSet<>();
        try(Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement("select * from \"Flights\" where \"from\" = ? and \"to\" = ? and \"landingTime\"::DATE = ? LIMIT ? OFFSET ?")){
            preparedStatement.setString(1, dto.getFrom());
            preparedStatement.setString(2, dto.getTo());
            preparedStatement.setDate(3,Date.valueOf(dto.getLandingDate()));
            preparedStatement.setInt(4, dto.getPageSize());
            preparedStatement.setInt(5, (dto.getPageNumber()-1)*dto.getPageSize());
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                long id = resultSet.getLong("flightId");
                String from = resultSet.getString("from");
                String to = resultSet.getString("to");
                LocalDateTime departure = resultSet.getTimestamp("departureTime").toLocalDateTime();
                LocalDateTime landing = resultSet.getTimestamp("landingTime").toLocalDateTime();
                int seats = resultSet.getInt("seats");
                Flight flight = new Flight(id, from, to, departure, landing,seats);
                flights.add(flight);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return flights;
    }


    public int filtredSize(FiltruDto dto) {
        Set<Flight> flights = new HashSet<>();
        try(Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement("select * from \"Flights\" where \"from\" = ? and \"to\" = ? and \"landingTime\"::DATE = ?")){
            preparedStatement.setString(1, dto.getFrom());
            preparedStatement.setString(2, dto.getTo());
            preparedStatement.setDate(3,Date.valueOf(dto.getLandingDate()));
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                long id = resultSet.getLong("flightId");
                String from = resultSet.getString("from");
                String to = resultSet.getString("to");
                LocalDateTime departure = resultSet.getTimestamp("departureTime").toLocalDateTime();
                LocalDateTime landing = resultSet.getTimestamp("landingTime").toLocalDateTime();
                int seats = resultSet.getInt("seats");
                Flight flight = new Flight(id, from, to, departure, landing,seats);
                flights.add(flight);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return flights.size();
    }
}
