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
import java.util.Scanner;

public class CrudProjectMenu {


    private static UserRepository userRepository = new UserRepository(DatabaseConnection.getConnection());
    private static UserService userService = new UserService(new UserRepository(DatabaseConnection.getConnection()));
    private static ProjectService projectService = new ProjectService(new ProjectRepository(DatabaseConnection.getConnection(), userRepository));
    private static Scanner scanner = new Scanner(System.in);

    public CrudProjectMenu(UserService userService, ProjectService projectService) {
        this.userService = userService;
        this.projectService = projectService;
    }

    public static void main(String[] args) {
        int choix = -1;

        while (choix != 4) {
            afficherMenuPrincipal();

            try {
                choix = scanner.nextInt();
                scanner.nextLine(); // Consommer la nouvelle ligne
            } catch (InputMismatchException e) {
                System.out.println("\n[Erreur] Veuillez entrer un chiffre valide.");
                scanner.next(); // Consommer l'entrée incorrecte
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

    private static void creerNouveauProjet() {
        User client = null;

        System.out.println("\n--- Recherche de client ---");
        System.out.println("1. Chercher un client existant");
        System.out.println("2. Ajouter un nouveau client");
        System.out.print("Choisissez une option : ");
        int choixClient = scanner.nextInt();
        scanner.nextLine(); // Consommer la nouvelle ligne

        if (choixClient == 1) {
            client = rechercherClientExistant();
        } else if (choixClient == 2) {
            client = ajouterNouveauClient();
        } else {
            System.out.println("\nChoix invalide. Retour au menu principal.");
            return;
        }

        if (client == null) {
            System.out.println("\nAucun client sélectionné. Retour au menu principal.");
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

    private static User rechercherClientExistant() {
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

    private static User ajouterNouveauClient() {
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
    private static void afficherProjetsExistants() {
        System.out.println("\n--- Projets Existants ---");
        // Logique pour afficher les projets (liste factice ici)
        System.out.println("1. Projet Rénovation Cuisine Mme Dupont");
        System.out.println("2. Projet Aménagement Salle de Bain Mr Martin");
    }

    // Calculer le coût d'un projet
    private static void calculerCoutProjet() {
        System.out.println("\n--- Calcul du Coût d'un Projet ---");
        // Logique pour calculer le coût
        System.out.print("Entrez l'ID du projet : ");
        int idProjet = scanner.nextInt();
        scanner.nextLine(); // Consommer la nouvelle ligne

        System.out.println("Le coût estimé pour le projet avec ID " + idProjet + " est de 15 000 €.");
    }

    // Quitter le programme
    private static void quitter() {
        System.out.println("\nMerci d'avoir utilisé notre service. À bientôt !");
    }
}
