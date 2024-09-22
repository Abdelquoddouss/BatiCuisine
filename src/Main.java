import Repository.Interface.ProjectRepositoryInter;
import Repository.Interface.UserRepositoryInter;
import Repository.MaterialRepository;
import Repository.ProjectRepository;
import Repository.UserRepository;
import Service.Interface.UserServiceInter;
import Service.MaterialService;
import Service.ProjectService;
import Service.UserService;
import Ui.CrudProjectMenu;
import config.DatabaseConnection;

import java.sql.Connection;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Initialisation des repositories
        UserRepositoryInter userRepository = new UserRepository(DatabaseConnection.getConnection());
        ProjectRepositoryInter projectRepository = new ProjectRepository(DatabaseConnection.getConnection(), userRepository);
        MaterialRepository materialRepository = new MaterialRepository(DatabaseConnection.getConnection());

        // Initialisation des services
        UserServiceInter userService = new UserService(userRepository);
        ProjectService projectService = new ProjectService(projectRepository);
        MaterialService materialService = new MaterialService(materialRepository);

        // Création du menu avec tous les services
        CrudProjectMenu projectMenu = new CrudProjectMenu(userService, projectService, materialService, scanner);
        projectMenu.afficherMenu();
    }
}