import Repository.ProjectRepository;
import Repository.UserRepository;
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

        UserRepository userRepository = new UserRepository(DatabaseConnection.getConnection());
        ProjectRepository projectRepository = new ProjectRepository(DatabaseConnection.getConnection(), userRepository);

        UserService userService = new UserService(userRepository);
        ProjectService projectService = new ProjectService(projectRepository);

        CrudProjectMenu projectMenu = new CrudProjectMenu(userService, projectService, scanner);
        projectMenu.afficherMenu();
    }
}