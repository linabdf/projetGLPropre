package com.example.demo;

import jakarta.persistence.Table;
import jakarta.persistence.*;




@Entity
@Table(name ="utilisateur")
public class User{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private String id;
    @Column(name="nomU")
    private String name;
    @Column(name="mail")
    private String email;
    @Column(name="motdepasse")
    private String password;
    @Column(name="role")
    private String userType;


    public User() {

    }


    public User(String id) {
        this.id=id;
    }


    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;

    }





    // Getters et setters
    public String getUserId() {
        return id;
    }


    public void setUserId(String id ) {
        this.id= id;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public String getUserType() {
        return userType;
    }


    public void setUserType(String userType) {
        this.userType = userType;
    }

}