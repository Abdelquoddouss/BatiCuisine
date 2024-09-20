package Ui;

import Entity.Project;
import Entity.User;
import Entity.enums.EtatProject;
import Repository.ProjectRepository;
import Repository.UserRepository;
import Service.ProjectService;
import Service.UserService;
import config.DatabaseConnection;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class CrudProjectMenu {

    private static UserService userService;
    private static ProjectService projectService;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        Connection connection = DatabaseConnection.getConnection();

        UserRepository userRepository = new UserRepository(connection);
        userService = new UserService(userRepository);
        ProjectRepository projectRepository = new ProjectRepository(connection);
        projectService = new ProjectService(projectRepository);

        int choix = -1;

        while (choix != 0) {
            afficherMenuPrincipal();
            choix = scanner.nextInt();
            scanner.nextLine(); // Consommer la nouvelle ligne

            switch (choix) {
                case 1:
                    ajouterProjet();
                    break;
                case 2:
                    consulterProjet();
                    break;
                case 3:
                    mettreAJourProjet();
                    break;
                case 4:
                    supprimerProjet();
                    break;
                case 5:
                    listerTousLesProjets();
                    break;
                case 0:
                    System.out.println("Sortie du menu...");
                    break;
                default:
                    System.out.println("Choix invalide ! Veuillez réessayer.");
            }
        }

        scanner.close();

        try {
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Méthode pour afficher le menu principal
    private static void afficherMenuPrincipal() {
        System.out.println("\n===== Menu CRUD Projets =====");
        System.out.println("1. Ajouter un Projet");
        System.out.println("2. Voir un Projet");
        System.out.println("3. Mettre à jour un Projet");
        System.out.println("4. Supprimer un Projet");
        System.out.println("5. Lister tous les Projets");
        System.out.println("0. Quitter");
        System.out.print("Entrez votre choix : ");
    }

    // Méthode pour ajouter un projet
    private static void ajouterProjet() {
        System.out.print("Entrez l'ID de l'utilisateur associé à ce projet : ");
        int userId = scanner.nextInt();
        User user = userService.getUserById(userId);
        if (user == null) {
            System.out.println("Utilisateur non trouvé ! Impossible de continuer.");
            return;
        }

        // Sélection du statut du projet
        System.out.println("Sélectionnez le statut du projet :");
        EtatProject[] statuts = EtatProject.values();
        for (int i = 0; i < statuts.length; i++) {
            System.out.println((i + 1) + ". " + statuts[i]);
        }
        System.out.print("Entrez le numéro correspondant au statut : ");
        int choixStatut = scanner.nextInt();
        scanner.nextLine(); // Consommer la nouvelle ligne
        EtatProject statutProjet = statuts[choixStatut - 1];

        // Collecte des autres informations
        System.out.print("Entrez le nom du projet : ");
        String nomProjet = scanner.nextLine();
        System.out.print("Entrez la marge bénéficiaire : ");
        double margeBeneficiaire = scanner.nextDouble();
        double coutTotal = 0.0;

        // Création du projet
        Project projet = new Project(0, nomProjet, margeBeneficiaire, coutTotal, statutProjet, user);
        projectService.addProject(projet);
        System.out.println("Projet ajouté avec succès.");
    }

    // Méthode pour consulter un projet
    private static void consulterProjet() {
        System.out.print("Entrez l'ID du projet à consulter : ");
        int projectId = scanner.nextInt();
        Project projetRecupere = projectService.getProjectById(projectId);
        if (projetRecupere != null) {
            System.out.println("\n===== Détails du Projet =====");
            afficherProjetTableau(projetRecupere);
        } else {
            System.out.println("Projet non trouvé !");
        }
    }

    // Méthode pour mettre à jour un projet
    private static void mettreAJourProjet() {
        System.out.print("Entrez l'ID du projet à mettre à jour : ");
        int updateId = scanner.nextInt();
        scanner.nextLine(); // Consommer la nouvelle ligne
        Project projetAMettreAJour = projectService.getProjectById(updateId);
        if (projetAMettreAJour != null) {
            System.out.print("Entrez le nouveau nom du projet (ou appuyez sur Entrée pour garder le nom actuel) : ");
            String nouveauNom = scanner.nextLine();
            if (!nouveauNom.isEmpty()) {
                projetAMettreAJour.setNomProject(nouveauNom);
            }

            System.out.print("Entrez la nouvelle marge bénéficiaire (ou appuyez sur Entrée pour garder la marge actuelle) : ");
            String nouvelleMargeStr = scanner.nextLine();
            if (!nouvelleMargeStr.isEmpty()) {
                projetAMettreAJour.setMargeBeneficiaire(Double.parseDouble(nouvelleMargeStr));
            }

            System.out.print("Entrez le nouveau coût total (ou appuyez sur Entrée pour garder le coût actuel) : ");
            String nouveauCoutStr = scanner.nextLine();
            if (!nouveauCoutStr.isEmpty()) {
                projetAMettreAJour.setCouTotal(Double.parseDouble(nouveauCoutStr));
            }

            System.out.print("Entrez le nouveau statut du projet (ou appuyez sur Entrée pour garder le statut actuel) : ");
            String nouveauStatutStr = scanner.nextLine();
            if (!nouveauStatutStr.isEmpty()) {
                try {
                    projetAMettreAJour.setEtatProject(EtatProject.valueOf(nouveauStatutStr));
                } catch (IllegalArgumentException e) {
                    System.out.println("Statut invalide. Statut actuel conservé.");
                }
            }

            projectService.updateProject(projetAMettreAJour);
            System.out.println("Projet mis à jour avec succès !");
        } else {
            System.out.println("Projet non trouvé !");
        }
    }

    // Méthode pour supprimer un projet
    private static void supprimerProjet() {
        System.out.print("Entrez l'ID du projet à supprimer : ");
        int deleteId = scanner.nextInt();
        projectService.deleteProject(deleteId);
        System.out.println("Projet supprimé avec succès !");
    }

    // Méthode pour lister tous les projets
    private static void listerTousLesProjets() {
        List<Project> projets = projectService.getAllProjects();
        System.out.println("\n===== Liste des Projets =====");
        for (Project p : projets) {
            afficherProjetTableau(p);
        }
    }

    // Fonction d'affichage formaté d'un projet en tableau
    private static void afficherProjetTableau(Project projet) {
        System.out.printf("%-15s %-20s %-20s %-10s %-10s\n", "ID Projet", "Nom", "Marge Bénéficiaire", "Coût Total", "Statut");
        System.out.printf("%-15d %-20s %-20.2f %-10.2f %-10s\n", projet.getId(), projet.getNomProject(),
                projet.getMargeBeneficiaire(), projet.getCouTotal(), projet.getEtatProject());
    }
}
