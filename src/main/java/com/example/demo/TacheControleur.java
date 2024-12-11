package com.example.demo;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
public class TacheControleur {
    private final DatabaseManager databaseManager;
    private  final TacheService TacheService;
    private final UserService UserService;

    private final ProjectService ProjectService;

    public TacheControleur(TacheService TacheService,UserService userService,ProjectService projectService) {
        UserService = userService;
        ProjectService = projectService;

        this.databaseManager = new DatabaseManager("jdbc:mysql://sql7.freesqldatabase.com:3306/sql7743283", "sql7743283", "aC2kDrfGsk");
        this.TacheService = TacheService;
    }
    @GetMapping("/dashboardManager")
    public String showAddProjectForm() {
        return "dashboardManager";
        // nom du template HTML pour ajouter un projet
    }

    /*@PostMapping("/addTask")
    public String ajouterTache( Tache tache, HttpSession session, Model model){
      //  String userId = DeveloppeurService.getDeveloperIdByEmail(tache.getDeveloppeur());
        String projectId= com.example.demo.ProjectService.getProjectIdByName(tache.getProject().getId());
        System.out.println("I AM HERE");
        boolean success = TacheService.insererTache( tache.getName(),
                tache.getDescription(),
                tache.getDueDate(),
                tache.getPriority(),
                tache.getStatus(),
                projectId, // L'ID du projet associé
                tache.getDeveloppeur(),                    // L'ID de l'utilisateur connecté
              ( tache.getTache() != null ? tache.getTache().getId() : null )// Dépendance éventuelle
        );
        if (success) {
            model.addAttribute("message", "Tâche ajoutée avec succès !");
            System.out.println("sucess");
            return "dashboardManager"; // Redirige vers une page de succès (ex. success.html)
        } else {
            System.out.println("ERRROR");
            model.addAttribute("error", "Une erreur est survenue.");
            return "dashboardManager"; // Redirige vers une page d'erreur (ex. error.html)
        }
    }
*///"/projects/{name}

    @PostMapping("/addTask")
    public String ajouterTache(  @RequestParam("taskName") String taskName,
                                 @RequestParam("taskDueDate") String taskDueDate,
                                 @RequestParam("taskPriority") String taskPriority,
                                 @RequestParam("taskStatus") String taskStatus,
                                 @RequestParam("taskProjet") String taskProjet,  // Récupérer l'ID du projet
                                 @RequestParam("taskAdmin") String taskAdmin // Récupérer l'ID du développeur
            ,HttpSession session, Model model){
        String userId = UserService.getUserIdByEmail(taskAdmin);
        String projectId= com.example.demo.ProjectService.getProjectIdByName( taskProjet);

        System.out.println("Task Name: " + taskName);

        System.out.println("taskDueDate " + taskDueDate);
        System.out.println("taskPriority " + taskPriority);
        System.out.println("taskStatus" + taskStatus);
        System.out.println("taskProjet " + taskProjet);  // Récupérer l'ID du projet
        System.out.println("taskAdmin"+taskAdmin);

        System.out.println("userid"+userId);
        System.out.println("projecteid"+projectId);

        boolean success = TacheService.insererTache(taskName,taskDueDate, taskPriority,taskStatus, projectId,userId);
        if (success) {
            model.addAttribute("message", "Tâche ajoutée avec succès !");
            System.out.println("sucess");
            return "redirect:/projects"; // Redirige vers une page de succès (ex. success.html)
        } else {
            System.out.println("ERRROR");
            model.addAttribute("error", "Une erreur est survenue.");
            return "dashboardManager"; // Redirige vers une page d'erreur (ex. error.html)
        }
    }

    @GetMapping("projects/{name}/tasks")
    @ResponseBody
    public List<Map<String, String>> getTasksByProjectName(@PathVariable String name, Model model) {

        String projectId = com.example.demo.ProjectService.getProjectIdByName(name);
        System.out.println("projetid"+ name);
        model.addAttribute("projectId"+projectId);
        List<Map<String, String>> tasks= com.example.demo.TacheService. getTacheByProjectName(name);


        return tasks;}
    @PostMapping("/add-dependency")
    public ResponseEntity<String> addDependency(@RequestBody Map<String, String> request) {
     //  String projectName = request.get("projectName");  // Récupérer le nom du projet
       String taskName = request.get("taskName");  // Récupérer le nom de la tâche
       String dependencyName = request.get("dependencyName");  // Récupérer le nom de la dépendance

    //   System.out.println("Project: " + projectName);
       System.out.println("Task: " + taskName);
       System.out.println("Dependency: " + dependencyName);
       if (TacheService.verifierache(dependencyName)) {
          Boolean insereTache= TacheService.inserDependancesTcahe(taskName,dependencyName);
          if(insereTache) {
              String tache = TacheService.getTacheid(taskName);
              System.out.println(tache);
          }       return ResponseEntity.ok("Dépendance ajoutée avec succès");

   }else {
           return ResponseEntity.ok("Dépendance  n'existe pas");
       }
    }
    // Endpoint pour récupérer les tâches dépendantes d'une tâche donnée
    // Endpoint pour récupérer les dépendances d'une tâche
    @GetMapping("/tasks/{taskName}")
    @ResponseBody
    public List<String> getDependencies(@PathVariable String taskName) {
        System.out.println("Tâche: " + taskName);
        String idtach = TacheService.getTacheid(taskName);
        List<String> dependencies = TacheService.getDependantesTache(idtach);
        System.out.println("Dépendances: " + dependencies);
        return dependencies; // Automatiquement converti en JSON
    }
    @PostMapping("/updateTaskStatus")
    public String updateTaskStatus(@RequestParam("taskName") String taskId,
                                   @RequestParam("status") String status,

                                   RedirectAttributes redirectAttributes) {
        // Appeler la méthode ModifierStatut pour vérifier et modifier le statut
        boolean statutModifie = TacheService.ModifierStatut(taskId, status);

        if (statutModifie) {
            // Si le statut a été modifié avec succès
            redirectAttributes.addFlashAttribute("successMessage", "Le statut de la tâche a été modifié avec succès.");
        } else {
            // Si la modification n'a pas eu lieu
            redirectAttributes.addFlashAttribute("errorMessage", "La modification du statut a échoué.");
        }

        // Rediriger vers le tableau de bord pour rafraîchir la page
        return "dashboarduser";
    }



}