package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;


import java.sql.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;



@SpringBootTest(classes = DemoApplication.class)
public class DatabaseManagerTestIntegration {

    private DatabaseManager databaseManager = new DatabaseManager("jdbc:mysql://sql7.freesqldatabase.com:3306/sql7743283","sql7743283","aC2kDrfGsk");


    @Test
    public void testConnexion() {
        // Test de la méthode connexion
        databaseManager.connexion();
        assertTrue(databaseManager.isOnline(), "La connexion doit être réussie.");
    }


    @Test
    public void testDeconnexion() throws SQLException {
        // Test de la déconnexion
        databaseManager.connexion();
        databaseManager.deconnexion();
        assertFalse(databaseManager.isOnline(), "La connexion doit être fermée.");
    }

    @Test
    public void testisOnline() throws SQLException {
        // Test de la déconnexion
        databaseManager.connexion();
        boolean result = databaseManager.isOnline();
        assertTrue(result, "La base de donnée doit etre en ligne");

        databaseManager.deconnexion();
        boolean result2 = databaseManager.isOnline();
        assertFalse(result2, "La base de donnée doit etre hors ligne");

    }


}

