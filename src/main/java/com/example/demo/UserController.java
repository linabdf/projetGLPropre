package com.example.demo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;



import org.springframework.web.bind.annotation.RequestParam;


import java.util.*;



@Controller
public class UserController {

    private final DatabaseManager databaseManager;
    private final UserService UserService;
    private final ProjectService projectService;
    private final TacheService TacheService;

    private String Adressemail;

    public UserController(UserService UserService, ProjectService projectService,TacheService TacheService) {
        this.projectService = projectService;
        this.databaseManager = new DatabaseManager("jdbc:mysql://sql7.freesqldatabase.com:3306/sql7743283","sql7743283","aC2kDrfGsk");
        this.UserService=UserService;
        this.TacheService= TacheService;

    }
    // fichier.html
    @GetMapping("/index")
    public String showForm() {
        return "index"; // nom du template HTML
    }


    @PostMapping("/register")
    public String registerUser(User user, Model model) {

        databaseManager.connexion();
        if(UserService.insererutilisateur(user.getName(), user.getEmail(), user.getPassword(), user.getUserType())) {
            // Redirigez vers une page de succès ou d'affichage des informations
            return "index"; // Nom du template pour la page de succès
        }else {
            model.addAttribute("errorMessageInscription", "L'addresse mail est déjà utilisée");
            return "index"; // rester sur la page incription
        }
    }


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

            String role = UserService.getUserRole(user.getEmail(), user.getPassword());

            session.setAttribute("userEmail", user.getEmail());

            String userId = UserService.getUserIdByEmail(user.getEmail());
            System.out.println("idUtilisateur"+userId);
            if (userId != null) {
                switch(role){
                    case "user" :
                        List<Project> userProject =projectService.getProjectsByDeveloperId(userId);
                        System.out.println("projets"+userProject);
                        model.addAttribute("projects", userProject);
                        List<Map<String, String>> userTaches = TacheService.getTacheByProjectdeveloppeur(userId);
                        System.out.println("Tâches : " + userTaches);
                        // Ajouter les projets et les tâches au modèle pour l'affichage dans la vue
                        model.addAttribute("projects", userProject);
                        model.addAttribute("tasks", userTaches);
                        return "dashboarduser";
                    case "admin" :
                        return "dashboardAdmin";
                    case "manager" :
                        List<Project> userProjects = projectService.getProjectByUserId(userId);
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


    @PostMapping("/update")
    public String updateProfile(
            @RequestParam String nom,
            @RequestParam String prenom,
            @RequestParam String email,
            @RequestParam String password,
            Model model) {

        // Appeler la méthode pour modifier les données
        if(UserService.verifierutilisateur(email)){
            UserService.modifierProfil(Adressemail, nom, prenom, email, password);
            model.addAttribute("successMessage", "Profil mis à jour avec succès !");
            return "index";

        }else{
            model.addAttribute("errorMessageUpdate", "L'addresse mail est déjà utilisée");
            return "profil";
        }

    }



    @PostMapping("/profil")
    public String gererProfil(User user, Model model,HttpSession session) {
        // Traitez ici les données utilisateur


        if (!databaseManager.isOnline()) {
            databaseManager.connexion();// Connecter à la base de données si ce n'est pas déjà fait
        }


        // Afficher les informations dans la console

        System.out.println("Email : " + user.getEmail());
        System.out.println("Password : " + user.getPassword());



        if(UserService.connectionutilisateur(user.getEmail(), user.getPassword())) { // si le compte existe


            session.setAttribute("userEmail", user.getEmail());
            String userId = UserService.getUserIdByEmail(user.getEmail());
            if (userId != null) {

                Adressemail = user.getEmail();
                return "profil";

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
    @PostMapping("/updateRole")
    public String updateRole(@RequestParam("email") String email,
                             @RequestParam("role") String role,
                             Model model) {
        // Appeler le service pour mettre à jour le rôle
        boolean success = UserService.modifierRole(email, role);

        // Vérifier si la mise à jour a réussi
        if (success) {
            model.addAttribute("successMessage", "Le rôle a été mis à jour avec succès.");
        } else {
            model.addAttribute("errorMessageUpdate", "Erreur : Utilisateur non trouvé ou problème lors de la mise à jour.");
        }

        return "dashboardAdmin"; // Retourner à la vue du formulaire
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
