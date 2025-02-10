package org.example.seminar14.Repo;

import eu.hansolo.tilesfx.tools.Rank;
import org.example.seminar14.Domain.Angajat;
import org.example.seminar14.Domain.Nivel;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AngajatRepo {
    private String url;
    private String dbUsername;
    private String dbPassword;

    public AngajatRepo(String url, String dbUsername, String dbPassword) {
        this.url = url;
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
    }

    public List<Angajat> findAll() {
        List<Angajat> angajati = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM \"Angajati\"");
            ResultSet resultSet = preparedStatement.executeQuery()){
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String nume = resultSet.getString("nume");
                double venitPeOra = resultSet.getDouble("venitPeOra");
                String senioritate = resultSet.getString("senioritate");
                Nivel niv;
                if(senioritate.equals("JUNIOR"))
                    niv = Nivel.JUNIOR;
                else if (senioritate.equals("SENIOR")) {
                    niv = Nivel.SENIOR;
                }
                else{
                    niv = Nivel.MEDIUM;
                }
                Angajat angajat = new Angajat(nume,venitPeOra,niv);
                angajat.setId(id);
                angajati.add(angajat);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return angajati;
    }
}
