package com.example.demo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import static com.example.demo.DatabaseManager.connection;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Connection;
import java.util.*;


import static com.example.demo.UserRepository.*;

@Controller
public class UserController {
    private final DatabaseManager databaseManager;
    private final UserService UserService;
    private final ProjectService projectService;

    public UserController(UserService UserService, ProjectService projectService) {
        this.projectService = projectService;
        this.databaseManager = new DatabaseManager("jdbc:mysql://sql7.freesqldatabase.com:3306/sql7743283","sql7743283","aC2kDrfGsk");
        this.UserService=UserService;

    }
    // fichier.html
    @GetMapping("/index")
    public String showForm() {
        return "index"; // nom du template HTML
    }
    @PostMapping("/register")
    public String registerUser(User user, Model model) {



        // API api = new API("jdbc:mysql://sql7.freesqldatabase.com:3306/sql7743283","sql7743283","aC2kDrfGsk");

        databaseManager.connexion();



        if(UserService.verifierutilisateur(user.getEmail())) {
            UserService.insererutilisateur(user.getName(), user.getEmail(), user.getPassword(), user.getUserType());
            // Redirigez vers une page de succès ou d'affichage des informations
            return "index"; // Nom du template pour la page de succès
        }else {
            model.addAttribute("errorMessageInscription", "L'addresse mail est déjà utilisée");
            return "index"; // rester sur la page incription
        }}
        @PostMapping("/connection")
        public String connectionUser(User user, Model model,HttpSession session) {
            // Traitez ici les données utilisateur


            if (!databaseManager.isOnline()) {
                databaseManager.connexion();// Connecter à la base de données si ce n'est pas déjà fait
            }


            // Afficher les informations dans la console

            System.out.println("Email : " + user.getEmail());
            System.out.println("Password : " + user.getPassword());



            // api.insererProjetAvecConstantes();
            if(UserService.connectionutilisateur(user.getEmail(), user.getPassword())) { // si le compte existe

                String role = UserService.getUser(user.getEmail(), user.getPassword());

                session.setAttribute("userEmail", user.getEmail());

                String userId = UserService.getUserIdByEmail(user.getEmail());
                System.out.println("idUtilisateur"+userId);
                if (userId != null) {
                    switch(role){
                        case "user" :
                            List<Project> userProject =ProjectService.getProjectsByDeveloperId(userId);
                            System.out.println("projets"+userProject);
                            model.addAttribute("projects", userProject);
                            return "dashboarduser";
                        case "admin" :
                            return "dashboardAdmin";
                        case "manager" :
                            List<Project> userProjects = ProjectService.getProjectByUserId(userId);
                            System.out.println("projets"+userProjects);
                            model.addAttribute("projects", userProjects); // Ajouter les projets au modèle
                            return "dashboardManager";
                        default:
                            model.addAttribute("error", "Rôle inconnu !");
                            return "index"; }
                    // Récupérer les projets associés à cet utilisateur

                } else {
                    model.addAttribute("errorMessage", "Utilisateur non trouvé.");
                    return "index"; // Redirige vers l'accueil si l'ID utilisateur est invalide
                }



            }
            else {
                model.addAttribute("errorMessageconnection", "Email ou mot de passe incorrect. Veuillez réessayer.");
                return "index"; // rester sur la page incription
            }

        }
    @GetMapping("/logout")
    public String deconnexion(HttpServletRequest request) {
        // Fermer la session de l'utilisateur (supprimer les attributs liés à l'utilisateur)
        request.getSession().invalidate(); // Cela déconnecte l'utilisateur et efface ses informations de session
        databaseManager.deconnexion();
        // Redirige vers la page d'accueil
        return "redirect:/index"; // Redirection vers la page d'accueil (par défaut)
    }



    }
