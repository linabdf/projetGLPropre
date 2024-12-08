package com.example.demo;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;

import static com.example.demo.DatabaseManager.isOnline;

import java.sql.Date;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ProjectController {

    @Autowired
    private DeveloppeurRepository developpeurRepository;
    private ProjectRepository ProjectRepository;
    private final DatabaseManager databaseManager;
    private final ProjectService ProjectService;
    private final UserService UserService;
    String projectiden;;
    public ProjectController(DeveloppeurRepository developpeurRepository, UserService UserService, ProjectService ProjectService) {
        this.databaseManager = new DatabaseManager("jdbc:mysql://sql7.freesqldatabase.com:3306/sql7743283", "sql7743283", "aC2kDrfGsk");
        this.ProjectService = ProjectService;
        this.UserService = UserService;
        this.developpeurRepository = developpeurRepository;

    }

    @GetMapping("/project-management")
    public String showAddProjectForm() {
        return "project-management";
        // nom du template HTML pour ajouter un projet
    }

    // ajouter un projet
    @PostMapping("/addProject")
    public String registerProject(@RequestParam String name,
                                  @RequestParam String description,
                                  @RequestParam String startDate,  // Correspond au champ "startDate" du formulaire
                                  @RequestParam String endDate,    // Correspond au champ "endDate" du formulaire
                                  @RequestParam int progres,
                                  HttpSession session, Model model) throws SQLException {
        String userEmail = (String) session.getAttribute("userEmail");
        if (!isOnline()) {
            databaseManager.connexion();
            // Connecter à la base de données si ce n'est pas déjà fait
        }

        String userId = ProjectService.getUserIdByEmail(userEmail);
        System.out.println("name" + name);
        System.out.println("Description" + description);
        System.out.println("startDate" + startDate);
        System.out.println(" endDate" + endDate);
        System.out.println(" endDate" + userId);

        if (userId != null) { // vérifier que l'utilisateur a bien été trouvé
            // Insérer le projet avec l'ID de l'utilisateur récupéré
            List<Project> userProjects = ProjectService.getProjectByUserId(userId);
            // Convertir les dates de String à Date
            String start = null;
            Date end = null;
            try {
                if (startDate != null && !startDate.isEmpty()) {
                    start = String.valueOf(Date.valueOf(startDate)); // Convertir la date de début
                }
                if (endDate != null && !endDate.isEmpty()) {
                    end = Date.valueOf(endDate); // Convertir la date de fin
                }
            } catch (IllegalArgumentException e) {
                model.addAttribute("errorMessage", "Les dates sont invalides.");
                return "project-management"; // Retourner sur la page d'ajout de projet en cas d'erreur
            }
            boolean success = ProjectService.insererProjet(
                    name,
                    description,
                    String.valueOf(start),
                    String.valueOf(end),
                    progres,
                    userId);
            model.addAttribute("successMessage", "Le projet a été enregistré avec succès !");
            model.addAttribute("projects", userProjects);
            if (success) {
                List<Project> Projects = com.example.demo.ProjectService.getProjectByUserId(userId);
                model.addAttribute("projects", Projects);
                return "dashboardManager"; // Redirige vers le tableau de bord après ajout
            } else {
                model.addAttribute("errorMessage", "Utilisateur non trouvé. Veuillez vous reconnecter.");
                return "project-management"; // Rester sur la page d'ajout de projet } }
            }

        } else {
            model.addAttribute("errorMessage", "Usernot found");
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
            String userId = ProjectService.getUserIdByEmail(userEmail);

            // Vérifier que l'ID de l'utilisateur est valide
            if (userId != null) {
                // Récupérer la liste des projets associés à l'ID de l'utilisateur
                List<Project> userProjects = ProjectService.getProjectByUserId(userId);

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

    /*
    @GetMapping("/adduser/{name}")
    public String showAddUserPage( Model model) {
        // Vous pouvez récupérer le projet par son nom
        //  String project = ProjectService.getProjectIdByName(name);
        //model.addAttribute("project", project);
        //System.out.println("projet id"+project);
        return "adduser"; // Retourne la page adduser.html
    }
    @PostMapping("/adduser/{name}")
    public ResponseEntity<Map<String, String>> checkDeveloppeur(@PathVariable String name,@RequestParam String email,Model model) {
        String project = ProjectService.getProjectIdByName(name);
        model.addAttribute("project", project);
        System.out.println("projet id"+project);
        Map<String, String> response = new HashMap<>();

        String developpeurId =  UserService.getDeveloppeurIdByEmail(email);
        System.out.println("dev"+developpeurId);
        if (developpeurId != null) {

            boolean sucess = ProjectService.addDeveloperToProject(project,developpeurId );
            if(sucess){
                response.put("message", "Le développeur existe déjà avec ID : " + developpeurId);
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }
            else {
                response.put("message", "Erreur lors de l'ajout du développeur au projet.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }


        } else {
            response.put("message", "L'utilisateur n'existe pas comme développeur.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
*/
    @GetMapping("/adduser/{name}")
    public String showAddUserPage(@PathVariable String name, Model model) {
        // Récupérer l'ID du projet à partir du nom
        projectiden= com.example.demo.ProjectService.getProjectIdByName(name);


        // Ajoutez les informations au modèle
        model.addAttribute("projectId", projectiden);
        // model.addAttribute("projectName", name);
        System.out.println("Nom du projet : " + projectiden);
        return "adduser";
    }

    @PostMapping("/adduser")
    public ResponseEntity<Map<String, String>> addUserToProject(
            @RequestParam String email) {

        Map<String, String> response = new HashMap<>();

        System.out.println("Project ID reçu : " + projectiden);  // Ajoutez ce log pour vérifier la valeur du projectId

        // Vérification des entrées
        if (projectiden == null || projectiden.isEmpty()) {

            response.put("message", "L'identifiant du projet est manquant.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        if (email == null || email.isEmpty()) {
            response.put("message", "L'email est manquant.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Vérifier si le développeur existe
        String developpeurId = UserService.getDeveloppeurIdByEmail(email);
        System.out.println("Developpeur : " + developpeurId);
        if (developpeurId == null) {
            response.put("message", "Le développeur avec cet email n'existe pas.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Ajouter le développeur au projet
        try {

            boolean success = ProjectService.addDeveloperToProject(developpeurId, projectiden);

            if (success) {
                response.put("message", "Le développeur a été ajouté avec succès au projet.");
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                response.put("message", "Le développeur existe déja.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "Une erreur inattendue s'est produite : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/projects/{name}/developers")
    @ResponseBody
    public List<String> showProjectDevelopers(@PathVariable String name, Model model) {
        System.out.println("projetid"+ name);
        // Récupérer l'ID du projet à partir du nom
        projectiden= com.example.demo.ProjectService.getProjectIdByName(name);


        // Ajoutez les informations au modèle
        model.addAttribute("projectId", projectiden);
        // Récupérer l'ID du projet par son nom

        System.out.println("projetid"+ projectiden);
        // Récupérer les IDs des développeurs associés au projet
        List<String> developpeurIds = com.example.demo.ProjectService.getDeveloppeuridByProjectid( projectiden);

        // Retourner la liste des développeurs au format JSON
        return developpeurIds;
    }

}