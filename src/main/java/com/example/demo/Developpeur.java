package com.example.demo;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("developpeur")
public class Developpeur extends User {
    private String numU;
    @ManyToOne
    @JoinColumn(name = "chefDeProjet")  // Référence vers le chef de projet
    private Chef_Projet chef_Projet;


    @ManyToMany(mappedBy = "developpeurs")// Le côté inverse de la relation
    private List<Project> projects = new ArrayList<>();
    public Developpeur(String name, String email, String password) {
        super(name, email, password);
    }


    public Developpeur(String numU) {
        this.numU=numU;
    }


    public Developpeur(String id, String chef_projet) {
        this.numU=id;
    }


    @Override
    public String toString() {
        return "Developpeur{" +
                "id='" + numU + '\''+
                '}';
    }


    // Getter pour obtenir le nom du chef de projet
    public String getChefDeProjetNom() {
        if (chef_Projet != null) {
            return chef_Projet.getName();  // Renvoie le nom du chef de projet
        }
        return null;  // Si pas de chef de projet assigné
    }


    public String getId() {
        return numU;
    }

    public void setId(String id) {
        this.numU = id;
    }


    // Getters et setters pour chefDeProjet
    public Chef_Projet getChefDeProjet() {
        return chef_Projet;
    }


    public void setChefDeProjet(Chef_Projet chef_Projet) {
        this.chef_Projet = chef_Projet;
    }


    public List<Project> getProjects() {
        return projects;
    }


    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }



}