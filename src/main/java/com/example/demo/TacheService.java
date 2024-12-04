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
                "JOIN Utilisateur u ON u.numU = t.numU "+
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
                String nomP=resultSet.getString("nomP");


                // Stocker les résultats dans une Map pour chaque ligne
                Map<String, String> row = new HashMap<>();
                row.put("nomT", nomT);
                row.put("priorite", priorite);
                row.put("status", status);
                row.put("dateEch", dateEch);
                row.put("mail", mail);
                row.put("nomP",nomP);


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


}