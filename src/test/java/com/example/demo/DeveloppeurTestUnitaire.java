package com.example.demo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DeveloppeurTestUnitaire {

    @Test
    public void testDeveloppeurCreation() {
        // Création d'un développeur
        Developpeur developpeur = new Developpeur("John Doe", "john.doe@example.com", "password123");

        // Vérifications initiales
        assertEquals("John Doe", developpeur.getName(), "Le nom du développeur n'est pas correct.");
        assertEquals("john.doe@example.com", developpeur.getEmail(), "L'email du développeur n'est pas correct.");
        assertEquals("password123", developpeur.getPassword(), "Le mot de passe du développeur n'est pas correct.");
        assertNull(developpeur.getChefDeProjet(), "Le chef de projet devrait être null par défaut.");
    }

    @Test
    public void testAffectationChefDeProjet() {
        // Création d'un chef de projet
        Chef_Projet chefDeProjet = new Chef_Projet();
        chefDeProjet.setName("Alice Manager");

        // Création d'un développeur
        Developpeur developpeur = new Developpeur("John Doe", "john.doe@example.com", "password123");

        // Affectation du chef de projet au développeur
        developpeur.setChefDeProjet(chefDeProjet);

        // Vérifications après affectation
        assertNotNull(developpeur.getChefDeProjet(), "Le chef de projet ne devrait pas être null après affectation.");
        assertEquals("Alice Manager", developpeur.getChefDeProjetNom(), "Le nom du chef de projet est incorrect.");
    }

    @Test
    public void testChefDeProjetNull() {
        // Création d'un développeur
        Developpeur developpeur = new Developpeur("John Doe", "john.doe@example.com", "password123");

        // Vérification de l'absence de chef de projet
        assertNull(developpeur.getChefDeProjetNom(), "Le nom du chef de projet devrait être null par défaut.");
    }
}
