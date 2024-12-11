package com.example.demo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.util.List;

public class TacheTestUnitaire {


    private Developpeur developpeur = new Developpeur("John Doe","john.doe@example.com", "securepassword");
    private  Project project = new Project("Project Alpha", "Description tu Project Alpha", Date.valueOf("2024-01-01"), Date.valueOf("2024-12-31"), 10, developpeur);
    private Tache tacheParam = new Tache("Task Name", "2024-12-31", "High", "In Progress", project, developpeur, null);
    private Tache dependance = new Tache("Dependent Task");
    @Test
    void testDefaultConstructor() {
        Tache tacheDefault = new Tache();
        assertNotNull(tacheDefault);
    }

    @Test
    void testParameterizedConstructor() {


        project.setId("P001");
        developpeur.setId("D001");

        assertEquals("Task Name", tacheParam.getName());
        assertEquals("2024-12-31", tacheParam.getDueDate());
        assertEquals("High", tacheParam.getPriority());
        assertEquals("In Progress", tacheParam.getStatus());
        assertEquals("P001", tacheParam.getProject());
        assertEquals("D001", tacheParam.getDeveloppeur());
    }

    @Test
    void testIdGetterSetter() {
        Tache tache = new Tache();
        tache.setId("T001");
        assertEquals("T001", tache.getId());
    }

    @Test
    void testNameGetterSetter() {
        Tache tache = new Tache();
        tache.setName("Test Task");
        assertEquals("Test Task", tache.getName());
    }

    @Test
    void testDueDateGetterSetter() {
        Tache tache = new Tache();
        tache.setDueDate("2024-12-31");
        assertEquals("2024-12-31", tache.getDueDate());
    }

    @Test
    void testPriorityGetterSetter() {
        Tache tache = new Tache();
        tache.setPriority("High");
        assertEquals("High", tache.getPriority());
    }

    @Test
    void testStatusGetterSetter() {
        Tache tache = new Tache();
        tache.setStatus("Completed");
        assertEquals("Completed", tache.getStatus());
    }

    @Test
    void testProjectGetterSetter() {
        project.setId("P001");
        tacheParam.setProject(project);
        assertEquals("P001",tacheParam.getProject());
    }

    @Test
    void testDeveloppeurGetterSetter() {
        developpeur.setId("D001");
        tacheParam.setDeveloppeur(developpeur);
        assertEquals("D001", tacheParam.getDeveloppeur());
    }

    @Test
    void testDependancesGetterSetter() {


        tacheParam.setDependances(List.of(dependance));
        assertEquals(1, tacheParam.getDependances().size());
        assertEquals("Dependent Task", tacheParam.getDependances().get(0).getName());
    }

    @Test
    void testAddDependance() {
        Tache tache = new Tache();
        tache.addDependance(dependance);
        List<Tache> dependances = tache.getDependances();

        assertNotNull(dependances);
        assertEquals(1, dependances.size());
        assertEquals("Dependent Task", dependances.get(0).getName());
    }

    @Test
    void testToString() {
        Tache tache = new Tache();
        developpeur.setId("D001");
        tache.setName("Test Task");
        tache.setPriority("Medium");
        tache.setStatus("In Progress");
        tache.setDueDate("2024-12-31");
        tache.setDeveloppeur(developpeur);



        String expected = "Tache{name='Test Task', priority='Medium', status='In Progress', dueDate='2024-12-31', developpeur=D001}";
        assertEquals(expected, tache.toString());
    }
}

