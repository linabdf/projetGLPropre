package com.example.demo;

import org.springframework.stereotype.Service;




import java.sql.PreparedStatement;
import java.sql.SQLException;

@Service
public class CommentaireService {

    private final DatabaseManager databaseManager;
    private  ProjectService projectService;
    private UserService UserService;

    public CommentaireService (DatabaseManager databaseManager,ProjectService projectService,UserService userService) {

        this.databaseManager = databaseManager;
        this.projectService=projectService;
        this.UserService=userService;
    }


    public boolean addComment(String projectName, String userEmail, String commentText) {
        String projectId = projectService.getProjectIdByName(projectName);
        String userId = UserService.getUserIdByEmail(userEmail);


        // Vérification si le projet et l'utilisateur existent
        if (projectId == null || userId == null) {
            System.out.println("Projet ou utilisateur non trouvé.");
            return false;
        }
        String query = "INSERT INTO Commentaire (contenu, numSender, numP) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = databaseManager.getConnexion().prepareStatement(query)) {
            stmt.setString(1, commentText);
            stmt.setString(2, userId);
            stmt.setString(3, projectId);  // ID du projet
            // ID de l'utilisateur
            // Le texte du commentaire

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Commentaire ajouté avec succès.");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du commentaire : " + e.getMessage());
        }
        return false;
    }

}