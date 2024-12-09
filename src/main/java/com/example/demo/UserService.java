package com.example.demo;

import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//import static com.example.demo.DatabaseManager.connection;
import static com.example.demo.DatabaseManager.connection;
@Service
public class UserService {
    private final DatabaseManager databaseManager;

    public UserService(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public String getUser(String email, String mdp) {

        // Requête SELECT pour récupérer les utilisateurs
        String selectQuery = "SELECT role FROM Utilisateur WHERE mail = ? AND motdepasse = ?";
        String role = "";

        // Afficher les données après insertion
        try (PreparedStatement stmtSelect = connection.prepareStatement(selectQuery)) {

            stmtSelect.setString(1, email);
            stmtSelect.setString(2, mdp);
            ResultSet rs = stmtSelect.executeQuery();

            // Parcours des résultats et affichage
            System.out.println("Liste des utilisateurs :");
            while (rs.next()) {
                role = rs.getString("role");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return role;
    }


    public String getRole(String email) {

        // Requête SELECT pour récupérer les utilisateurs
        String selectQuery = "SELECT role FROM Utilisateur WHERE mail = ?";
        String role = "";

        // Afficher les données après insertion
        try (PreparedStatement stmtSelect = connection.prepareStatement(selectQuery)) {

            stmtSelect.setString(1, email);

            ResultSet rs = stmtSelect.executeQuery();

            // Parcours des résultats et affichage
            System.out.println("Liste des utilisateurs :");
            while (rs.next()) {
                role = rs.getString("role");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return role;
    }


    // Méthode pour obtenir le dernier ID utilisé et générer le suivant
    public static String generateNextId(Connection conn) {
        String newId = "U001"; // Valeur par défaut si la table est vide

        String query = "SELECT MAX(numU) AS lastId FROM Utilisateur";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                String lastId = rs.getString("lastId");

                // Si un ID a été trouvé, extraire le numéro et incrémenter
                if (lastId != null) {
                    int numPart = Integer.parseInt(lastId.substring(1));  // Extraire la partie numérique
                    newId = "U" + String.format("%03d", numPart + 1);  // Incrémenter et formater
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return newId;
    }

    public boolean connectionutilisateur(String email, String mdp) {

        // Requête SELECT pour récupérer les utilisateurs
        String selectQuery = "SELECT * FROM Utilisateur WHERE mail = ? AND motdepasse = ?";
        //      String mail = "";
        //    String motdepasse = "";
        boolean mess = false;

        // Afficher les données après insertion
        try (PreparedStatement stmtSelect = connection.prepareStatement(selectQuery)) {
            stmtSelect.setString(1, email);
            stmtSelect.setString(2, mdp);
            ResultSet rs = stmtSelect.executeQuery();

            // Parcours des résultats et affichage
            System.out.println("Liste des utilisateurs :");
            // Parcours des résultats et vérification
            if (rs.next()) {
                // L'utilisateur existe avec cet email et mot de passe
                mess = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mess;
    }

    public boolean verifierutilisateur(String email) {
        Boolean mess = true;
        // Requête SELECT pour récupérer les utilisateurs
        String selectQuery = "SELECT mail FROM Utilisateur";
        // Afficher les données après insertion
        try (Statement stmtSelect = connection.createStatement()) {
            ResultSet rs = stmtSelect.executeQuery(selectQuery);
            // Parcours des résultats et affichage
            System.out.println("Liste des utilisateurs :");
            while (rs.next()) {
                String mail = rs.getString("mail");
                if (mail.equals(email)) {
                    mess = false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mess;
    }

    /*
        private String InsereTypeUtilisateur(String newUserID, connection connecion, String query1) {
            try (PreparedStatement stmt = connection.prepareStatement(query1)) {

                String newUserId = generateNextId(connection);  // Générer le nouvel ID
                // Remplacer les points d'interrogation (?) par les valeurs
                stmt.setString(1, newUserId);
                // Exécuter la requête
                int rowsAffected = stmt.executeUpdate();
                System.out.println(rowsAffected + " ligne(s) insérée(s) !");

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }*/
    public boolean insererutilisateur(String name, String email, String passworld, String role) {

        // Chaîne à séparer
        String fullName = name;

        // Utilisation de split() pour séparer à l'espace
        String[] parts = fullName.split(" ");

        // Requête SQL d'insertion
        String query = "INSERT INTO Utilisateur (numU,nomU, prenomU, mail, motdepasse, role) VALUES (?, ?, ?, ?, ?, ?)";


        // Préparation de la requête avec des paramètres
        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            String newUserId = generateNextId(connection);  // Générer le nouvel ID
            // Remplacer les points d'interrogation (?) par les valeurs
            stmt.setString(1, newUserId); // numU
            stmt.setString(2, parts[0]); // nom
            stmt.setString(3, parts[1]); // prenom
            stmt.setString(4, email); // email
            stmt.setString(5, passworld); // mot_de_passe
            stmt.setString(6, role); // type_utilisateur
            if (role.equals("manager")) {
                // Exécuter la requête
                int rowsAffected = stmt.executeUpdate();
                System.out.println(rowsAffected + " ligne(s) insérée(s) !");
                String insertChefProjet = "INSERT INTO chef_Projet (numU) VALUES (?)";
                PreparedStatement psChefProjet = connection.prepareStatement(insertChefProjet);

                psChefProjet.setString(1, newUserId);
                psChefProjet.executeUpdate();
            }
            if (role.equals("user")) {
                // Exécuter la requête
                int rowsAffected = stmt.executeUpdate();
                System.out.println(rowsAffected + " ligne(s) insérée(s) !");
                String insertChefProjet = "INSERT INTO developpeur (numU) VALUES (?)";
                PreparedStatement psChefProjet = connection.prepareStatement(insertChefProjet);

                psChefProjet.setString(1, newUserId);
                psChefProjet.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


          /*  if (role.equals("manager")) {
                // Do something
        /*   String query1 = "INSERT INTO chef_Projet (numU) VALUES (?)";
            //
           databaseManager.InsereTypeUtilisateur(newUserId,connecion,query1);
*/
     /*           String query1 = "INSERT INTO developpeur (numU) VALUES (?)";
                // Préparation de la requête avec des paramètres
                try (PreparedStatement stmt = connection.prepareStatement(query1)) {

                    String newUserId = generateNextId(connection);  // Générer le nouvel ID
                    // Remplacer les points d'interrogation (?) par les valeurs
                    stmt.setString(1, newUserId);
                    // Exécuter la requête
                    int rowsAffected = stmt.executeUpdate();
                    System.out.println(rowsAffected + " ligne(s) insérée(s) !");

                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }*/
        // Requête SELECT pour récupérer les utilisateurs
        String selectQuery = "SELECT * FROM Utilisateur";

        // Afficher les données après insertion
        try (Statement stmtSelect = connection.createStatement()) {
            ResultSet rs = stmtSelect.executeQuery(selectQuery);

            // Parcours des résultats et affichage
            System.out.println("Liste des utilisateurs :");
            while (rs.next()) {
                String numU = rs.getString("numU");
                String nomU = rs.getString("nomU");
                String prenomU = rs.getString("prenomU");
                String mail = rs.getString("mail");
                String motDePasse = rs.getString("motdepasse");
                String role1 = rs.getString("role");

                System.out.println("ID: " + numU + "Nom: " + nomU + "Prenom: " + prenomU + ", Email: " + mail + ", MotDePasse: " + motDePasse + ", Type: " + role1);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;

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

    public String getDeveloppeurIdByEmail(String email) {
        // Étape 1 : Vérifier dans la table Utilisateur
        String queryUtilisateur = "SELECT numU FROM Utilisateur WHERE mail = ?";
        try (PreparedStatement stmtUtilisateur = connection.prepareStatement(queryUtilisateur)) {
            stmtUtilisateur.setString(1, email);
            ResultSet rsUtilisateur = stmtUtilisateur.executeQuery();

            if (rsUtilisateur.next()) {
                String numdev = rsUtilisateur.getString("numU");
                System.out.println("numU: " + numdev);

                // Étape 2 : Vérifier si numU est présent dans la table Developpeur
                String queryDeveloppeur = "SELECT numU FROM developpeur WHERE numU = ?";
                try (PreparedStatement stmtDeveloppeur = connection.prepareStatement(queryDeveloppeur)) {
                    stmtDeveloppeur.setString(1, numdev);
                    ResultSet rsDeveloppeur = stmtDeveloppeur.executeQuery();

                    if (rsDeveloppeur.next()) {
                        System.out.println("numdev trouvé dans developpeur: " + numdev);
                        return numdev; // Retourne numU si trouvé dans Developpeur
                    } else {
                        System.out.println("numdev introuvable dans developpeur.");
                        return null; // Utilisateur trouvé, mais pas en tant que Développeur
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // retourne null si l'utilisateur n'est pas trouvé ou pas développeur
    }
    public boolean modifierProfil(String mail, String nom, String prenom, String email, String motDePasse) {
        String query = "UPDATE Utilisateur SET nomU = ?, prenomU = ?, mail = ?, motdepasse = ? WHERE mail = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            // Définir les paramètres
            stmt.setString(1, nom);
            stmt.setString(2, prenom);
            stmt.setString(3, email);
            stmt.setString(4, motDePasse);
            stmt.setString(5, mail);

            // Exécuter la requête
            int rowsUpdated = stmt.executeUpdate();

            // Vérifier si une ligne a été mise à jour
            if (rowsUpdated > 0) {
                System.out.println("Profil mis à jour avec succès !");
                return true;
            } else {
                System.out.println("Aucun utilisateur trouvé avec cet ID.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public String getUserRole(String email, String mdp) {

        // Requête SELECT pour récupérer les utilisateurs
        String selectQuery = "SELECT role FROM Utilisateur WHERE mail = ? AND motdepasse = ?";
        String role = "";

        // Afficher les données après insertion
        try (PreparedStatement stmtSelect = connection.prepareStatement(selectQuery)) {

            stmtSelect.setString(1, email);
            stmtSelect.setString(2, mdp);
            ResultSet rs = stmtSelect.executeQuery();

            // Parcours des résultats et affichage
            System.out.println("Liste des utilisateurs :");
            while (rs.next()) {
                role = rs.getString("role");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return role;
    }

}