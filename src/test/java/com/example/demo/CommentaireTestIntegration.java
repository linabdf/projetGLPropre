package com.example.demo;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.sql.SQLException;

public class CommentaireTestIntegration {

    private DatabaseManager databasemanager = new DatabaseManager("jdbc:mysql://sql7.freesqldatabase.com:3306/sql7743283","sql7743283","aC2kDrfGsk");
    private ProjectService projetservice = new ProjectService(databasemanager);
    private UserService userservice = new UserService(databasemanager);
    public CommentaireTestIntegration()throws SQLException {
        databasemanager.connexion();
        databasemanager.getConnexion().setAutoCommit(false);
    }
    private CommentaireService Commentaire = new CommentaireService(databasemanager,projetservice,userservice);
    @Test
     void testaddcomment(){
        projetservice.insererProjet("Project47", "Project Description", "2024-11-22", "2025-11-22", 50, "U002");
        String numP = projetservice.getProjectIdByName("Projet47");

        userservice.insererutilisateur("Jonathan Gayraud", "test1@example.com", "1234", "user");
        String numU = userservice.getUserIdByEmail("test1@example.com");


        boolean res= Commentaire.addComment("Project47", "test1@example.com", "offf");
     //   boolean res1= Commentaire.addComment("gggggggg", "massil@gmail.com", "offf");
       // boolean res2= Commentaire.addComment("gggggggg", "mass@gmail.com", "offf");
        assertTrue(res,"succes");
       // assertFalse(res1,"succes");

    }

}
