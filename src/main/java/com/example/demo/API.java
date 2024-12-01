//appi
/*package com.example.demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import static io.netty.handler.codec.http.HttpHeaders.setDate;
//service

// L'API (Application Programming Interface) dans le contexte de votre code sert à fournir des services métiers qui interagissent avec la base de données
public class API {

   
   
    private String urlBase;
    private String username;
    private String password;
    static Connection connection;

    public API(String urlBase, String username, String password) {

        this.urlBase = urlBase;
        this.password = password;
        this.username = username;

    }

    public API() {

    }


    public Connection getConnexion() {
        return connection;
    }

    public void connexion() {
        if (!isOnline()) {
            try {

                connection = DriverManager.getConnection(this.urlBase, this.username, this.password);
                System.out.println("[DataBaseManager] Connexion Reusite");
                return;
            } catch (SQLException e) {
                System.out.println("[DataBaseManager] Erreur lors de la connection a la base de donné");
                System.out.println(e.getMessage());
            }
        }
    }


    public void deconnexion() {
        if (isOnline()) {
            try {
                connection.close();
                System.out.println("§a[DataBaseManager] Deconnexion Reusite");
                return;
            } catch (SQLException e) {
                System.out.println("[DataBaseManager] Erreur lors de la déconnection a la base de donné");
            }
        }
    }

    public boolean isOnline() {
        try {
            if ((connection == null) || (connection.isClosed())) {
                return false;
            }
            return true;
        } catch (SQLException e) {
            System.out.println("[DataBaseManager] Erreur lors de la verification de l'etat de la base");
        }
        return false;
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

            // Exécuter la requête
            int rowsAffected = stmt.executeUpdate();
            System.out.println(rowsAffected + " ligne(s) insérée(s) !");

        } catch (SQLException e) {
            e.printStackTrace();
        }


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

   public List<Project> getProjectByUserId(String userId) {
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

} */