package com.example.demo;



import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Tache")
public class Tache implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "nomT", nullable = false)
    private String name;

    @Column(name = "dateEch")
    private String dueDate;
    @Column(name = "priorite")
    private String priority;
    @Column(name = "statuts")
    private String status;
    @ManyToOne
    @JoinColumn(name = "numU")
    private Developpeur developpeur;
    @ManyToOne
    @JoinColumn(name = "numP")
    private Project project;


    @ManyToMany
    @JoinTable(
            name = "Tache_dependance",  // Nom de la table de jointure
            joinColumns = @JoinColumn(name = "numT"),  // Colonne de jointure pour la tâche principale
            inverseJoinColumns = @JoinColumn(name = "depT")  // Colonne de jointure pour la tâche dépendante
    )
    private List<Tache> dependances = new ArrayList<>();  // Liste des dépendances

    public Tache() {}

    public Tache(String name, String dueDate, String priority, String status, Project project, Developpeur developpeur,Tache tache) {
        this.name = name;
        this.dueDate = dueDate;
        this.priority = priority;
        this.status = status;
        this.project = project;
        this.developpeur=developpeur;

    }

    public Tache(String nomT) {
        this.name=nomT;

    }

    public Tache(String nomT, String priorite, String status, String dateEch, String numU, String numP, String depT) {
    }

    // Getters et setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProject() {
        return project.getId();
    }

    public void setProject(Project project) {
        this.project = project;
    }
    public void setDeveloppeur(Developpeur developpeur) {
        this.developpeur = developpeur;
    }


    public String getDeveloppeur() {
        return developpeur.getId();
    }
    // Getter et setter pour les dépendances
    public List<Tache> getDependances() {
        return dependances;
    }

    public void setDependances(List<Tache> dependances) {
        this.dependances = dependances;
    }

    // Méthode pour ajouter une dépendance
    public void addDependance(Tache dependance) {
        this.dependances.add(dependance);
    }


    @Override
    public String toString() {
        return "Tache{name='" + name + '\'' +
                ", priority='" + priority + '\'' +
                ", status='" + status + '\'' +
                ", dueDate='" + dueDate + '\'' +
                ", developpeur=" + (developpeur != null ? developpeur.toString() : "null") +
                '}';
    }}
