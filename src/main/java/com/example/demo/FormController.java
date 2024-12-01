/*package com.example.demo;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;


import static com.example.demo.UserRepository.*;

@Controller
public class FormController {


    private final API api;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    @Autowired
    public FormController(UserRepository userRepository, ProjectRepository projectRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.api=new API("jdbc:mysql://sql7.freesqldatabase.com:3306/sql7743283","sql7743283","aC2kDrfGsk");
    }

    // fichier.html
    @GetMapping("/index")
    public String showForm() {
        return "index"; // nom du template HTML
    }


    @PostMapping("/register")
    public String registerUser(User user, Model model) {



       // API api = new API("jdbc:mysql://sql7.freesqldatabase.com:3306/sql7743283","sql7743283","aC2kDrfGsk");

        api.connexion();



		if(api.verifierutilisateur(user.getEmail())) {
			api.insererutilisateur(user.getName(), user.getEmail(), user.getPassword(), user.getUserType());
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


        if (!api.isOnline()) {
            api.connexion(); // Connecter à la base de données si ce n'est pas déjà fait
        }


     // Afficher les informations dans la console

        System.out.println("Email : " + user.getEmail());
        System.out.println("Password : " + user.getPassword());




      // api.insererProjetAvecConstantes();
		if(api.connectionutilisateur(user.getEmail(), user.getPassword())) { // si le compte existe

			String role = api.getUser(user.getEmail(), user.getPassword());
            session.setAttribute("userEmail", user.getEmail());
            String userId = api.getUserIdByEmail(user.getEmail());
            if (userId != null) {
                // Récupérer les projets associés à cet utilisateur
                List<Project> userProjects = api.getProjectByUserId(userId);
                model.addAttribute("projects", userProjects); // Ajouter les projets au modèle
            } else {
                model.addAttribute("errorMessage", "Utilisateur non trouvé.");
                return "index"; // Redirige vers l'accueil si l'ID utilisateur est invalide
            }
            switch(role){
            case "user" :
                return "dashboarduser";
           case "admin" :
                return "dashboardAdmin";
             case "manager" :
                return "dashboardManager";
              default:
                 model.addAttribute("error", "Rôle inconnu !");
                  return "index"; }


		}
		else {
			model.addAttribute("errorMessageconnection", "Email ou mot de passe incorrect. Veuillez réessayer.");
			return "index"; // rester sur la page incription
		}

    }
    @GetMapping("/project-management")
    public String showAddProjectForm() {
        return "project-management";
        // nom du template HTML pour ajouter un projet
    }

    // ajouter un projet
    @PostMapping("/addProject")
    public String registerProject(Project project, HttpSession session, Model model) {
        String userEmail = (String) session.getAttribute("userEmail");
        if (!api.isOnline()) {
            api.connexion();
            // Connecter à la base de données si ce n'est pas déjà fait
        }
        API api = new API();
        String userId = api.getUserIdByEmail(userEmail);
        System.out.println("name"+ project.getName());
        System.out.println("Description"+project.getDescription());
        System.out.println("startDate"+ project.getStartDate());
        System.out.println(" endDate"+project.getEndDate());
        if (userId != null) { // vérifier que l'utilisateur a bien été trouvé
            // Insérer le projet avec l'ID de l'utilisateur récupéré
            List<Project> userProjects = api.getProjectByUserId(userId);
            boolean success = api.insererProjet(
                    project.getName(),
                    project.getDescription(),
                    project.getStartDate(),
                    project.getEndDate(),
                    project.getProgres(),
                    userId);
            model.addAttribute("successMessage", "Le projet a été enregistré avec succès !");
            model.addAttribute("projects", userProjects);
            if(success){
                List<Project> Projects = api.getProjectByUserId(userId);
                model.addAttribute("projects", Projects);
                return "dashboardManager"; // Redirige vers le tableau de bord après ajout
             } else {
               model.addAttribute("errorMessage", "Utilisateur non trouvé. Veuillez vous reconnecter.");
               return "project-management"; // Rester sur la page d'ajout de projet } }
           }

        }else {
            model.addAttribute("errorMessage","Usernot found");
            return "project-management";
        }

   }
    @GetMapping("/projects")
    public String showUserProjects(HttpSession session, Model model) {
        // Récupérer l'email de l'utilisateur depuis la session
        String userEmail = (String) session.getAttribute("userEmail");

        // Vérifier si l'utilisateur est connecté
        if (userEmail != null) {
            // Récupérer l'ID de l'utilisateur à partir de son email
            String userId = api.getUserIdByEmail(userEmail);

            // Vérifier que l'ID de l'utilisateur est valide
            if (userId != null) {
                // Récupérer la liste des projets associés à l'ID de l'utilisateur
                List<Project> userProjects = api.getProjectByUserId(userId);

                // Ajouter la liste des projets au modèle
                model.addAttribute("projects", userProjects);

                // Retourner la vue (par exemple, "projects.html")
                return "dashboardManager";
                // Nom du template pour afficher les projets
            } else {
                model.addAttribute("errorMessage", "Utilisateur non trouvé. Veuillez vous reconnecter.");
                return "index";  // Si l'utilisateur n'est pas trouvé, retourner à la page d'accueil
            }
        } else {
            model.addAttribute("errorMessage", "Utilisateur non connecté.");
            return "index";  // Si l'utilisateur n'est pas connecté, retourner à la page d'accueil
        }
    }
    @GetMapping("/adduser")
    public String showAddUserPage() {
        return "adduser"; // Nom du fichier adduser.html dans le dossier templates
    }

    @PostMapping("/adduser")
    public ResponseEntity<Map<String, String>> checkUtilisateur(@RequestParam String email) {
        Map<String, String> response = new HashMap<>();
        boolean existe = api.verifierutilisateur(email);
        if (existe) {
            response.put("message", "L'utilisateur n'existe pas.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            response.put("message", "Utilisateur ajouté.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }
}

*/