package com.example.demo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.stereotype.Service;

@Service
public class DeveloppeurService {

    private final DatabaseManager databaseManager;

    public DeveloppeurService(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public String getDeveloperIdByEmail(String email) {
        String query = "SELECT d.numU FROM developpeur d " +
                "JOIN Utilisateur u ON u.numU = d.numU " +
                "WHERE mail= ?";

        try (PreparedStatement stmt = databaseManager.getConnexion().prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("numU");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Aucun résultat trouvé
    }
}
