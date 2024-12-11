package com.example.demo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Service
public class TacheService {

    @Autowired
    private final DatabaseManager databaseManager;

    public TacheService(DatabaseManager databasemanager, ProjectService projetservice, UserService userservice) {

        this.databaseManager = databasemanager;


    }
    public String generateNextProjectId() {
        String newId = "T001";
        String query = "SELECT MAX(numT)AS lastId FROM Tache";
        try (Statement stmt = databaseManager.getConnexion().createStatement(); ResultSet rs = stmt.executeQuery(query)) {
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
        try (PreparedStatement stmt = databaseManager.getConnexion().prepareStatement(query)) {
            String newidP = generateNextProjectId();
            stmt.setString(1, newidP); // id
            stmt.setString(2, nomT); // name

            stmt.setDate(3, Date.valueOf(dateEch)); //start_date
            stmt.setString(4, priorite); // end_date
            stmt.setString(5, status);
            stmt.setString(6, numP);
            stmt.setString(7, numU);
            int rowsAffected = stmt.executeUpdate();
            System.out.println(rowsAffected + " ligne projet insérée(s) !");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Map<String, String>> getTacheByProjectName(String projectName) {
        List<Map<String, String>> resultList = new ArrayList<>();
        String query = "SELECT nomT, dateEch, priorite, status, u.mail, p.nomP " +
                "FROM Tache t " +
                "JOIN Utilisateur u ON u.numU = t.numU " +
                "JOIN Projet p ON p.numP = t.numP " +
                "WHERE p.nomP = ?";
        System.out.println("Fetching tasks for project: " + projectName);

        try (PreparedStatement stmt = databaseManager.getConnexion().prepareStatement(query)) {
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
        try (PreparedStatement stmtSelect =databaseManager.getConnexion().prepareStatement(query)) {
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
        try (PreparedStatement stmt = databaseManager.getConnexion().prepareStatement(query)) {
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
        try (PreparedStatement stmt = databaseManager.getConnexion().prepareStatement(query)) {
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
        try (PreparedStatement stmt = databaseManager.getConnexion().prepareStatement(query)) {
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
        try (PreparedStatement stmt = databaseManager.getConnexion().prepareStatement(query)) {
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

    public List<Map<String, String>> getTacheByProjectdeveloppeur(String numU) {
        List<Map<String, String>> resultList = new ArrayList<>();
        String query = "SELECT nomT, dateEch, priorite, status ,p.nomP " +
                "FROM Tache t " +
                "JOIN Utilisateur u ON u.numU = t.numU " +
                "JOIN Projet p ON p.numP = t.numP " +
                "WHERE u.numU = ?";
        System.out.println("Fetching tasks for project: " + numU);

        try (PreparedStatement stmt = databaseManager.getConnexion().prepareStatement(query)) {
            stmt.setString(1, numU);  // Remplir le paramètre avec le nom du projet
            ResultSet resultSet = stmt.executeQuery();


            while (resultSet.next()) {
                // Récupérer les valeurs directement
                String nomT = resultSet.getString("nomT");
                String priorite = resultSet.getString("priorite");
                String status = resultSet.getString("status");
                String dateEch = String.valueOf(resultSet.getDate("dateEch"));
                String nomP = resultSet.getString("nomP");


                // Stocker les résultats dans une Map pour chaque ligne
                Map<String, String> row = new HashMap<>();
                row.put("nomT", nomT);
                row.put("priorite", priorite);
                row.put("status", status);
                row.put("dateEch", dateEch);
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
    public void mettreAJourProgresProjet(String projectId, double nouveauProgres) throws SQLException {
        String updateQuery = "UPDATE Projet SET progres = ? WHERE numP = ?";
        try (PreparedStatement stmt = databaseManager.getConnexion().prepareStatement(updateQuery)) {
            stmt.setDouble(1, nouveauProgres);
            stmt.setString(2, projectId);
            stmt.executeUpdate();
            System.out.println("Progrès mis à jour pour le projet : " + projectId);
        }
    }

    public boolean ModifierStatut(String tacheName, String nouveauStatut) {
        // Vérification de l'existence de la tâche et du statut actuel
        String querySelect = "SELECT status, numT FROM Tache WHERE nomT = ?";  // Ajout de numT pour utiliser dans la vérification des dépendances

        try (PreparedStatement stmtSelect = databaseManager.getConnexion().prepareStatement(querySelect)) {
            stmtSelect.setString(1, tacheName); // Remplir le paramètre `nomT`

            try (ResultSet rs = stmtSelect.executeQuery()) {
                if (rs.next()) {
                    // Récupération du statut actuel et du numT de la tâche principale
                    String statusActuel = rs.getString("status");
                    String numT = rs.getString("numT");  // Récupère le numT de la tâche principale

                    // Vérification des dépendances de la tâche
                    String querySelectDependances =
                            "SELECT t.status FROM Tache t " +
                                    "JOIN Tache_dependance d ON t.numT = d.depT " + // Jointure pour récupérer les tâches dépendantes
                                    "WHERE d.numT = ?"; // On sélectionne toutes les tâches dépendantes pour numT (notre tâche principale)

                    try (PreparedStatement stmtSelectDependances = databaseManager.getConnexion().prepareStatement(querySelectDependances)) {
                        stmtSelectDependances.setString(1, numT); // Utiliser numT ici pour récupérer les dépendances de la tâche principale

                        try (ResultSet rsDependances = stmtSelectDependances.executeQuery()) {
                            boolean dependancesFinies = true;

                            // Vérification que toutes les tâches dépendantes sont "finished"
                            while (rsDependances.next()) {
                                String statusDependant = rsDependances.getString("status");
                                System.out.println("Status de la dépendance : " + statusDependant);  // Affichage pour débogage

                                // Si une dépendance n'est pas "finished", on marque dependancesFinies comme false
                                if (!"finished".equals(statusDependant)) {
                                    dependancesFinies = false;
                                    break; // Dès qu'une dépendance n'est pas terminée, on arrête la vérification
                                }
                            }

                            // Si toutes les dépendances sont "finished" et que le statut actuel est valide
                            if (dependancesFinies &&
                                    (("unstarted".equals(statusActuel) && "begin".equals(nouveauStatut)) ||
                                            ("begin".equals(statusActuel) && "finished".equals(nouveauStatut)))) {

                                // Mise à jour du statut
                                String queryUpdate = "UPDATE Tache SET status = ? WHERE nomT = ?";


                                try (PreparedStatement stmtUpdate = databaseManager.getConnexion().prepareStatement(queryUpdate)) {
                                    stmtUpdate.setString(1, nouveauStatut); // Remplir le nouveau statut
                                    stmtUpdate.setString(2, tacheName); // Remplir le nom de la tâche

                                    int rowsUpdated = stmtUpdate.executeUpdate();
                                    if (rowsUpdated > 0) {
                                        // Recalculez le progrès du projet
                                        String projectId = getProjectIdByTask(tacheName);
                                        System.out.println("projetid"+projectId);// Méthode pour récupérer l'ID du projet à partir de la tâche
                                        double nouveauProgres = progres(projectId); // Recalcule le progrès
                                        mettreAJourProgresProjet(projectId, nouveauProgres); // Met à jour le progrès du projet dans la BD
                                        return true;
                                    }
                                }

                            } else {
                                // Si les conditions ne sont pas remplies (par exemple, une dépendance n'est pas terminée)
                                return false;
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la vérification ou de la mise à jour de la tâche", e);
        }
        return false; // Retourne false si la tâche n'existe pas ou si les dépendances ne sont pas toutes "finished"
    }
    // Méthode pour récupérer l'ID du projet associé à une tâche
    private String getProjectIdByTask(String taskId) throws SQLException {
        String query = "SELECT numP FROM Tache WHERE nomT = ?";
        try (PreparedStatement stmt = databaseManager.getConnexion().prepareStatement(query)) {
            stmt.setString(1, taskId);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("numP");
                }
            }
        }
        return null;
    }

    public int afficherNombretache(String numP) throws SQLException {
        int count = 0;
        String checkQuery = "SELECT COUNT(*) FROM Tache WHERE numP = ?";
        try (PreparedStatement checkStmt = databaseManager.getConnexion().prepareStatement(checkQuery)) {
            checkStmt.setString(1, numP);
            ResultSet resultSet = checkStmt.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
            return count;
        }
    }
    public int afficherNombretachefini(String numP) throws SQLException {
        int count = 0;
        String checkQuery = "SELECT COUNT(*) FROM Tache WHERE numP = ? and status = 'finished'";
        // String statusDependant = rsDependances.getString("status");
        try (PreparedStatement checkStmt = databaseManager.getConnexion().prepareStatement(checkQuery)) {
            checkStmt.setString(1, numP);
            ResultSet resultSet = checkStmt.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
            return count;
        }
    }

    public double progres(String numP) throws SQLException {
        int nbtache = afficherNombretache(numP); // Nombre total de tâches
        int nbtachefini = afficherNombretachefini(numP); // Nombre de tâches terminées

        // Éviter la division par zéro
        if (nbtache == 0) {
            return 0.0; // Aucun progrès si aucune tâche
        }

        // Calculer le progrès en pourcentage
        double progres = ((double) nbtachefini / nbtache) * 100;
        return progres;
    }


}