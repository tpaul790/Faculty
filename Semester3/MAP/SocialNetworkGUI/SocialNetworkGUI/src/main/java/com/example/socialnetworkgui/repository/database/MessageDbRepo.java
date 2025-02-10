package com.example.socialnetworkgui.repository.database;

import com.example.socialnetworkgui.domain.Message;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.domain.validation.Validator;
import com.example.socialnetworkgui.repository.Repository2;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class MessageDbRepo implements Repository2<Integer, Message> {
    private String url;
    private String dbUsername;
    private String dbPassword;
    private Validator<Message> validator;

    public MessageDbRepo(String url, String dbUsername, String dbPassword, Validator<Message> validator) {
        this.url = url;
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
        this.validator = validator;
    }

    @Override
    public Optional<Message> findOne(Integer idMessage) {
        Optional<Message> messageOptional = Optional.empty();
        try(Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM \"messages\" WHERE \"id\" = ?");){
            preparedStatement.setInt(1, idMessage);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                int id = resultSet.getInt("id");
                int idSender = resultSet.getInt("senderId");
                int idReceiver = resultSet.getInt("receiverId");
                String content = resultSet.getString("content");
                LocalDateTime date = resultSet.getTimestamp("sendTime").toLocalDateTime();
                String sender = resultSet.getString("sender");
                String receiver = resultSet.getString("receiver");
                messageOptional = Optional.of(new Message(id,idSender,idReceiver,sender,receiver,content,date));
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messageOptional;
    }

    @Override
    public Iterable<Message> findAll() {
        Set<Message> messages = new HashSet<>();
        try(Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM \"messages\"");
            ResultSet resultSet = preparedStatement.executeQuery();){
            while(resultSet.next()) {
                int id = resultSet.getInt("id");
                int idSender = resultSet.getInt("senderId");
                int idReceiver = resultSet.getInt("receiverId");
                String content = resultSet.getString("content");
                LocalDateTime date = resultSet.getTimestamp("sendTime").toLocalDateTime();
                String sender = resultSet.getString("sender");
                String receiver = resultSet.getString("receiver");
                messages.add(new Message(id,idSender,idReceiver,sender,receiver,content,date));
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    @Override
    public Optional<Message> save(Message message) {
        validator.validate(message);
        int rez = -1;
        try(Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement statement = connection.prepareStatement("INSERT INTO \"messages\" (\"senderId\", \"receiverId\", \"content\", \"sendTime\", sender, receiver)VALUES (?,?,?,?,?,?)")){
            statement.setInt(1,message.getIdSender());
            statement.setInt(2,message.getIdReceiver());
            statement.setString(3,message.getMessage());
            statement.setObject(4, message.getDate());
            statement.setString(5,message.getSender());
            statement.setString(6,message.getReceiver());
            rez = statement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
        if(rez > 0)
            return Optional.empty();
        return Optional.of(message);
    }

    @Override
    public Optional<Message> delete(Integer idMessage) {
        Optional<Message> messageOptional = Optional.empty();
        try(Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement statement = connection.prepareStatement("DELETE FROM \"messages\" WHERE \"id\" = ?")){
            statement.setInt(1, idMessage);
            messageOptional = findOne(idMessage);
            if(messageOptional.isPresent()) {
                statement.executeUpdate();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return messageOptional;
    }

    @Override
    public Optional<Message> update(Message message) {
        validator.validate(message);
        int rez = -1;
        try(Connection connection = DriverManager.getConnection(url,dbUsername,dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE \"messages\" SET senderId=?,receiverId=?,content=?,sender=?,receiver=? WHERE \"id\" = ?")){
            preparedStatement.setInt(1,message.getIdSender());
            preparedStatement.setInt(2,message.getIdReceiver());
            preparedStatement.setString(3,message.getMessage());
            preparedStatement.setString(4,message.getSender());
            preparedStatement.setString(5,message.getReceiver());
            rez = preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
        if(rez > 0)
            return Optional.empty();
        return Optional.of(message);
    }

    @Override
    public int size() {
        Set<Message> messages = (Set<Message>)findAll();
        return messages.size();
    }
}
