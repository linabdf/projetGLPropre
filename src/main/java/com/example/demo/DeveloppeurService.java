package com.example.demo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.demo.DatabaseManager.connection;

public class DeveloppeurService {
    private final DatabaseManager databaseManager;


    public DeveloppeurService(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }
    public static String getDeveloperIdByEmail(String email) {
        // Étape 1 : Récupérer l'ID de l'utilisateur par son email



        String query = "SELECT numU FROM developpeur d"+
                "JOIN Utilisateur u ON u.numU = d.numU "+
                  "WHERE mail= ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);  // Utiliser l'ID de l'utilisateur trouvé précédemment
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Si l'ID existe dans la table 'Developpeur', retourner cet ID
                return rs.getString("numU");
            }
        } catch (SQLException e) {
            e.printStackTrace();  // En cas d'erreur, afficher la trace de l'exception
        }

        // Si aucun développeur n'est trouvé avec cet ID, retourner null
        return null;
    }

}
