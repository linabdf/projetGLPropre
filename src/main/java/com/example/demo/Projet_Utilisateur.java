/*package com.example.demo;
import java.io.Serializable;

import jakarta.persistence.Table;

import jakarta.persistence.*;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Entity
@Table(name = "Projet_Utilisateur")
public class Projet_Utilisateur{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @ManyToOne
    @JoinColumn(name = "numP", referencedColumnName = "id", nullable = false)
    private Project project;
    @ManyToOne
    @JoinColumn(name = "numU", referencedColumnName = "id", nullable = false)
    private User user;
    public Projet_Utilisateur(Project project, User user){
       this.project=project;
       this.user=user;
    }
    public Project getProject(){
        return project;
    }
    public void setProject(Project project){
        this.project=project;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
*/