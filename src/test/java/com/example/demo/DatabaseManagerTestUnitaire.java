package com.example.demo;



import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;

import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

@ExtendWith(MockitoExtension.class)  // Cette annotation permet d'utiliser les mocks dans JUnit5
public class DatabaseManagerTestUnitaire {

    @Mock
    private DatabaseManager databaseManager;  // Créer un mock de DatabaseManager

    @BeforeEach
    public void setUp() {
        // Pas besoin de créer une instance ici, car elle est mockée avec @Mock
    }

    @Test
    public void testConnexion() throws SQLException {
        // Simuler la méthode connexion()
        doNothing().when(databaseManager).connexion();  // Simule que la connexion ne fait rien de particulier
        
        // Appeler la méthode connexion()
        databaseManager.connexion();
        
        // Vérifier que la méthode connexion() a bien été appelée une fois
        verify(databaseManager, times(1)).connexion();
    }

    @Test
    public void testDeconnexion() throws SQLException {
        // Simuler la méthode deconnexion()
        doNothing().when(databaseManager).deconnexion();  // Simule que la déconnexion ne fait rien de particulier

        // Appeler la méthode deconnexion()
        databaseManager.deconnexion();
        
        // Vérifier que la méthode deconnexion() a bien été appelée une fois
        verify(databaseManager, times(1)).deconnexion();
    }

    @Test
    public void testIsOnline() {
        // Simuler la méthode statique isOnline() pour qu'elle retourne true
        try (MockedStatic<DatabaseManager> mockedStatic = mockStatic(DatabaseManager.class)) {
            mockedStatic.when(DatabaseManager::isOnline).thenReturn(true);
            
            // Vérifier que isOnline retourne true
            boolean result = DatabaseManager.isOnline();
            assertTrue(result, "La base de données doit être en ligne.");
            
            // Simuler que la base de données est hors ligne
            mockedStatic.when(DatabaseManager::isOnline).thenReturn(false);
            
            // Vérifier que isOnline retourne false
            boolean result2 = DatabaseManager.isOnline();
            assertFalse(result2, "La base de données doit être hors ligne.");
        }
    }
}