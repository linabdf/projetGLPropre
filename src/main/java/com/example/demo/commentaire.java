package com.example.demo;

import jakarta.persistence.*;
import org.springframework.context.ApplicationEvent;

@Entity
@DiscriminatorValue("Commentaire")
public class commentaire extends ApplicationEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "contenu", unique = true, nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "numSender", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "numP", nullable = false)
    private Project projet;

    public commentaire(Object source,Project projectName,User userEmail,String commentText) {
        super(source);// Appel du constructeur parent pour spécifier l'origine de l'événement
        this.user=userEmail;
        this.projet = projectName;
        this.content = commentText;

    }

    // Getters et Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Project getProjet() {
        return projet;
    }

    public void setProjet(Project projet) {
        this.projet = projet;
    }
}
