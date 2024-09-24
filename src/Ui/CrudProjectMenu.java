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
import java.time.format.DateTimeParseException;
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

    // ANSI color codes for terminal output
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_CYAN = "\u001B[36m";

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
                System.out.println(ANSI_RED + "\n[Erreur] Veuillez entrer un chiffre valide." + ANSI_RESET);
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
                    System.out.println(ANSI_RED + "\nChoix invalide ! Veuillez réessayer." + ANSI_RESET);
            }
        }
    }

    private boolean getYesNoInput(String prompt) {
        System.out.print(prompt);
        String input = scanner.next().trim().toLowerCase();
        scanner.nextLine(); // Consomme la nouvelle ligne
        return input.equals("y") || input.equals("yes");
    }

    public void calculerCoutProjet()    {
        System.out.println(ANSI_YELLOW + "--- Total Cost Calculation ---" + ANSI_RESET);

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

            if (marginRate >= 0) {
                project.setMargeBeneficiaire(marginRate);
                totalCost = totalCost + (totalCost * marginRate / 100);
            } else {
                System.out.println("Invalid margin percentage, please enter a non-negative value.");
            }
        }

        projectService.updateMarginAndTotalCost_Project(projectId, project.getMargeBeneficiaire(), totalCost);


        System.out.println(ANSI_YELLOW + "\n--- Calculation Result ---" + ANSI_RESET);
        System.out.println("Project Name: " + project.getNomProject());
        System.out.println("Client: " + project.getUser().getNom());
        System.out.println("Address: " + project.getUser().getAddress());
        System.out.println(ANSI_CYAN + "--- Cost Details ---" + ANSI_RESET);
        System.out.println("Materials Cost Before VAT: " + String.format("%.2f", totalMaterialBeforeVat) + " €");
        System.out.println("Materials Cost After VAT: " + String.format("%.2f", totalMaterialAfterVat) + " €");
        System.out.println("Labors Cost Before VAT: " + String.format("%.2f", totalLaborsBeforeVat) + " €");
        System.out.println("Labors Cost After VAT: " + String.format("%.2f", totalLaborsAfterVat) + " €");
        System.out.println("Total Cost Before Margin: " + String.format("%.2f", totalCostBeforeMargin) + " €");


        if (project.getUser().isEstProfessional()) {
            System.out.println(ANSI_GREEN + "\n--- Professional Client Discount Applied ---" + ANSI_RESET);
            totalCost *= discount;
            System.out.println("Discounted Total Cost: " + String.format("%.2f", totalCost) + " €");
        }

        System.out.println("\nEnter issue date (yyyy-MM-dd): ");
        LocalDate issueDate = null;


        while (issueDate == null) {
            String issue_Date = scanner.nextLine().trim();
            try {
                issueDate = LocalDate.parse(issue_Date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (DateTimeParseException e) {
                System.out.println(ANSI_RED + "Invalid date format. Please enter a valid date in the format yyyy-MM-dd." + ANSI_RESET);
                System.out.println("\nEnter issue date (yyyy-MM-dd): ");
            }
        }


        // Validation de la date de validation
        LocalDate validatedDate = null;
        while (validatedDate == null || validatedDate.isBefore(issueDate)) {
            System.out.println("\n Enter validated date (yyyy-MM-dd): ");
            String validated_Date = scanner.nextLine().trim();
            try {
                validatedDate = LocalDate.parse(validated_Date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                if (validatedDate.isBefore(issueDate)) {
                    System.out.println(ANSI_RED + "The validated date must be after the issue date (" + issueDate + ")." + ANSI_RESET);
                    validatedDate = null;  // Reset to force another input
                }
            } catch (DateTimeParseException e) {
                System.out.println(ANSI_RED + "Invalid date format. Please enter a valid date in the format yyyy-MM-dd." + ANSI_RESET);
            }
        }


            afficherDetailsDevis(project, totalMaterialBeforeVat, totalMaterialAfterVat, totalLaborsBeforeVat, totalLaborsAfterVat, totalCostBeforeMargin, totalCost, project.getUser().isEstProfessional(), issueDate, validatedDate);


        Devi devis = new Devi(0, totalCost, issueDate, false,validatedDate, project.getId());
        deviService.save(devis);


        System.out.print("Do you want to accept the devis? (Yes/No): ");
        String choice = scanner.nextLine().trim().toLowerCase();

        switch (choice) {
            case "yes":
            case "y":
                deviService.updateDevisStatus(devis.getId());
                projectService.updateStatus(projectId, EtatProject.TERMINE.name());
                System.out.println(ANSI_GREEN + "Devis accepted. Project marked as FINISHED." + ANSI_RESET);
                break;
            case "no":
            case "n":
                projectService.updateStatus(projectId, EtatProject.ANNULE.name());
                System.out.println(ANSI_RED + "Devis rejected. Project marked as CANCELLED." + ANSI_RESET);
                break;
            default:
                System.out.println(ANSI_RED + "Invalid choice. Please enter 'Yes' or 'No'." + ANSI_RESET);
        }

    }

    // Afficher le menu principal
    private static void afficherMenuPrincipal() {
        System.out.println(ANSI_BLUE + "\n==========================" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "=== Menu Principal ===" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "==========================" + ANSI_RESET);

        System.out.println(ANSI_GREEN + "1. Créer un nouveau projet 🆕" + ANSI_RESET);
        System.out.println(ANSI_GREEN + "2. Afficher les projets existants 📁" + ANSI_RESET);
        System.out.println(ANSI_GREEN + "3. Calculer le coût d'un projet 💰" + ANSI_RESET);
        System.out.println(ANSI_GREEN + "4. Quitter ❌" + ANSI_RESET);

        System.out.println(ANSI_BLUE + "==========================" + ANSI_RESET);
        System.out.print("Votre choix: ");
    }


    private void afficherProjetsParUtilisateur() {
        System.out.println(ANSI_YELLOW + "--- Projets Existants ---" + ANSI_RESET);
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


    public void creerNouveauProjet() {
        User client = null;

        System.out.println(ANSI_YELLOW + "\n--- Recherche de client ---" + ANSI_RESET);
        System.out.println("1. Chercher un client existant 📇");
        System.out.println("2. Ajouter un nouveau client ✏️");
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
            System.out.println(ANSI_RED + "\nChoix invalide. Retour au menu principal." + ANSI_RESET);
            return;
        }

        // Création du projet
        System.out.println(ANSI_YELLOW + "\n--- Création d'un Nouveau Projet ---" + ANSI_RESET);
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

        System.out.println(ANSI_GREEN + "Projet '" + nomProjet + "' pour une surface de " + surfaceCuisine + " m² a été créé et ajouté à la base de données avec succès !" + ANSI_RESET);

        // Nouveau menu pour choisir quoi ajouter
        boolean continuerAjout = true;
        while (continuerAjout) {
            System.out.println(ANSI_YELLOW + "\nQue souhaitez-vous ajouter au projet ?" + ANSI_RESET);
            System.out.println("1. Ajouter de la main-d'œuvre 👷‍♂️");
            System.out.println("2. Ajouter des matériaux 🧱");
            System.out.println("3. Terminer ✅");
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
                    System.out.println(ANSI_GREEN + "Ajouts terminés pour le projet." + ANSI_RESET);
                    break;
                default:
                    System.out.println(ANSI_RED + "Choix invalide. Veuillez réessayer." + ANSI_RESET);
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
        System.out.println(ANSI_YELLOW + "--- Fin du projet ---" + ANSI_RESET);
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
        scanner.nextLine();

        // Créer et ajouter la main-d'œuvre
        Labor labor = new Labor(type, "Main-d'œuvre", 0, heuresTravail, 0, tauxHoraire, productuviteOuvrier);
        labor.setProject(project);
        laborService.addLabor(labor);

        System.out.println("Main-d'œuvre ajoutée avec succès !");

        System.out.print("Voulez-vous ajouter un autre type de main-d'œuvre ? (y/n) : ");
        String reponse = scanner.nextLine();
        if (reponse.equalsIgnoreCase("y")) {
            ajouterMainOeuvre(project); // Récursion pour ajouter un autre type
        } else {
            System.out.println("Ajout de la main-d'œuvre terminé.");
        }








}

    private void afficherDetailsDevis(Project project, double totalMaterialBeforeVat, double totalMaterialAfterVat, double totalLaborsBeforeVat, double totalLaborsAfterVat, double totalCostBeforeMargin, double totalCost, boolean isProfessionalClient, LocalDate issueDate, LocalDate validatedDate) {
        System.out.println(ANSI_YELLOW + "\n--- Devis Détails ---" + ANSI_RESET);
        System.out.printf("%-20s : %s%n", "Nom du Projet", project.getNomProject());
        System.out.printf("%-20s : %s%n", "Client", project.getUser().getNom());
        System.out.printf("%-20s : %s%n", "Adresse", project.getUser().getAddress());
        System.out.printf("%-20s : %.2f €%n", "Coût Matériaux (HT)", totalMaterialBeforeVat);
        System.out.printf("%-20s : %.2f €%n", "Coût Matériaux (TTC)", totalMaterialAfterVat);
        System.out.printf("%-20s : %.2f €%n", "Coût Main d'Oeuvre (HT)", totalLaborsBeforeVat);
        System.out.printf("%-20s : %.2f €%n", "Coût Main d'Oeuvre (TTC)", totalLaborsAfterVat);
        System.out.printf("%-20s : %.2f €%n", "Coût Total (HT)", totalCostBeforeMargin);
        if (isProfessionalClient) {
            System.out.println(ANSI_GREEN + "\n--- Remise Appliquée ---" + ANSI_RESET);
            System.out.printf("%-20s : %.2f €%n", "Coût Total (avec remise)", totalCost);
        }
        System.out.printf("%-20s : %s%n", "Date d'Émission", issueDate);
        System.out.printf("%-20s : %s%n", "Date Validée", validatedDate);
    }

}