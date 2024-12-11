package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;


import java.sql.*;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;



@SpringBootTest(classes = DemoApplication.class)
public class ProjetServiceTestIntegration {

    private DatabaseManager databasemanager = new DatabaseManager("jdbc:mysql://sql7.freesqldatabase.com:3306/sql7743283","sql7743283","aC2kDrfGsk");



    private ProjectService projetservice = new ProjectService(databasemanager);
    private UserService userservice = new UserService(databasemanager);

    public  ProjetServiceTestIntegration() throws SQLException{
        databasemanager.connexion();
        databasemanager.getConnexion().setAutoCommit(false);
    }



    @Test
    public void testGenerateNextProjectId() throws SQLException {
        // Test de la génération du prochain ID
        databasemanager.connexion();
        String newId = projetservice.generateNextProjectId();
        projetservice.insererProjet("Project48", "Project Description", "2024-11-22", "2025-11-22", 50, "U002");
        System.out.println(newId);
        String newId2 =projetservice.generateNextProjectId();
        System.out.println(newId2);
        assertNotEquals(newId2, newId, "L'ID généré doit etre different'.");
    }


    @Test
    public void testInsererProjet() throws SQLException {
        // Test de la méthode insererProjet
        boolean result = projetservice.insererProjet("Project157", "Project Description", "2024-11-22", "2025-11-22", 50, "U002");
        assertTrue(result, "Le projet doit etre inseré ");

        boolean result2 = projetservice.insererProjet("Project157", "Project Description", "2024-11-22", "2025-11-22", 50, "U002");
        assertFalse(result2, "Le projet ne doit pas être inséré car le nom du projet est deja pris.");
    }


    @Test
    public void testgetNameProjet() throws SQLException {
        // Test de la méthode insererProjet
        boolean result = projetservice.getNameProjet("Project157");
        assertFalse(result, "Le nom du projet doit exister.");

        boolean result2 = projetservice.getNameProjet("Project46");
        assertTrue(result2, "Le nom du projet ne doit pas exister.");
    }


    @Test
    public void testgetUserbyEmail() throws SQLException {
        // Test de la méthode insererProjet
        String result = projetservice.getUserIdByEmail("Gayraudjonathan47@gmail.com");
        String result2 = projetservice.getUserIdByEmail("Gayraudjonathan47@gmail.com");
        assertEquals(result2,result, "Le numU doit etre U019");

        String result4 = projetservice.getUserIdByEmail("Gayraudjonathan47@gmail.com");
        assertNotEquals("U002",result4, "Le numU ne doit pas etre U002");

        String result3 = projetservice.getUserIdByEmail("jonathan47@gmail.com");
        assertEquals(null,result3, "Le numU doit etre null car l'utilisateur n'existe pas.");
    }


    @Test
    public void testGetProjectByUserId() throws SQLException {
        // Test de la méthode getProjectByUserId
        databasemanager.connexion();
        List<Project> projects = projetservice.getProjectByUserId("U004");
        assertNotNull(projects, "La liste des projets ne doit pas être nulle.");
        assertFalse(projects.isEmpty(), "La liste des projets doit contenir des projets.");
    }


    @Test
    public void  testaddDeveloperToProject() throws SQLException {
        projetservice.insererProjet("Project57", "Project Description", "2024-11-22", "2025-11-22", 50, "U002");
        String numP = projetservice.getProjectIdByName("Project157");

        userservice.insererutilisateur("Jonathan Gayraud", "test1@example.com", "1234", "user");
        String numU = userservice.getUserIdByEmail("test1@example.com");
        boolean result = projetservice.addDeveloperToProject(numU, numP);
        assertTrue(result,"Le developpeur doit avoir être ajouté au projet");

        boolean result2 = projetservice.addDeveloperToProject(numU, numP);
        assertFalse(result2,"Le developpeur doit déjà être ajouté au projet");

        boolean result3 = projetservice.addDeveloperToProject("U123", numP);
        assertFalse(result3,"Le developpeur ne doit pas être ajouté au projet car le developpeur n'existe pas");

        boolean result4 = projetservice.addDeveloperToProject(numU, "P123");
        assertFalse(result4,"Le developpeur ne doit pas être ajouté au projet car le projet n'existe pas");
    }


    @Test
    public void testgetDeveloppeuridByProjectid() throws SQLException {

        String numP = projetservice.getProjectIdByName("Project157");
        userservice.insererutilisateur("Jonathan Gayraud", "test2@example.com", "1234", "user");
        userservice.insererutilisateur("Jonathan Gayraud", "test4@example.com", "1234", "user");
        String numU = userservice.getUserIdByEmail("test2@example.com");
        String numU2 = userservice.getUserIdByEmail("test4@example.com");
        projetservice.addDeveloperToProject(numU, numP);
        projetservice.addDeveloperToProject(numU2, numP);

        List<String> developerIds = projetservice.getDeveloppeuridByProjectid(numP);

        assertNotNull(developerIds);
        assertEquals(2, developerIds.size());
        assertTrue(developerIds.contains("test2@example.com"));
        assertTrue(developerIds.contains("test4@example.com"));
        assertFalse(developerIds.contains("test1@example.com"));
    }


    @Test
    public void  testgetProjectIdByName() throws SQLException {
        projetservice.insererProjet("Project64", "Project Description", "2024-11-22", "2025-11-22", 50, "U002");
        String result = projetservice.getProjectIdByName("Project64");
        assertNotEquals("P101",result,"Le nom du projet doit etre Project64");
        assertNotEquals("Projet57",result,"Le nom du projet ne doit pas etre Projet57");
    }


    @Test
    public void  testgetProjectsByDeveloperId() throws SQLException {
        projetservice.insererProjet("Project49", "Project Description", "2024-11-22", "2025-11-22", 50, "U002");
        String numP = projetservice.getProjectIdByName("Project49");
        String numU = userservice.getUserIdByEmail("test2@example.com");
        projetservice.addDeveloperToProject(numU, numP);

        List<Project> projetIds = projetservice.getProjectsByDeveloperId(numU);

        assertNotNull(projetIds);
        assertEquals(2, projetIds.size());
        assertEquals("Project157", projetIds.get(0).getName());
        assertEquals("Project49", projetIds.get(1).getName());
    }


}

