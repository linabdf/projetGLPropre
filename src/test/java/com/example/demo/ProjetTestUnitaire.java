package com.example.demo;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;

public class ProjetTestUnitaire {

    @Test
    public void testConstructorWithParameters() {
        // Création d'un utilisateur pour le projet
        User user = new User("123");
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");
        user.setUserType("Admin");

        // Création d'un projet avec le constructeur complet
        String name = "Project Alpha";
        String description = "Description of Project Alpha";
        Date startDate = Date.valueOf("2024-01-01");
        Date endDate = Date.valueOf("2024-12-31");
        int progres = 50;

        Project project = new Project(name, description, startDate, endDate, progres, user);

        // Vérifications
        assertEquals(name, project.getName(), "Le nom du projet devrait correspondre à celui fourni.");
        assertEquals(description, project.getDescription(), "La description du projet devrait correspondre à celle fournie.");
        assertEquals(startDate.toString(), project.getStartDate(), "La date de début devrait correspondre à celle fournie.");
        assertEquals(endDate.toString(), project.getEndDate(), "La date de fin devrait correspondre à celle fournie.");
        assertEquals(progres, project.getProgres(), "Le progrès du projet devrait correspondre à celui fourni.");
        assertEquals(user, project.getUser(), "L'utilisateur associé au projet devrait correspondre à celui fourni.");
    }

    @Test
    public void testSettersAndGetters() {
        // Création d'un projet vide
        Project project = new Project(null, null, null, null, 0, null);

        // Création d'un utilisateur pour tester l'association
        User user = new User("456");
        user.setName("Jane Smith");

        // Modification des champs
        project.setId("1");
        project.setName("Project Beta");
        project.setDescription("Description of Project Beta");
        project.setStartDate("2023-01-01");
        project.setEndDate("2023-12-31");
        project.setProgres(75);
        project.setUser(user);

        // Vérifications
        assertEquals("1", project.getId(), "L'ID du projet devrait être '1'.");
        assertEquals("Project Beta", project.getName(), "Le nom du projet devrait être 'Project Beta'.");
        assertEquals("Description of Project Beta", project.getDescription(), "La description devrait être 'Description of Project Beta'.");
        assertEquals("2023-01-01", project.getStartDate(), "La date de début devrait être '2023-01-01'.");
        assertEquals("2023-12-31", project.getEndDate(), "La date de fin devrait être '2023-12-31'.");
        assertEquals(75, project.getProgres(), "Le progrès devrait être 75.");
        assertEquals(user, project.getUser(), "L'utilisateur associé devrait être 'Jane Smith'.");
    }

    @Test
    public void testUserAssociation() {
        // Création d'un utilisateur
        User user = new User("789");
        user.setName("Alice");
        user.setEmail("alice@example.com");

        // Création d'un projet et association avec l'utilisateur
        Project project = new Project(null, null, null, null, 0, null);
        project.setUser(user);

        // Vérification de l'association
        assertEquals(user, project.getUser(), "L'utilisateur associé au projet devrait être 'Alice'.");
        assertEquals("alice@example.com", project.getUser().getEmail(), "L'email de l'utilisateur associé devrait être 'alice@example.com'.");
    }

    @Test
    public void testDefaultValues() {
        // Création d'un projet vide
        Project project = new Project();

        // Vérification des valeurs par défaut
        assertNull(project.getId(), "L'ID du projet devrait être null par défaut.");
        assertNull(project.getName(), "Le nom du projet devrait être null par défaut.");
        assertNull(project.getDescription(), "La description devrait être null par défaut.");
        assertNull(project.getStartDate(), "La date de début devrait être null par défaut.");
        assertNull(project.getEndDate(), "La date de fin devrait être null par défaut.");
        assertEquals(0, project.getProgres(), "Le progrès devrait être 0 par défaut.");
        assertNull(project.getUser(), "L'utilisateur associé devrait être null par défaut.");
    }

    @Test
    public void testInvalidProgres() {
        // Création d'un projet
        Project project = new Project(null, null, null, null, 0, null);

        // Vérification avec des valeurs de progrès invalides
        project.setProgres(-10); // Progrès négatif
        assertEquals(-10, project.getProgres(), "Le progrès négatif devrait être accepté par défaut (ou vérifié dans la logique métier).");

        project.setProgres(120); // Progrès supérieur à 100
        assertEquals(120, project.getProgres(), "Le progrès supérieur à 100 devrait être accepté par défaut (ou vérifié dans la logique métier).");
    }
}