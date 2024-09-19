package Ui;

import Entity.User;
import Repository.ProjectRepository;
import Repository.UserRepository;
import Service.ProjectService;
import Service.UserService;
import config.DatabaseConnection;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class CrudUserMenu {
    public static void main(String[] args) {

        Connection connection = DatabaseConnection.getConnection();

        UserRepository userRepository = new UserRepository(connection);
        ProjectRepository projectRepository = new ProjectRepository(connection);
        UserService userService = new UserService(userRepository);
        ProjectService projectService = new ProjectService(projectRepository);

        Scanner scanner = new Scanner(System.in);
        int choice = -1;

        while (choice != 0) {
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
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("\n--- Ajouter un utilisateur ---");
                    System.out.print("Entrez le nom de l'utilisateur : ");
                    String nom = scanner.nextLine();
                    System.out.print("Entrez l'adresse de l'utilisateur : ");
                    String address = scanner.nextLine();
                    System.out.print("Entrez le téléphone de l'utilisateur : ");
                    String telephone = scanner.nextLine();
                    System.out.print("L'utilisateur est-il professionnel (true/false) ? ");
                    boolean estProfessional = scanner.nextBoolean();
                    User user = new User(0, nom, address, telephone, estProfessional);
                    userService.addUser(user);
                    System.out.println("Utilisateur ajouté avec succès !");
                    break;
                case 2:
                    System.out.println("\n--- Voir un utilisateur ---");
                    System.out.print("Entrez le nom de l'utilisateur à voir : ");
                    String nomUtilisateur = scanner.nextLine();
                    User retrievedUserByName = userService.getUserByName(nomUtilisateur);
                    if (retrievedUserByName != null) {
                        System.out.println("Détails de l'utilisateur : ");
                        System.out.printf("%-15s : %s%n", "ID", retrievedUserByName.getId());
                        System.out.printf("%-15s : %s%n", "Nom", retrievedUserByName.getNom());
                        System.out.printf("%-15s : %s%n", "Adresse", retrievedUserByName.getAddress());
                        System.out.printf("%-15s : %s%n", "Téléphone", retrievedUserByName.getTelephone());
                        System.out.printf("%-15s : %s%n", "Professionnel", retrievedUserByName.isEstProfessional() ? "Oui" : "Non");
                    } else {
                        System.out.println("Utilisateur non trouvé !");
                    }
                    break;

                case 3:
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
                    break;
                case 4:
                    System.out.println("\n--- Supprimer un utilisateur ---");
                    System.out.print("Entrez l'ID de l'utilisateur à supprimer : ");
                    int deleteId = scanner.nextInt();
                    userService.deleteUser(deleteId);
                    System.out.println("Utilisateur supprimé avec succès !");
                    break;
                case 5:
                    System.out.println("\n--- Liste des utilisateurs ---");
                    List<User> users = userService.getAllUsers();
                    if (users.isEmpty()) {
                        System.out.println("Aucun utilisateur trouvé.");
                    } else {
                        // Affichage des en-têtes de colonnes
                        System.out.printf("%-5s %-20s %-20s %-15s %-15s%n", "ID", "Nom", "Adresse", "Téléphone", "Professionnel");
                        System.out.println("-------------------------------------------------------------------------------");
                        for (User u : users) {
                            System.out.printf("%-5d %-20s %-20s %-15s %-15s%n",
                                    u.getId(),
                                    u.getNom(),
                                    u.getAddress(),
                                    u.getTelephone(),
                                    u.isEstProfessional() ? "Oui" : "Non"
                            );
                        }
                    }
                    break;

                case 0:
                    System.out.println("Fermeture de l'application...");
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
}
