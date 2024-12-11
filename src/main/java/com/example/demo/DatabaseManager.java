package com.example.demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.sql.*;


import javax.sql.DataSource;


@Configuration
public class DatabaseManager {

    private String url;
    private String username;
    private String password;

    private static Connection connection;

    public DatabaseManager(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }


    public DatabaseManager() {
    }


    @Autowired
    private DataSource dataSource;  // Injecte la DataSource automatiquement par Spring

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();  // Obtenir la connexion via DataSource
    }


    public void closeConnection(Connection connection) throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }


    public Connection getConnexion() {
        return connection;
    }


    public void connexion() {
        if (!isOnline()) {
            try {
                connection = DriverManager.getConnection(this.url, this.username, this.password);
                System.out.println("[DataBaseManager] Connexion Reusite");
                return;
            } catch (SQLException e) {
                System.out.println("[DataBaseManager] Erreur lors de la connection a la base de donné");
                System.out.println(e.getMessage());
            }
        }
    }


    public void deconnexion() {
        if (isOnline()) {
            try {
                connection.close();
                System.out.println("§a[DataBaseManager] Deconnexion Reusite");
                return;
            } catch (SQLException e) {
                System.out.println("[DataBaseManager] Erreur lors de la déconnection a la base de donné");
            }
        }
    }


    public boolean isOnline() {
        try {
            if ((connection == null) || (connection.isClosed())) {
                return false;
            }
            return true;
        } catch (SQLException e) {
            System.out.println("[DataBaseManager] Erreur lors de la verification de l'etat de la base");
        }
        return false;
    }

}