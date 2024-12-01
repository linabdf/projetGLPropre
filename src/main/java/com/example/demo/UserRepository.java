package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
public interface UserRepository extends JpaRepository<User, String> {

  User findByEmail(String mail);
    boolean existsByEmail(String email);
    // Vous pouvez ajouter d'autres méthodes ici selon vos besoins
}
