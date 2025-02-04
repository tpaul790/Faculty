package org.example.template.Repository;

public class PersoaneRepository {
    private String url;
    private String dbUsername;
    private String dbPassword;

    public PersoaneRepository(String url, String username, String password) {
        this.url = url;
        this.dbUsername = username;
        this.dbPassword = password;
    }
}
