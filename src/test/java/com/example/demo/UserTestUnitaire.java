package com.example.demo;



import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTestUnitaire {

    @Test
    public void testDefaultConstructor() {
        // Création d'un utilisateur avec le constructeur par défaut
        User user = new User();

        // Vérification des valeurs par défaut
        assertNull(user.getUserId(), "L'ID de l'utilisateur devrait être null par défaut.");
        assertNull(user.getName(), "Le nom de l'utilisateur devrait être null par défaut.");
        assertNull(user.getEmail(), "L'email de l'utilisateur devrait être null par défaut.");
        assertNull(user.getPassword(), "Le mot de passe de l'utilisateur devrait être null par défaut.");
        assertNull(user.getUserType(), "Le type d'utilisateur devrait être null par défaut.");
    }

    @Test
    public void testConstructorWithId() {
        // Création d'un utilisateur avec un ID spécifique
        String id = "123";
        User user = new User(id);

        // Vérification de l'ID
        assertEquals(id, user.getUserId(), "L'ID de l'utilisateur devrait correspondre à celui fourni.");
        assertNull(user.getName(), "Le nom de l'utilisateur devrait être null.");
        assertNull(user.getEmail(), "L'email de l'utilisateur devrait être null.");
        assertNull(user.getPassword(), "Le mot de passe de l'utilisateur devrait être null.");
        assertNull(user.getUserType(), "Le type d'utilisateur devrait être null.");
    }

    @Test
    public void testConstructorWithNameEmailPassword() {
        // Création d'un utilisateur avec le constructeur à 3 paramètres
        String name = "John Doe";
        String email = "john.doe@example.com";
        String password = "securepassword";
        User user = new User(name, email, password);

        // Vérification des champs
        assertEquals(name, user.getName(), "Le nom de l'utilisateur devrait correspondre à celui fourni.");
        assertEquals(email, user.getEmail(), "L'email de l'utilisateur devrait correspondre à celui fourni.");
        assertEquals(password, user.getPassword(), "Le mot de passe de l'utilisateur devrait correspondre à celui fourni.");
        assertNull(user.getUserId(), "L'ID de l'utilisateur devrait être null.");
        assertNull(user.getUserType(), "Le type d'utilisateur devrait être null.");
    }

    @Test
    public void testSettersAndGetters() {
        // Création d'un utilisateur
        User user = new User();

        // Modification et vérification des champs
        user.setUserId("456");
        assertEquals("456", user.getUserId(), "L'ID de l'utilisateur devrait être '456'.");

        user.setName("Jane Doe");
        assertEquals("Jane Doe", user.getName(), "Le nom de l'utilisateur devrait être 'Jane Doe'.");

        user.setEmail("jane.doe@example.com");
        assertEquals("jane.doe@example.com", user.getEmail(), "L'email de l'utilisateur devrait être 'jane.doe@example.com'.");

        user.setPassword("password123");
        assertEquals("password123", user.getPassword(), "Le mot de passe de l'utilisateur devrait être 'password123'.");

        user.setUserType("Admin");
        assertEquals("Admin", user.getUserType(), "Le type d'utilisateur devrait être 'Admin'.");
        
       
    }

    @Test
    public void testEquality() {
        // Création de deux utilisateurs avec les mêmes données
        User user1 = new User("789");
        user1.setName("Alice");
        user1.setEmail("alice@example.com");
        user1.setPassword("password");
        user1.setUserType("User");

        User user2 = new User("789");
        user2.setName("Alice");
        user2.setEmail("alice@example.com");
        user2.setPassword("password");
        user2.setUserType("User");

        // Vérification des champs individuellement
        assertEquals(user1.getUserId(), user2.getUserId(), "Les IDs des utilisateurs devraient être égaux.");
        assertEquals(user1.getName(), user2.getName(), "Les noms des utilisateurs devraient être égaux.");
        assertEquals(user1.getEmail(), user2.getEmail(), "Les emails des utilisateurs devraient être égaux.");
        assertEquals(user1.getPassword(), user2.getPassword(), "Les mots de passe des utilisateurs devraient être égaux.");
        assertEquals(user1.getUserType(), user2.getUserType(), "Les types des utilisateurs devraient être égaux.");
    }
}
