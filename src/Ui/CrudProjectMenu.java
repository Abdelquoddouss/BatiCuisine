package Ui;

import Entity.Project;
import Entity.User;
import Entity.enums.EtatProject;
import Repository.ProjectRepository;
import Repository.UserRepository;
import Service.ProjectService;
import Service.UserService;
import config.DatabaseConnection;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class CrudProjectMenu {

    private UserService userService;
    private ProjectService projectService;
    private Scanner scanner;

    public CrudProjectMenu(UserService userService, ProjectService projectService, Scanner scanner) {
        this.userService = userService;
        this.projectService = projectService;
        this.scanner = scanner;
    }

    public void afficherMenu() {
        int choix = -1;
        while (choix != 4) {
            afficherMenuPrincipal();
            try {
                choix = scanner.nextInt();
                scanner.nextLine(); // Consomme la nouvelle ligne
            } catch (InputMismatchException e) {
                System.out.println("\n[Erreur] Veuillez entrer un chiffre valide.");
                scanner.next(); // Consomme l'entrée incorrecte
                continue;
            }

            switch (choix) {
                case 1:
                    creerNouveauProjet();
                    break;
                case 2:
                    afficherProjetsExistants();
                    break;
                case 3:
                    calculerCoutProjet();
                    break;
                case 4:
                    quitter();
                    break;
                default:
                    System.out.println("\nChoix invalide ! Veuillez réessayer.");
            }
        }
    }

    // Afficher le menu principal
    private static void afficherMenuPrincipal() {
        System.out.println("\n=== Menu Principal ===");
        System.out.println("1. Créer un nouveau projet");
        System.out.println("2. Afficher les projets existants");
        System.out.println("3. Calculer le coût d'un projet");
        System.out.println("4. Quitter");
        System.out.print("Choisissez une option : ");
    }

    public  void creerNouveauProjet() {
        User client = null;

        System.out.println("\n--- Recherche de client ---");
        System.out.println("1. Chercher un client existant");
        System.out.println("2. Ajouter un nouveau client");
        System.out.print("Choisissez une option : ");
        int choixClient = scanner.nextInt();
        scanner.nextLine();

        if (choixClient == 1) {
            // Rechercher un client existant
            client = rechercherClientExistant();
        } else if (choixClient == 2) {
            // Appel de la méthode dans CrudUserMenu
            CrudUserMenu crudUserMenu = new CrudUserMenu(userService, scanner);
            crudUserMenu.afficherMenuUtilisateur(scanner);
            System.out.print("Entrez le nom du client que vous venez d'ajouter ou gérer : ");
            String clientNom = scanner.nextLine();
            client = userService.getUserByName(clientNom);
        } else {
            System.out.println("\nChoix invalide. Retour au menu principal.");
            return;
        }

        // Création du projet
        System.out.println("\n--- Création d'un Nouveau Projet ---");
        System.out.print("Entrez le nom du projet : ");
        String nomProjet = scanner.nextLine();

        System.out.print("Entrez la surface de la cuisine (en m²) : ");
        double surfaceCuisine = scanner.nextDouble();
        scanner.nextLine(); // Consommer la nouvelle ligne

        System.out.print("Entrez la marge bénéficiaire (en %) : ");
        double margeBeneficiaire = scanner.nextDouble();
        scanner.nextLine();


        // Créer un nouvel objet Project
        Project nouveauProjet = new Project(nomProjet, margeBeneficiaire, surfaceCuisine, EtatProject.EN_COURS, client);

        // Sauvegarder dans la base de données
        projectService.addProject(nouveauProjet);

        System.out.println("Projet '" + nomProjet + "' pour une surface de " + surfaceCuisine + " m² a été créé et ajouté à la base de données avec succès !");
    }

    private  User rechercherClientExistant() {
        System.out.println("\n--- Recherche de client existant ---");
        System.out.print("Entrez le nom du client : ");
        String nomUtilisateur = scanner.nextLine();
        User utilisateurRecupere = userService.getUserByName(nomUtilisateur);
        if (utilisateurRecupere != null) {
            System.out.println("Détails de l'utilisateur : ");
            System.out.printf("%-15s : %s%n", "ID", utilisateurRecupere.getId());
            System.out.printf("%-15s : %s%n", "Nom", utilisateurRecupere.getNom());
            System.out.printf("%-15s : %s%n", "Adresse", utilisateurRecupere.getAddress());
            System.out.printf("%-15s : %s%n", "Téléphone", utilisateurRecupere.getTelephone());
            System.out.printf("%-15s : %s%n", "Professionnel", utilisateurRecupere.isEstProfessional() ? "Oui" : "Non");
        } else {
            System.out.println("Utilisateur non trouvé !");
        }

        return utilisateurRecupere;
    }

    private  User ajouterNouveauClient() {
        System.out.println("\n--- Ajout d'un nouveau client ---");
        System.out.print("Entrez le nom du client : ");
        String nomClient = scanner.nextLine();

        System.out.print("Entrez l'adresse du client : ");
        String adresseClient = scanner.nextLine();

        System.out.print("Entrez le numéro de téléphone du client : ");
        String telephoneClient = scanner.nextLine();

        System.out.println("Le client '" + nomClient + "' a été ajouté.");

        // Créer un nouvel objet User
        User client = new User(0, nomClient, adresseClient, telephoneClient, false);

        // Sauvegarder dans la base de données
        userService.addUser(client);

        return client;
    }


    // Afficher les projets existants
    private  void afficherProjetsExistants() {
        System.out.println("\n--- Projets Existants ---");

        // Récupérer la liste des projets depuis le service
        List<Project> projets = projectService.getAllProjects();

        // Vérifier si des projets existent
        if (projets.isEmpty()) {
            System.out.println("Aucun projet trouvé.");
            return;
        }

        // Afficher les détails de chaque projet
        for (Project projet : projets) {
            System.out.printf("ID: %d | Nom: %s | Surface: %.2f m² | État: %s%n",
                    projet.getId(),
                    projet.getNomProject(),
                    projet.getMargeBeneficiaire(),
                    projet.getEtatProject().name());
        }
    }


    // Calculer le coût d'un projet
    private  void calculerCoutProjet() {
        System.out.println("\n--- Calcul du Coût d'un Projet ---");
        // Logique pour calculer le coût
        System.out.print("Entrez l'ID du projet : ");
        int idProjet = scanner.nextInt();
        scanner.nextLine(); // Consommer la nouvelle ligne

        System.out.println("Le coût estimé pour le projet avec ID " + idProjet + " est de 15 000 €.");
    }

    // Quitter le programme
    private  void quitter() {
        System.out.println("\nMerci d'avoir utilisé notre service. À bientôt !");
    }
}
