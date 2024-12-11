package com.example.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.util.AssertionErrors.assertFalse;

public class TacheServiceTestIntegration {

    private DatabaseManager databasemanager = new DatabaseManager("jdbc:mysql://sql7.freesqldatabase.com:3306/sql7743283","sql7743283","aC2kDrfGsk");
    private ProjectService projetservice = new ProjectService(databasemanager);
    private UserService userservice = new UserService(databasemanager);
    public TacheServiceTestIntegration()throws SQLException {
        databasemanager.connexion();
        databasemanager.getConnexion().setAutoCommit(false);
    }
    private TacheService Tache = new TacheService(databasemanager,projetservice,userservice);
    @BeforeEach
    void setupDatabase() {
        userservice.insererutilisateur("manager abcd", "testuser@example.com", "1234", "manager");
        String usermanager = userservice.getUserIdByEmail("testuser@example.com");
        // Insérer un projet de test
        projetservice.insererProjet("ProjectTest", "DescriptionTest", "2024-01-01", "2024-12-31", 50,  usermanager);

        // Insérer un utilisateur de test
        userservice.insererutilisateur("Test User", "testuser1@example.com", "password", "user");

        // Récupérer les IDs nécessaires
        String projectId = projetservice.getProjectIdByName("ProjectTest");
        String userId = userservice.getUserIdByEmail("testuser1@example.com");

        // Insérer des tâches liées au projet
        Tache.insererTache("Tache1000", "2024-05-01", "High", "unstarted", projectId, userId);
        Tache.insererTache("Tache20000", "2024-06-01", "Medium", "unstarted", projectId, userId);
    }
    @Test
    void insererTacheTest(){
        userservice.insererutilisateur("manager abcd", "task343@example.com", "1234", "manager");
        String numM = userservice.getUserIdByEmail("task343@example.com");
        projetservice.insererProjet("Project201", "Project Description", "2024-12-12", "2025-12-22", 50, numM);
        String numP = projetservice.getProjectIdByName("Project201");
       System.out.println("nump"+numP);

        userservice.insererutilisateur("developpeurabc abcd", "task344@example.com", "1234", "user");
        String numU = userservice.getUserIdByEmail("task344@example.com");

        boolean res= Tache.insererTache("tacheM", "2024-12-12","moyenne", "unstarted",numP,numU);
      //  boolean res1= TacheService.insererTache("tache", "2024-12-12","moyenne", "unstarted","",numU);
        assertTrue(res,"la tache est inserer avec succées");
        //Assertions.assertFalse(res1,"l'insertion est echoué");


    }
    @Test
    void insererDependanceTest(){
        // Vérifier l'insertion de dépendances
        boolean res = Tache.inserDependancesTcahe("Tache1000", "Tache20000");
        assertTrue(res, "L'insertion de la dépendance doit réussir.");

        // Réessayer d'insérer la même dépendance pour tester les doublons
        //boolean resDuplicate = Tache.inserDependancesTcahe("Tache1000", "Tache1000");
        //Assertions.assertFalse(resDuplicate, "La dépendance existe déjà, l'insertion doit échouer.");
    }
    @Test
    void modifierStatut_SuccessfulUpdate() {
        // Cas où TacheB est terminée et TacheA peut passer de "unstarted" à "begin"
        boolean result = Tache.ModifierStatut("Tache1000", "begin");
        assertTrue(result, "Le statut de la tâche doit être mis à jour avec succès.");

    }
    @Test
    void modifierStatut_Negative() {
        // Cas où TacheB est terminée et TacheA peut passer de "unstarted" à "begin"

        boolean result1 = Tache.ModifierStatut("Tache1000", "finished");
        assertFalse( "Le statut de la tâche doit être mis à jour avec succès.",result1);

    }


}

