package com.example.demo;

import java.util.List;

import ch.qos.logback.core.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
@Controller
public class Publisher {
    @Autowired
    private CommentaireService commentaireService;

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

            return "redirect:/projet/" + projectName;  // Redirigez vers la page du projet
        } else {
            return "error";  // Affichez une page d'erreur si l'ajout échoue
        }
    }

}
