package org.example.template.Repository;

public class NevoiRepository {
    private String url;
    private String dbUsername;
    private String dbPassword;

    public NevoiRepository(String url, String username, String password) {
        this.url = url;
        this.dbUsername = username;
        this.dbPassword = password;
    }
}
