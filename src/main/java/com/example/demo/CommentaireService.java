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

    public CommentaireService (DatabaseManager databaseManager) {
        
                this.databaseManager = databaseManager;
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
            String projectId = getProjectIdByName(projectName);
            String userId = getUserIdByEmail(userEmail);

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
  /*  public static List<commentaire> getCommentairesByProjectId(String projectId) {
        String query = "SELECT id,contenu,numSender,numP FROM Commentaire WHERE numP = ?";
        List<commentaire> commentaires = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, projectId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                commentaire commentaire = new commentaire(
                        rs.getInt("ID"),
                        rs.getString("contenu"),
                        rs.getString("numSender"),
                        rs.getString("numP")

                );
                commentaires.add(commentaire);

            }



        } catch (SQLException e) {
            e.printStackTrace();
        }
        return commentaires;
    }*/
        /**
         * Récupérer l'ID d'un projet à partir de son nom.
         *
         * @param projectName Le nom du projet.
         * @return L'ID du projet ou null si non trouvé.
         */
        private static String getProjectIdByName(String projectName) {
            // Définir la requête SQL pour récupérer l'ID du projet en fonction de son nom
            String query = "SELECT numP FROM Projet WHERE nomP = ?"; // Attention : cela peut entraîner une injection SQL si projectName n'est pas validé correctement

            // Utilisation de Statement
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, projectName);
                // Exécuter la requête SQL et obtenir les résultats
                ResultSet resultSet = stmt.executeQuery();

                // Si des résultats sont trouvés, retourner l'ID du projet
                if (resultSet.next()) {
                    return resultSet.getString("numP");
                }
            } catch (SQLException e) {
                // Gestion des erreurs liées à l'exécution de la requête
                System.err.println("Erreur lors de l'exécution de la requête : " + e.getMessage());
            }

            // Retourner null si aucun projet n'est trouvé ou si une erreur se produit
            return null;
        }


    /**
         * Récupérer l'ID d'un utilisateur à partir de son email.
         *
         * @param email L'email de l'utilisateur.
         * @return L'ID de l'utilisateur ou null si non trouvé.
         */
    private String getUserIdByEmail(String email) {
        // Définir la requête SQL pour récupérer l'ID de l'utilisateur en fonction de son email
        String query = "SELECT numU FROM Utilisateur WHERE mail = ?"; // Attention à l'injection SQL !

        // Utilisation de Statement
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            // Exécuter la requête SQL et obtenir les résultats
            ResultSet resultSet = stmt.executeQuery();

            // Si des résultats sont trouvés, retourner l'ID de l'utilisateur
            if (resultSet.next()) {
                return resultSet.getString("numU");
            }
        } catch (SQLException e) {
            // Gestion des erreurs liées à l'exécution de la requête
            System.err.println("Erreur lors de la récupération de l'ID de l'utilisateur : " + e.getMessage());
        }

        // Retourner null si aucun utilisateur n'est trouvé ou si une erreur se produit
        return null;
    }

  /*  public static List<commentaire> getCommentairesByProject(String projectName) {
        String projectId = getProjectIdByName(projectName);

        if (projectId == null) {
            return new ArrayList<>();  // Aucun projet trouvé, retournez une liste vide
        }

        List<commentaire> commentaires = new ArrayList<>();
        String query = "SELECT  contenu FROM Commentaire WHERE numP = ?";  // Exemple de requête pour récupérer les commentaires du projet
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, projectId);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                commentaire commentaire = new commentaire();
                commentaire.setContent(resultSet.getString("contenu"));
                // Remplir les autres attributs du commentaire (user, etc.)
                commentaires.add(commentaire);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return commentaires;  // Retourner la liste des commentaires
    }
*/
}


