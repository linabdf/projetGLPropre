package com.example.demo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.demo.DatabaseManager.connection;

@Service
public class TacheService {
    @Autowired
    private final DatabaseManager databaseManager;

    public TacheService(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public static String generateNextProjectId(Connection conn) {
        String newId = "T001";
        String query = "SELECT MAX(numT)AS lastId FROM Tache";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                String lastId = rs.getString("lastId");
                if (lastId != null) {
                    int numPart = Integer.parseInt(lastId.substring(1));  // Extraire la partie numérique
                    newId = "T" + String.format("%03d", numPart + 1);  // Incrémenter et formater
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newId;
    }


    public boolean insererTache(String nomT, String dateEch, String priorite, String status, String numP, String numU) {
        String query = "INSERT INTO Tache(numT,nomT,dateEch,priorite,status,numP,numU)VALUES(?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            String newidP = generateNextProjectId(connection);
            stmt.setString(1, newidP); // id
            stmt.setString(2, nomT); // name

            stmt.setDate(3, Date.valueOf(dateEch)); //start_date
            stmt.setString(4, priorite); // end_date
            stmt.setString(5, status);
            stmt.setString(6, numP);
            stmt.setString(7, numU);


            // user_id// progress

            int rowsAffected = stmt.executeUpdate();
            System.out.println(rowsAffected + " ligne projet insérée(s) !");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<Map<String, String>> getTacheByProjectName(String projectName) {
        List<Tache> taches = new ArrayList<>();
        List<Map<String, String>> resultList = new ArrayList<>();
        String query = "SELECT nomT, dateEch, priorite, status, u.mail, p.nomP " +
                "FROM Tache t " +
                "JOIN Utilisateur u ON u.numU = t.numU " +
                "JOIN Projet p ON p.numP = t.numP " +
                "WHERE p.nomP = ?";
        System.out.println("Fetching tasks for project: " + projectName);

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, projectName);  // Remplir le paramètre avec le nom du projet
            ResultSet resultSet = stmt.executeQuery();


            while (resultSet.next()) {
                // Récupérer les valeurs directement
                String nomT = resultSet.getString("nomT");
                String priorite = resultSet.getString("priorite");
                String status = resultSet.getString("status");
                String dateEch = String.valueOf(resultSet.getDate("dateEch"));
                String mail = resultSet.getString("mail");
                String nomP = resultSet.getString("nomP");


                // Stocker les résultats dans une Map pour chaque ligne
                Map<String, String> row = new HashMap<>();
                row.put("nomT", nomT);
                row.put("priorite", priorite);
                row.put("status", status);
                row.put("dateEch", dateEch);
                row.put("mail", mail);
                row.put("nomP", nomP);


                // Ajouter la ligne dans la liste des résultats
                resultList.add(row);

                // Afficher ou manipuler directement les données
                System.out.println(row);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Ajoutez plus de gestion des erreurs si nécessaire
            throw new RuntimeException("Erreur lors de la récupération des tâches", e);
        }
        return resultList;
    }

    public String getTacheid(String tacheName) {
        String query = "SELECT numT FROM Tache WHERE nomT = ?";
        try (PreparedStatement stmtSelect = connection.prepareStatement(query)) {
            stmtSelect.setString(1, tacheName); // Injecter le paramètre dans la requête
            try (ResultSet rs = stmtSelect.executeQuery()) {
                // Parcours des résultats
                if (rs.next()) {
                    return rs.getString("numT"); // Retourner l'identifiant de la tâche
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération de l'ID de la tâche", e);
        }
        return ""; // Retourner une chaîne vide si aucun résultat
    }


    public boolean verifierache(String tacheName) {

        String query = "SELECT numT " +
                "FROM Tache " +
                "WHERE nomT = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, tacheName); // Remplir le paramètre `nomT`
            //    ResultSet rs = stmtSelect.executeQuery(query);
            // Parcours des résultats et affichage
            try (ResultSet rs = stmt.executeQuery()) {
                // Si un résultat est retourné, la tâche existe
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la vérification de la tâche", e);
        }
    }

    public boolean inserDependancesTcahe(String Tache, String Dependancestache) {
        String tacheid = getTacheid(Tache);
        String dependancesId = getTacheid(Dependancestache);

        // Vérification si la dépendance existe déjà
        if (checkDependencyExists(tacheid, dependancesId)) {
            System.out.println("La dépendance existe déjà pour cette tâche.");
            return false; // Ou vous pouvez retourner true si vous ne souhaitez pas signaler l'existence
        }

        String query = "INSERT INTO Tache_dependance(numT, depT) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, tacheid);          // ID de la tâche
            stmt.setString(2, dependancesId);   // ID de la dépendance
            int rowsAffected = stmt.executeUpdate();
            System.out.println(rowsAffected + " ligne(s) insérée(s) dans Tache_dependance.");
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'insertion des dépendances", e);
        }
    }

    // Méthode pour vérifier si la dépendance existe déjà
    private boolean checkDependencyExists(String tacheid, String dependancesId) {
        String query = "SELECT COUNT(*) FROM Tache_dependance WHERE numT = ? AND depT = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, tacheid);
            stmt.setString(2, dependancesId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Si le compte est supérieur à 0, la dépendance existe déjà
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la vérification de la dépendance", e);
        }
        return false;
    }

public List<String> getDependantesTache(String tacheid) {
    List<String> dependantes = new ArrayList<>();
    String query = "SELECT t2.nomT " +
            "FROM Tache_dependance d " +
            "JOIN Tache t1 ON t1.numT = d.numT " +
            "JOIN Tache t2 ON t2.numT = d.depT " +
            "WHERE t1.numT = ?";
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setString(1, tacheid);  // Set the task ID for which you want to find dependent tasks

        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                dependantes.add(rs.getString("nomT"));  // Add each dependent task to the list
            }
        }
    } catch (SQLException e) {
        throw new RuntimeException("Erreur lors de la récupération des tâches dépendantes", e);
    }

    return dependantes;
}

 /*
public List<String> getDependantesTache(String tacheid) {
    List<String> dependantes = new ArrayList<>();
    String query = "SELECT d.nomT FROM Tache d " +  // Notez l'espace avant "JOIN"
            "JOIN  Tache_dependance  t ON t.numT = d.numT " +      // Notez l'espace après "d.numT"
            "WHERE d.numT = ?";

    try (PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setString(1, tacheid);  // Set the task ID for which you want to find dependent tasks

        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                dependantes.add(rs.getString("nomT"));  // Add each dependent task to the list
            }
        }
    } catch (SQLException e) {
        throw new RuntimeException("Erreur lors de la récupération des tâches dépendantes", e);
    }

    return dependantes;
}
*/
}