package com.example.demo;

import jakarta.persistence.*;

import java.util.List;

@Entity
@DiscriminatorValue("chef_Projet")
public class Chef_Projet extends User{

    @OneToMany(mappedBy="chef_Projet")  // Relie à la classe User, l'attribut chefDeProjet dans User
    private List<Developpeur> developpeur;

    public Chef_Projet(String name, String email, String password) {
            super(name, email, password);
    }

    public Chef_Projet() {

    }

    public List<Developpeur> getDeveloppeurs() {
        return developpeur;
    }

    public void setDeveloppeurs(List<Developpeur> developpeurs) {
        this.developpeur = developpeurs;
    }

}

