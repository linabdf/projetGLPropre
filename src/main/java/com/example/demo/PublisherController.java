package com.example.demo;

import java.util.List;


import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
@Controller
public class PublisherController {

    @Autowired
    private CommentaireService commentaireService;
    private final ProjectService ProjectService;


    public PublisherController(ProjectService ProjectService) {
        this.ProjectService = ProjectService;

    }


    @GetMapping("/projet/{projectName}")
    @ResponseBody
    public  String afficherProjet(@PathVariable("projectName") String projectName, Model model) {
        // Récupérer les commentaires pour le projet
        System.out.println("Appel de la méthode getCommentaireProjectid pour le projet : " + projectName);

        List<String> commentaires =  ProjectService.getCommentaireProjectid(projectName);
        // Vérifiez si la liste est null ou vide
        if (commentaires == null) {
            System.out.println("La liste des commentaires est null");
        } else if (commentaires.isEmpty()) {
            System.out.println("La liste des commentaires est vide");
        } else {
            System.out.println("Commentaires récupérés : " + commentaires); // Affiche la liste des commentaires
        }
        System.out.println("commm"+commentaires);
        // Ajouter les commentaires au modèle pour les afficher dans la vue
        model.addAttribute("commentaires", commentaires);
        model.addAttribute("projectName", projectName);
        // Ajouter un objet Commentaire vide pour lier avec le formulaire (s'il n'est pas déjà ajouté)


        return "liste des commentaires"+commentaires;
    }
    @PostMapping("/projet/commentaire")
    public String ajouterCommentaire(@RequestParam("projectName") String projectName,
                                     @RequestParam("userEmail") String userEmail,
                                     @RequestParam("commentText") String commentText) {
        // Affichage des données récupérées pour vérification
        System.out.println("Project Name: " + projectName);
        System.out.println("User Email: " + userEmail);
        System.out.println("Comment Text: " + commentText);

        // Logique pour ajouter le commentaire et rediriger ou afficher un message
        boolean isAdded = commentaireService.addComment(projectName, userEmail, commentText);

        if (isAdded) {

            return "dashboarduser";  // Redirigez vers la page du projet
        } else {
            return "error";  // Affichez une page d'erreur si l'ajout échoue
        }
    }

}