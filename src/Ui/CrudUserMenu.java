package Ui;

import Entity.User;
import Repository.ProjectRepository;
import Repository.UserRepository;
import Service.Interface.UserServiceInter;
import Service.ProjectService;
import Service.UserService;
import config.DatabaseConnection;

import java.sql.Connection;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class CrudUserMenu {

    private static UserServiceInter userService;
    private static Scanner scanner;

    public CrudUserMenu(UserServiceInter userService, Scanner scanner) {
        this.userService = userService;
        this.scanner = scanner;
    }

    public void afficherMenuUtilisateur(Scanner scanner) {
        int choix = -1;
        while (choix != 3) {
            System.out.println("\n=== Menu Utilisateur ===");
            System.out.println("1. Ajouter un utilisateur");
            System.out.println("2. Afficher les utilisateurs");
            System.out.println("3. Quitter");
            System.out.print("Choisissez une option : ");
            try {
                choix = scanner.nextInt();
                scanner.nextLine(); // Consomme la nouvelle ligne
            } catch (InputMismatchException e) {
                System.out.println("Erreur: Veuillez entrer un nombre.");
                scanner.next(); // Consomme l'entrée incorrecte
                continue;
            }

            switch (choix) {
                case 1:
                    ajouterUtilisateur(scanner);
                    break;
                case 2:
                    listerTousLesUtilisateurs();
                    break;
                case 3:
                    System.out.println("Retour au menu principal.");
                    break;
                default:
                    System.out.println("Choix invalide.");
            }
        }
    }

    // Méthode pour ajouter un utilisateur
    private static void ajouterUtilisateur(Scanner scanner) {
        System.out.println("\n--- Ajouter un utilisateur ---");
        System.out.print("Entrez le nom de l'utilisateur : ");
        String nom = scanner.nextLine();
        System.out.print("Entrez l'adresse de l'utilisateur : ");
        String adresse = scanner.nextLine();
        System.out.print("Entrez le téléphone de l'utilisateur : ");
        String telephone = scanner.nextLine();
        System.out.print("L'utilisateur est-il professionnel (true/false) ? ");
        boolean estProfessional = scanner.nextBoolean();
        User user = new User(0, nom, adresse, telephone, estProfessional);
        userService.addUser(user);
        System.out.println("Utilisateur ajouté avec succès !");
    }

    private static void afficherMenuPrincipal(Scanner scanner) {
        System.out.println("\n=============================");
        System.out.println("=== Menu CRUD Utilisateur ===");
        System.out.println("=============================");
        System.out.println("1. Ajouter un utilisateur");
        System.out.println("2. Voir un utilisateur");
        System.out.println("3. Mettre à jour un utilisateur");
        System.out.println("4. Supprimer un utilisateur");
        System.out.println("5. Lister tous les utilisateurs");
        System.out.println("0. Quitter");
        System.out.println("=============================");
        System.out.print("Entrez votre choix : ");
    }

    // Méthode pour voir un utilisateur
    private static void voirUtilisateur() {
        System.out.println("\n--- Voir un utilisateur ---");
        System.out.print("Entrez le nom de l'utilisateur à voir : ");
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
    }

    // Méthode pour mettre à jour un utilisateur
    private static void mettreAJourUtilisateur() {
        System.out.println("\n--- Mettre à jour un utilisateur ---");
        System.out.print("Entrez l'ID de l'utilisateur à mettre à jour : ");
        int updateId = scanner.nextInt();
        scanner.nextLine();
        User updateUser = userService.getUserById(updateId);
        if (updateUser != null) {
            System.out.print("Entrez le nouveau nom : ");
            updateUser.setNom(scanner.nextLine());
            System.out.print("Entrez la nouvelle adresse : ");
            updateUser.setAddress(scanner.nextLine());
            System.out.print("Entrez le nouveau téléphone : ");
            updateUser.setTelephone(scanner.nextLine());
            System.out.print("L'utilisateur est-il professionnel (true/false) ? ");
            updateUser.setEstProfessional(scanner.nextBoolean());
            userService.updateUser(updateUser);
            System.out.println("Utilisateur mis à jour avec succès !");
        } else {
            System.out.println("Utilisateur non trouvé !");
        }
    }

    // Méthode pour supprimer un utilisateur
    private static void supprimerUtilisateur() {
        System.out.println("\n--- Supprimer un utilisateur ---");
        System.out.print("Entrez l'ID de l'utilisateur à supprimer : ");
        int deleteId = scanner.nextInt();
        userService.deleteUser(deleteId);
        System.out.println("Utilisateur supprimé avec succès !");
    }

    // Méthode pour lister tous les utilisateurs
    private static void listerTousLesUtilisateurs() {
        System.out.println("\n--- Liste des utilisateurs ---");
        List<User> utilisateurs = userService.getAllUsers();
        if (utilisateurs.isEmpty()) {
            System.out.println("Aucun utilisateur trouvé.");
        } else {
            System.out.printf("%-5s %-20s %-20s %-15s %-15s%n", "ID", "Nom", "Adresse", "Téléphone", "Professionnel");
            System.out.println("-------------------------------------------------------------------------------");
            for (User u : utilisateurs) {
                System.out.printf("%-5d %-20s %-20s %-15s %-15s%n",
                        u.getId(), u.getNom(), u.getAddress(), u.getTelephone(), u.isEstProfessional() ? "Oui" : "Non");
            }
        }
    }
}
