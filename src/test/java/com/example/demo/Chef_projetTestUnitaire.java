package com.example.demo;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class Chef_projetTestUnitaire {

    @Test
    public void testChefProjetCreation() {
        // Création d'un chef de projet
        Chef_Projet chefDeProjet = new Chef_Projet("Alice Manager", "alice.manager@example.com", "securepassword");

        // Vérifications initiales
        assertEquals("Alice Manager", chefDeProjet.getName(), "Le nom du chef de projet est incorrect.");
        assertEquals("alice.manager@example.com", chefDeProjet.getEmail(), "L'email du chef de projet est incorrect.");
        assertEquals("securepassword", chefDeProjet.getPassword(), "Le mot de passe du chef de projet est incorrect.");
        assertNull(chefDeProjet.getDeveloppeurs(), "La liste des développeurs devrait être null par défaut.");
    }

    @Test
    public void testAffecterDeveloppeurs() {
        // Création d'un chef de projet
        Chef_Projet chefDeProjet = new Chef_Projet("Alice Manager", "alice.manager@example.com", "securepassword");

        // Création de développeurs
        Developpeur dev1 = new Developpeur("John Doe", "john.doe@example.com", "password123");
        Developpeur dev2 = new Developpeur("Jane Doe", "jane.doe@example.com", "password456");

        // Assignation du chef de projet aux développeurs
        dev1.setChefDeProjet(chefDeProjet);
        dev2.setChefDeProjet(chefDeProjet);

        // Ajout des développeurs à la liste
        List<Developpeur> developpeurs = new ArrayList<>();
        developpeurs.add(dev1);
        developpeurs.add(dev2);
        chefDeProjet.setDeveloppeurs(developpeurs);

        // Vérifications
        assertNotNull(chefDeProjet.getDeveloppeurs(), "La liste des développeurs ne devrait pas être null.");
        assertEquals(2, chefDeProjet.getDeveloppeurs().size(), "Le nombre de développeurs assignés est incorrect.");
        assertTrue(chefDeProjet.getDeveloppeurs().contains(dev1), "La liste des développeurs devrait contenir dev1.");
        assertTrue(chefDeProjet.getDeveloppeurs().contains(dev2), "La liste des développeurs devrait contenir dev2.");
    }

    @Test
    public void testChefProjetSansDeveloppeurs() {
        // Création d'un chef de projet sans développeurs
        Chef_Projet chefDeProjet = new Chef_Projet("Alice Manager", "alice.manager@example.com", "securepassword");

        // Vérifications
        assertNull(chefDeProjet.getDeveloppeurs(), "La liste des développeurs devrait être null si non initialisée.");
    }

    @Test
    public void testRetirerDeveloppeur() {
        // Création d'un chef de projet
        Chef_Projet chefDeProjet = new Chef_Projet("Alice Manager", "alice.manager@example.com", "securepassword");

        // Création de développeurs
        Developpeur dev1 = new Developpeur("John Doe", "john.doe@example.com", "password123");
        Developpeur dev2 = new Developpeur("Jane Doe", "jane.doe@example.com", "password456");

        // Assignation des développeurs
        List<Developpeur> developpeurs = new ArrayList<>();
        developpeurs.add(dev1);
        developpeurs.add(dev2);
        chefDeProjet.setDeveloppeurs(developpeurs);

        // Retirer un développeur
        developpeurs.remove(dev1);
        chefDeProjet.setDeveloppeurs(developpeurs);

        // Vérifications
        assertEquals(1, chefDeProjet.getDeveloppeurs().size(), "Le nombre de développeurs après retrait est incorrect.");
        assertFalse(chefDeProjet.getDeveloppeurs().contains(dev1), "La liste des développeurs ne devrait pas contenir dev1.");
        assertTrue(chefDeProjet.getDeveloppeurs().contains(dev2), "La liste des développeurs devrait contenir dev2.");
    }
}
