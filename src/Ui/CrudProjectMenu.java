package Ui;

import Entity.*;
import Entity.enums.EtatProject;
import Repository.ProjectRepository;
import Repository.UserRepository;
import Service.*;
import Service.Interface.LaborServiceInter;
import Service.Interface.UserServiceInter;
import config.DatabaseConnection;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class CrudProjectMenu {

    private UserServiceInter userService;
    private ProjectService projectService;
    private MaterialService materialService;
    private LaborServiceInter laborService;
    private DeviService deviService;
    private Scanner scanner;
    private double discount = 0.9;

    public CrudProjectMenu(UserServiceInter userService, ProjectService projectService, MaterialService materialService, LaborServiceInter laborService, Scanner scanner,DeviService deviService) {
        this.userService = userService;
        this.projectService = projectService;
        this.materialService = materialService;
        this.laborService = laborService;
        this.scanner = scanner;
        this.deviService=deviService;
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

                default:
                    System.out.println("\nChoix invalide ! Veuillez réessayer.");
            }
        }
    }

    private boolean getYesNoInput(String prompt) {
        System.out.print(prompt);
        String input = scanner.next().trim().toLowerCase();
        return input.equals("y") || input.equals("yes");
    }

    public void calculerCoutProjet() {
        System.out.println("--- Total Cost Calculation ---");

        System.out.print("Enter project ID: ");
        int projectId = scanner.nextInt();

        Project project = projectService.getProjectById(projectId);

        List<Material> materials = materialService.findAllMaterialsByProject(projectId);
        List<Labor> Labors = laborService.findAllLaborsByProject(projectId);

        double totalMaterialBeforeVat = 0;
        double totalMaterialAfterVat = 0;

        for (Material material : materials) {
            double materialCostBeforeVat = materialService.calculateMaterialBeforeVatRate(material);
            double materialCostAfterVat = materialService.calculateMaterialAfterVatRate(material);

            totalMaterialBeforeVat += materialCostBeforeVat;
            totalMaterialAfterVat += materialCostAfterVat;
        }

        double totalLaborsBeforeVat = 0;
        double totalLaborsAfterVat = 0;

        for (Labor labor : Labors) {
            double laborCostBeforeVat = laborService.calculateLaborBeforeVatRate(labor);
            double laborCostAfterVat = laborService.calculateLaborAfterVatRate(labor);

            totalLaborsBeforeVat += laborCostBeforeVat;
            totalLaborsAfterVat += laborCostAfterVat;
        }

        double totalCostBeforeMargin = totalMaterialBeforeVat + totalLaborsBeforeVat;
        double totalCostAfterVat = totalMaterialAfterVat + totalLaborsAfterVat;

        double totalCost = totalCostAfterVat;
        double marginRate = 0.0;
        if (getYesNoInput("Do you want to apply a profit margin to the project? (y/n): ")) {
            System.out.print("Enter profit margin percentage: ");
            marginRate = scanner.nextDouble();
            scanner.nextLine();
            project.setMargeBeneficiaire(marginRate);
            totalCost=totalCost+(totalCost*marginRate/100);
        }

        projectService.updateMarginAndTotalCost_Project(projectId, project.getMargeBeneficiaire(), totalCost);


        System.out.println("\n--- Calculation Result ---");
        System.out.println("Project Name: " + project.getNomProject());
        System.out.println("Client: " + project.getUser().getNom());
        System.out.println("Address: " + project.getUser().getAddress());
        System.out.println("--- Cost Details ---");
        System.out.println("Materials Cost Before VAT: " + String.format("%.2f", totalMaterialBeforeVat) + " €");
        System.out.println("Materials Cost After VAT: " + String.format("%.2f", totalMaterialAfterVat) + " €");
        System.out.println("Labors Cost Before VAT: " + String.format("%.2f", totalLaborsBeforeVat) + " €");
        System.out.println("Labors Cost After VAT: " + String.format("%.2f", totalLaborsAfterVat) + " €");
        System.out.println("Total Cost Before Margin: " + String.format("%.2f", totalCostBeforeMargin) + " €");


        if (project.getUser().isEstProfessional()) {
            System.out.println("\n--- Professional Client Discount Applied ---");
            totalCost *= discount;
            System.out.println("Discounted Total Cost: " + String.format("%.2f", totalCost) + " €");
        }

        System.out.println("\nEnter issue date (yyyy-MM-dd): ");
        String issue_Date = scanner.nextLine();
        LocalDate issueDate = LocalDate.parse(issue_Date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        System.out.println("\n Enter validated date (yyyy-MM-dd): ");
        String validated_Date = scanner.nextLine();
        LocalDate validatedDate = LocalDate.parse(validated_Date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        while(validatedDate.isBefore(issueDate)){
            System.out.println("\nEnter the end date (yyyy-MM-dd): After = "+ issueDate);
            validated_Date = scanner.nextLine();
            validatedDate = LocalDate.parse(validated_Date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }


        Devi devis = new Devi(0, totalCost, issueDate, false,validatedDate, project.getId());
        deviService.save(devis);


        System.out.print("Do you want to accept the devis? (Yes/No): ");
        String choice = scanner.nextLine().trim().toLowerCase();

        switch (choice) {
            case "yes":
            case "y":
                deviService.updateDevisStatus(devis.getId());
                projectService.updateStatus(projectId, EtatProject.TERMINE.name());
                System.out.println("Devis accepted. Project marked as FINISHED.");
                break;
            case "no":
            case "n":
                projectService.updateStatus(projectId, EtatProject.ANNULE.name());
                System.out.println("Devis rejected. Project marked as CANCELLED.");
                break;
            default:
                System.out.println("Invalid choice. Please enter 'Yes' or 'No'.");
        }
//        try {
//            devisMenu.findDevisByProject(projectId);
//        } catch (QuotesNotFoundException devisNotFoundException) {
//            System.out.println(devisNotFoundException.getMessage());
//        }
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
        scanner.nextLine();

        System.out.print("Entrez la marge bénéficiaire (en %) : ");
        double margeBeneficiaire = scanner.nextDouble();
        scanner.nextLine();

        // Créer un nouvel objet Project
        Project nouveauProjet = new Project(nomProjet, margeBeneficiaire, surfaceCuisine, EtatProject.EN_COURS, client);
        Project savedProject = projectService.addProject(nouveauProjet);

        System.out.println("Projet '" + nomProjet + "' pour une surface de " + surfaceCuisine + " m² a été créé et ajouté à la base de données avec succès !");

        // Nouveau menu pour choisir quoi ajouter
        boolean continuerAjout = true;
        while (continuerAjout) {
            System.out.println("\nQue souhaitez-vous ajouter au projet ?");
            System.out.println("1. Ajouter de la main-d'œuvre");
            System.out.println("2. Ajouter des matériaux");
            System.out.println("3. Terminer");
            System.out.print("Choisissez une option : ");

            int choixAjout = scanner.nextInt();
            scanner.nextLine();

            switch (choixAjout) {
                case 1:
                    ajouterMainOeuvre(savedProject);
                    break;
                case 2:
                    ajouterMateriaux(savedProject);
                    break;
                case 3:
                    continuerAjout = false;
                    System.out.println("Ajouts terminés pour le projet.");
                    break;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
        }
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
            Material material = new Material(nom, typeComposant, 0, 0, quantite, coutUnitaire, coutTransport, coefficientQualite);
            material.setProject(projet);
            materialService.addMaterial(material);
            System.out.println("Matériau ajouté avec succès pour le projet " + projet.getNomProject() + " !");


            // Demander si l'utilisateur veut ajouter un autre matériau
            System.out.print("Voulez-vous ajouter un autre matériau ? (y/n) : ");
            String reponse = scanner.nextLine();
            continuer = reponse.equalsIgnoreCase("y");
        }
    }

    public void ajouterMainOeuvre(Project project) {
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
                scanner.next();
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
                scanner.next();
            }
        }

        // Créer et ajouter la main-d'œuvre
        Labor labor = new Labor(type, "Main-d'œuvre", 0, heuresTravail, 0, tauxHoraire, productuviteOuvrier);
        labor.setProject(project);
        laborService.addLabor(labor);

        System.out.println("Main-d'œuvre ajoutée avec succès !");

        System.out.print("Voulez-vous ajouter un autre type de main-d'œuvre ? (y/n) : ");
        String reponse = scanner.nextLine();
        if (reponse.equalsIgnoreCase("y")) {
            ajouterMainOeuvre(project); // Récursion pour ajouter un autre type
        }








}
}