package com.example.demo;
//model
import jakarta.persistence.Table;
import jakarta.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Projet")
public class Project  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @Column( name ="nomP",unique = true,nullable = false)
    private String name;
    @Column(name = "description")
    private String description ;
    @Column(name = "dateDebP")
    private String startDate;
    @Column(name = "dateFinP")
    private String endDate;
    @Column(name = "progres")
    private int  progres;

    @ManyToOne
    @JoinColumn(name = "numU")
    private User user;
    @ManyToMany()
    @JoinTable(
            name = "project_developpeur",
            joinColumns = @JoinColumn(name = "numP"),
            inverseJoinColumns = @JoinColumn(name = "numU")
    )
    private List<Developpeur> developpeurs = new ArrayList<>();

    // public Project(){}
    public Project(String name, String description, Date startDate, Date endDate, int progres, User user){
        this.name=name;
        this.description=description;
        this.startDate= String.valueOf(startDate);
        this.endDate= String.valueOf(endDate);
        this.progres =progres;
        this.user=user;
    }

    public Project() {

    }


    // Getters et setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id= id;
    }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String email) { this.description = description; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String StartDate) { this.startDate= startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public int getProgres(){return progres;}
    public void  setProgres(int  progres){
      this.progres=progres;
    }
    public User getUser(){
        return user;
    }
    public void setUser(User user){
        this.user=user;
    }
    // Getters et Setters
    public List<Developpeur> getDeveloppeurs() {
        return developpeurs;
    }

    public void setDeveloppeurs(List<Developpeur> developpeurs) {
        this.developpeurs = developpeurs;
    }

}





