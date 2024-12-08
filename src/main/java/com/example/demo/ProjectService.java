package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.example.demo.DatabaseManager.connection;
import static com.example.demo.ProjectRepository.*;

@Service
public class ProjectService {
    @Autowired
    private final DatabaseManager databaseManager;
    public ProjectService(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }
    @Autowired
    private ProjectRepository ProjectRepository;

    // Génération d'un ID pour un nouveau projet
    public static String generateNextProjectId(Connection conn) {
        String newId = "P001";  // Valeur par défaut si la table est vide

        String query = "SELECT MAX(numP) AS lastId FROM Projet";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                String lastId = rs.getString("lastId");
                if (lastId != null) {
                    int numPart = Integer.parseInt(lastId.substring(1));  // Extraire la partie numérique
                    newId = "P" + String.format("%03d", numPart + 1);  // Incrémenter et formater
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newId;
    }

    public String getUserIdByEmail(String email) {
        String query = "SELECT numU FROM Utilisateur WHERE mail = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("numU");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // retourne null si l'utilisateur n'est pas trouvé
    }

    public boolean insererProjet(String name, String description, String startDate, String endDate, int progres, String userId) {
        String query = "INSERT INTO Projet (numP, nomP, description, dateDebP, dateFinP, progres, numU) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            String newidP = generateNextProjectId(connection);
            stmt.setString(1, newidP); // id
            stmt.setString(2, name); // name
           stmt.setString(3, description); // description
         stmt.setDate(4, Date.valueOf(startDate)); //start_date
            stmt.setDate(5, Date.valueOf(endDate)); // end_date
            stmt.setInt(6, progres);
            stmt.setString(7, userId); // user_id// progress
            int rowsAffected = stmt.executeUpdate();
            System.out.println(rowsAffected + " ligne projet insérée(s) !");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<Project> getProjectByUserId(String userId) {
        List<Project> projects = new ArrayList<>();
        String query = "SELECT numP, nomP, description,dateDebP,dateFinP, progres FROM Projet WHERE numU = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();


            while (rs.next()) {
                // Récupérer la chaîne de la base de données
                String startDateString = rs.getString("dateDebP");
                String endDateString = rs.getString("dateFinP");

                // Convertir les chaînes en objets Date
                Date startDate = Date.valueOf(startDateString); // Convertit la chaîne en Date
                Date endDate = Date.valueOf(endDateString); // Convertit la chaîne en Date
                User user = new User(userId);
                Project project = new Project(
                        rs.getString("nomP"),
                        rs.getString("description"),
                        startDate, // Date de début (objet Date)
                        endDate,   // Date de fin (objet Date)
                        rs.getInt("progres"),
                        new User(userId)
                );

                projects.add(project);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return projects;


    }
    public static List<Project> getProjectsByDeveloperId(String developerId) {
        // Cette méthode interroge la base de données pour récupérer les projets associés à un développeur
        String query = "SELECT nomP FROM Projet p " +
                "JOIN project_developpeur d ON d.numP = p.numP " +
                "WHERE d.numU = ?"; // Correction : d.numP => d.numD (si developerId correspond à numD)
        List<Project> projects = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, developerId); // Assignation de l'ID du développeur à la requête
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Project project = new Project(); // Créer un objet Project pour chaque ligne
                project.setName(rs.getString("nomP")); // Récupérer le nom du projet depuis le résultat
                projects.add(project);// Ajouter l'objet à la liste

            }
            System.out.println("projects"+projects);
        } catch (SQLException e) {
            e.printStackTrace(); // Afficher les erreurs éventuelles
        }

        return projects; // Retourner la liste des projets
    }
    public static String getProjectIdByName(String projectName) {
        String projectId = null;
        String query = "SELECT numP FROM Projet WHERE nomP = ?"; // Recherche par nom de projet

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, projectName); // Set le nom du projet
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                projectId = rs.getString("numP"); // Récupère l'ID du projet
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Gérer l'exception SQL
        }

        return projectId; // Retourne l'ID du projet, ou null si non trouvé
    }
    public static boolean addDeveloperToProject(String developerId, String projectId) {
        // Étape 1 : Vérifier si la ligne existe déjà dans la table project_developpeur
        String checkQuery = "SELECT COUNT(*) FROM project_developpeur WHERE numU = ? AND nump = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
            // Définir les paramètres de la requête
            checkStmt.setString(1, developerId);
            checkStmt.setString(2, projectId);

            // Exécuter la requête pour vérifier si une ligne existe déjà
            ResultSet resultSet = checkStmt.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                if (count > 0) {
                    // Si le compte est supérieur à 0, cela signifie que la ligne existe déjà
                    System.out.println("Le développeur est déjà associé à ce projet.");
                    return false;  // Retourner false car l'association existe déjà
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        // Étape 2 : Si l'association n'existe pas, insérer la nouvelle ligne
        String query = "INSERT INTO project_developpeur (numU, nump) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            // Définir les paramètres de la requête
            stmt.setString(1, developerId);  // ID du développeur
            stmt.setString(2, projectId);    // ID du projet

            // Exécuter l'insertion
            int rowsAffected = stmt.executeUpdate();

            // Retourner true si l'insertion a réussi (au moins une ligne insérée)
            if (rowsAffected > 0) {
                System.out.println("Développeur ajouté au projet avec succès.");
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<String> getDeveloppeuridByProjectid(String projectid) {
        List<String> developerIds = new ArrayList<>();

        String query = "SELECT u.mail " +
                "FROM Utilisateur u " +
                "JOIN developpeur d ON d.numU = u.numU " +
                "JOIN project_developpeur p ON p.numU = d.numU " +
                "WHERE p.nump = ?";
     /*   String query = "SELECT d.numU " +
                "FROM project_developpeur d " +
                "WHERE d.nump = ?";
       */
        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, projectid);
            try (ResultSet resultSet = stmt.executeQuery()) {
                // Parcourir les résultats et ajouter les numU à la liste
                while (resultSet.next()) {
                    String developerId = resultSet.getString("mail");
                    System.out.println("developerId"+developerId);
                    developerIds.add(developerId);
                }
                System.out.println("developerIds"+developerIds);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Ajoutez plus de gestion des erreurs si nécessaire
            throw new RuntimeException("Erreur lors de la récupération des développeurs", e);
        }

        // Si la liste est vide, cela signifie qu'il n'y a pas de développeur associé à ce projet
        if (developerIds.isEmpty()) {
            System.out.println("Aucun développeur n'est associé à ce projet.");
        }

        return developerIds;
    }

}


