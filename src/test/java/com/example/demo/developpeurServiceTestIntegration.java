package com.example.demo;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

@SpringBootTest(classes = DemoApplication.class)
public class developpeurServiceTestIntegration {

    private DatabaseManager databasemanager;
    private ProjectService projetservice;
    private UserService userservice;
    private DeveloppeurService developpeurService;

    public developpeurServiceTestIntegration() throws SQLException {
        databasemanager = new DatabaseManager(
                "jdbc:mysql://sql7.freesqldatabase.com:3306/sql7743283",
                "sql7743283",
                "aC2kDrfGsk"
        );
        projetservice = new ProjectService(databasemanager);
        userservice = new UserService(databasemanager);
        developpeurService = new DeveloppeurService(databasemanager);
    }

    @BeforeEach
    public void setUp() throws SQLException {
        databasemanager.connexion();
        databasemanager.getConnexion().setAutoCommit(false); // Désactiver le commit automatique
    }

    @AfterEach
    public void tearDown() throws SQLException {
        databasemanager.getConnexion().rollback(); // Annuler toutes les modifications après chaque test
        databasemanager.getConnexion().close();
    }

    @Test
    public void testGetDeveloperIdByEmail_Success() {
        // Insérer un utilisateur et un projet
        userservice.insererutilisateur("test test", "test@gmail.com", "123", "user");
        String numU = userservice.getUserIdByEmail("test@gmail.com");

        projetservice.insererProjet(
                "Project47",
                "Project Description",
                "2024-11-22",
                "2025-11-22",
                50,
                "U002"
        );
        String numP = projetservice.getProjectIdByName("Project47");

        // Associer le développeur au projet
        projetservice.addDeveloperToProject(numU, numP);

        // Récupérer l'ID du développeur
        String developerId = developpeurService.getDeveloperIdByEmail("test@gmail.com");

        // Vérifier les assertions
        assertNotNull(developerId, "L'identifiant du développeur ne doit pas être nul.");
        assertEquals(numU, developerId, "L'identifiant du développeur doit correspondre à " + numU);
    }

    @Test
    public void testGetDeveloperIdByEmail_Failure() {
        // Cas où l'utilisateur n'existe pas
        String developerId = developpeurService.getDeveloperIdByEmail("nonexistent@gmail.com");

        // Vérifier que l'ID est nul
        assertNull(developerId, "L'identifiant du développeur doit être nul pour un email inexistant.");
    }

    @Test
    public void testAddDeveloperToProjectAndVerify() {
        // Insérer un utilisateur et un projet
        userservice.insererutilisateur("test dev", "dev@example.com", "password", "user");
        String numU = userservice.getUserIdByEmail("dev@example.com");

        projetservice.insererProjet(
                "Project99",
                "Another Project",
                "2024-01-01",
                "2024-12-31",
                100,
                "U003"
        );
        String numP = projetservice.getProjectIdByName("Project99");

        // Associer le développeur au projet
        boolean result = projetservice.addDeveloperToProject(numU, numP);

        // Vérifier l'ajout
        assertTrue(result, "Le développeur doit être ajouté au projet avec succès.");

        // Vérifier que le développeur est bien associé
        String developerId = developpeurService.getDeveloperIdByEmail("dev@example.com");
        assertEquals(numU, developerId, "L'identifiant du développeur doit correspondre après association.");
    }
}
