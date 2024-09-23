package Ui;

import Entity.Labor;
import Entity.Material;
import Entity.Project;
import Entity.User;
import Entity.enums.EtatProject;
import Repository.ProjectRepository;
import Repository.UserRepository;
import Service.Interface.LaborServiceInter;
import Service.Interface.UserServiceInter;
import Service.LaborService;
import Service.MaterialService;
import Service.ProjectService;
import Service.UserService;
import config.DatabaseConnection;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class CrudProjectMenu {

    private UserServiceInter userService;
    private ProjectService projectService;
    private MaterialService materialService;
    private LaborServiceInter laborService;
    private Scanner scanner;

    public CrudProjectMenu(UserServiceInter userService, ProjectService projectService, MaterialService materialService, LaborServiceInter laborService, Scanner scanner) {
        this.userService = userService;
        this.projectService = projectService;
        this.materialService = materialService;
        this.laborService = laborService;
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
                    afficherProjetsParUtilisateur();
                    break;
                case 3:
                    calculerCoutProjet();
                    break;

                case 4:
                    quitter();
                    break;
                case 5:
                    ajouterMainOeuvre();

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
        System.out.println("5. Ajouter de la main-d'œuvre");
        System.out.println("3. Calculer le coût d'un projet");
        System.out.println("4. Quitter");
        System.out.print("Choisissez une option : ");
    }

    public void creerNouveauProjet() {
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
        projectService.addProject(nouveauProjet);

        System.out.println("Projet '" + nomProjet + "' pour une surface de " + surfaceCuisine + " m² a été créé et ajouté à la base de données avec succès !");

        // Ajout des matériaux associés au projet
        ajouterMateriaux(nouveauProjet);
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


    private void afficherProjetsParUtilisateur() {
        System.out.println("\n--- Afficher Projets par Utilisateur ---");
        Scanner scanner = new Scanner(System.in);

        System.out.print("Entrez l'ID de l'utilisateur : ");
        int userId = scanner.nextInt();

        List<Project> projets = projectService.getProjectsByUserId(userId);
        Project projet= projectService.getProjectById(9);
        if (projets.isEmpty()) {
            System.out.println("Aucun projet trouvé pour l'utilisateur avec ID: " + userId);
        } else {
            System.out.printf("%-5s %-20s %-20s %-15s %-15s%n", "ID", "nomproject", "coutotal", "etatproject", "client_id");
            System.out.println("-------------------------------------------------------------------------------");
            for (Project p : projets) {
                System.out.printf("%-5d %-20s %-20s %-15s %-15s%n",
                        p.getId(), p.getNomProject(), p.getCouTotal(), p.getEtatProject(), p.getUser() != null ? p.getUser().getNom() : "Aucun utilisateur"
                );
            }

        }
    }

    public void ajouterMateriaux(Project projet) {
        boolean continuer = true;

        while (continuer) {
            System.out.println("\n--- Ajout des Matériaux pour le projet " + projet.getNomProject() + " ---");

            // Demande des détails sur le matériau
            System.out.print("Entrez le nom du matériau : ");
            String nom = scanner.nextLine();

            System.out.print("Entrez le type de composant (ex: carrelage, bois, etc.) : ");
            String typeComposant = scanner.nextLine();

            double tauxTva = 0;
            boolean validInput = false;
            while (!validInput) {
                try {
                    System.out.print("Entrez la TVA applicable (%) : ");
                    tauxTva = scanner.nextDouble();
                    validInput = true;
                } catch (InputMismatchException e) {
                    System.out.println("Erreur : veuillez entrer un nombre valide pour la TVA.");
                    scanner.next(); // Nettoyer l'entrée incorrecte
                }
            }

            int quantite = 0;
            validInput = false;
            while (!validInput) {
                try {
                    System.out.print("Entrez la quantité de ce matériau (en m²) : ");
                    quantite = scanner.nextInt();
                    validInput = true;
                } catch (InputMismatchException e) {
                    System.out.println("Erreur : veuillez entrer un nombre entier pour la quantité.");
                    scanner.next(); // Nettoyer l'entrée incorrecte
                }
            }

            double coutUnitaire = 0;
            validInput = false;
            while (!validInput) {
                try {
                    System.out.print("Entrez le coût unitaire de ce matériau (€/m²) : ");
                    coutUnitaire = scanner.nextDouble();
                    validInput = true;
                } catch (InputMismatchException e) {
                    System.out.println("Erreur : veuillez entrer un nombre valide pour le coût unitaire.");
                    scanner.next(); // Nettoyer l'entrée incorrecte
                }
            }

            double coutTransport = 0;
            validInput = false;
            while (!validInput) {
                try {
                    System.out.print("Entrez le coût de transport de ce matériau (€) : ");
                    coutTransport = scanner.nextDouble();
                    validInput = true;
                } catch (InputMismatchException e) {
                    System.out.println("Erreur : veuillez entrer un nombre valide pour le coût de transport.");
                    scanner.next(); // Nettoyer l'entrée incorrecte
                }
            }

            double coefficientQualite = 0;
            validInput = false;
            while (!validInput) {
                try {
                    System.out.print("Entrez le coefficient de qualité du matériau (1.0 = standard, > 1.0 = haute qualité) : ");
                    coefficientQualite = scanner.nextDouble();
                    validInput = true;
                } catch (InputMismatchException e) {
                    System.out.println("Erreur : veuillez entrer un nombre valide pour le coefficient de qualité.");
                    scanner.next(); // Nettoyer l'entrée incorrecte
                }
            }

            scanner.nextLine();

            // Créer et ajouter le matériau
            Material material = new Material(nom, typeComposant, tauxTva, 0, quantite, coutUnitaire, coutTransport, coefficientQualite);
            material.setProject(projet);
            materialService.addMaterial(material);
            System.out.println("Matériau ajouté avec succès pour le projet " + projet.getNomProject() + " !");


            // Demander si l'utilisateur veut ajouter un autre matériau
            System.out.print("Voulez-vous ajouter un autre matériau ? (y/n) : ");
            String reponse = scanner.nextLine();
            continuer = reponse.equalsIgnoreCase("y");
        }
    }

    public void ajouterMainOeuvre() {
        System.out.println("\n--- Ajout de la main-d'œuvre ---");

        System.out.print("Entrez le type de main-d'œuvre (e.g., Ouvrier de base, Spécialiste) : ");
        String type = scanner.nextLine();

        double tauxHoraire = 0;
        while (true) {
            try {
                System.out.print("Entrez le taux horaire de cette main-d'œuvre (€/h) : ");
                tauxHoraire = scanner.nextDouble();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Erreur : veuillez entrer un nombre valide pour le taux horaire.");
                scanner.next(); // Nettoyer l'entrée incorrecte
            }
        }

        double heuresTravail = 0;
        while (true) {
            try {
                System.out.print("Entrez le nombre d'heures travaillées : ");
                heuresTravail = scanner.nextDouble();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Erreur : veuillez entrer un nombre valide pour les heures travaillées.");
                scanner.next(); // Nettoyer l'entrée incorrecte
            }
        }

        double productuviteOuvrier = 0;
        while (true) {
            try {
                System.out.print("Entrez le facteur de productivité (1.0 = standard, > 1.0 = haute productivité) : ");
                productuviteOuvrier = scanner.nextDouble();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Erreur : veuillez entrer un nombre valide pour le facteur de productivité.");
                scanner.next(); // Nettoyer l'entrée incorrecte
            }
        }

        // Créer et ajouter la main-d'œuvre
        Labor labor = new Labor(type, "Main-d'œuvre", 0, heuresTravail, 0, tauxHoraire, productuviteOuvrier);
        laborService.addLabor(labor);

        System.out.println("Main-d'œuvre ajoutée avec succès !");

        System.out.print("Voulez-vous ajouter un autre type de main-d'œuvre ? (y/n) : ");
        String reponse = scanner.next();
        if (reponse.equalsIgnoreCase("y")) {
            ajouterMainOeuvre(); // Récursion pour ajouter un autre type
        }
    }




}
