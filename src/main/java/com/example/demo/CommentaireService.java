package com.example.demo;

import org.springframework.stereotype.Service;

import java.sql.*;

import static com.example.demo.DatabaseManager.connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.hibernate.annotations.UuidGenerator.Style.RANDOM;
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

        /**
         * Méthode pour ajouter un commentaire dans la base de données.
         *
         * @param projectName  Le nom du projet auquel le commentaire est associé.
         * @param userEmail    L'email de l'utilisateur qui laisse le commentaire.
         * @param commentText  Le texte du commentaire.
         * @return true si l'insertion réussie, sinon false.
         */
        public boolean addComment(String projectName, String userEmail, String commentText) {
            String projectId = projectService.getProjectIdByName(projectName);
            String userId = UserService.getUserIdByEmail(userEmail);


            // Vérification si le projet et l'utilisateur existent
            if (projectId == null || userId == null) {
                System.out.println("Projet ou utilisateur non trouvé.");
                return false;
            }
            String query = "INSERT INTO Commentaire (contenu, numSender, numP) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
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


