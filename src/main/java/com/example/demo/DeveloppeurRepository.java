package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeveloppeurRepository extends JpaRepository<Developpeur, Long> {


    // Trouver un développeur par son email
    Optional<Developpeur> findByEmail(String email);

    Optional<Developpeur> findById(String developpeurId);
}
