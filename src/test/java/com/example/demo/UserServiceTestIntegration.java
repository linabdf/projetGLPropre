package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;


import java.sql.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;



@SpringBootTest(classes = DemoApplication.class)
public class UserServiceTestIntegration {

    private DatabaseManager databasemanager = new DatabaseManager("jdbc:mysql://sql7.freesqldatabase.com:3306/sql7743283","sql7743283","aC2kDrfGsk");


    private UserService userservice = new UserService(databasemanager);




    public  UserServiceTestIntegration() throws SQLException{
        databasemanager.connexion();
        databasemanager.getConnexion().setAutoCommit(false);
    }



    @Test
    public void testGetUserRole() throws SQLException {
        // Test de la méthode getUser
        databasemanager.connexion();

        userservice.insererutilisateur("Jonathan Gayraud", "Gayraudjonathan467@gmail.com", "1234", "manager");
        String role = userservice.getUserRole("Gayraudjonathan467@gmail.com", "1234");
        assertEquals("manager", role, "Le rôle de l'utilisateur doit être 'manager'.");

        String role2 = userservice.getUserRole("Gayraudjonathan467@gmail.com", "1234");
        assertNotEquals("admin", role2, "Le rôle de l'utilisateur ne doit pas être 'admin'.");

    }


    @Test
    public void testGenerateNextId() throws SQLException {
        // Test de la génération du prochain ID
        databasemanager.connexion();
        String newId = userservice.generateNextId();
        userservice.insererutilisateur("Jane Doe", "test@example.com", "password123", "user");
        String newId2 = userservice.generateNextId();
        assertNotEquals(newId2, newId, "L'ID généré doit different'.");

    }





    @Test
    public void testConnectionUtilisateur() throws SQLException {
        // Test de la méthode connectionutilisateur
        databasemanager.connexion();

        userservice.insererutilisateur("Jonathan Gayraud", "Gayraudjonathan47@gmail.com", "1234", "manager");
        boolean result = userservice.connectionutilisateur("Gayraudjonathan47@gmail.com", "1234");
        assertTrue(result, "L'utilisateur doit être connecté avec succès.");

        boolean result2 = userservice.connectionutilisateur("john@example.com", "1234");
        assertFalse(result2, "L'utilisateur ne doit pas etre connecté.");


    }



    @Test
    public void testVerifierUtilisateur() throws SQLException {
        // Test de la méthode verifierutilisateur
        boolean result = userservice.verifierutilisateur("john@example.com");
        assertTrue(result, "L'email doit etre disponible.");

        boolean result2 = userservice.verifierutilisateur("Gayraudjonathan47@gmail.com");
        assertFalse(result2, "L'email doit être déjà pris.");
    }


    @Test
    public void testModifierProfile() throws SQLException {
        // Test de la méthode modifierprofile
        boolean result = userservice.modifierProfil("Gayraudjonathan47@gmail.com","Denis","Jaque","JaqueDenis@gmail.com","Jaque123");
        assertTrue(result, "Le profil doit être modifié avec succès.");

        boolean result2 = userservice.modifierProfil("Gayraudjonathan47@gmail.com","Denis","Jaque","jane@example.com","Jaque123");
        assertFalse(result2, "Le profil ne doit pas être modifié car l'addresse mail est deja utilisé.");


    }

    @Test
    public void testInsererUtilisateur() throws SQLException {
        // Test de la méthode insererutilisateur
        boolean result = userservice.insererutilisateur("Jane Doe", "jane@example.com", "password123", "user");
        assertTrue(result, "L'utilisateur doit être inséré avec succès.");

        boolean result2 = userservice.insererutilisateur("Jane Doe", "jane@example.com", "password123", "user");
        assertFalse(result2, "L'utilisateur ne doit pas être inséré car l'addresse mail est deja utilisé.");
    }





    @Test
    public void testgetUserbyEmail() throws SQLException {
        // Test de la méthode insererProjet
        String result = userservice.getUserIdByEmail("Gayraudjonathan47@gmail.com");
        String result2 = userservice.getUserIdByEmail("Gayraudjonathan47@gmail.com");
        assertEquals(result2,result, "Le numU doit etre U019");

        String result4 = userservice.getUserIdByEmail("Gayraudjonathan47@gmail.com");
        assertNotEquals("U002",result4, "Le numU ne doit pas etre U002");

        String result3 = userservice.getUserIdByEmail("jonathan47@gmail.com");
        assertEquals(null,result3, "Le numU doit etre null car l'utilisateur n'existe pas.");
    }


    @Test
    public void getDeveloppeurIdByEmail() throws SQLException {

        String result = userservice.getDeveloppeurIdByEmail("jane@example.com");
        assertNotNull(result, "Le développeur doit exister dans la base de données.");

        String result2 = userservice.getDeveloppeurIdByEmail("developertest@example.com");
        assertNull(result2, "Le développeur ne doit pas exister dans la base de données.");

        String result3 = userservice.getDeveloppeurIdByEmail("Gayraudjonathan47@gmail.com");
        assertNull(result3, "Le résultat doit être nul pour un utilisateur qui n'est pas un developpeur");
    }
}
