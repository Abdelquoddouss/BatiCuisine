import Entity.Devi;
import Entity.Project;
import Entity.enums.EtatProject;
import Repository.*;
import Repository.Interface.LaborRepositoryInter;
import Repository.Interface.MaterialRepsitoryInter;
import Repository.Interface.ProjectRepositoryInter;
import Repository.Interface.UserRepositoryInter;
import Service.*;
import Service.Interface.LaborServiceInter;
import Service.Interface.UserServiceInter;
import Ui.CrudProjectMenu;
import config.DatabaseConnection;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.stream.Collectors;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Initialisation des repositories
        UserRepositoryInter userRepository = new UserRepository(DatabaseConnection.getConnection());
        ProjectRepositoryInter projectRepository = new ProjectRepository(DatabaseConnection.getConnection(), userRepository);
        MaterialRepsitoryInter materialRepository = new MaterialRepository(DatabaseConnection.getConnection());
        LaborRepositoryInter laborRepository = new LaborRepository(DatabaseConnection.getConnection());
        DeviRepository deviRepository = new DeviRepository(DatabaseConnection.getConnection());
        // Initialisation des services
        UserServiceInter userService = new UserService(userRepository);
        ProjectService projectService = new ProjectService(projectRepository);
        MaterialService materialService = new MaterialService(materialRepository);
        LaborServiceInter laborService = new LaborService(laborRepository);
        DeviService deviService = new DeviService(deviRepository);


        // Création du menu avec tous les services
        CrudProjectMenu projectMenu = new CrudProjectMenu(userService, projectService, materialService, laborService, scanner, deviService);
        projectMenu.afficherMenu();




    }
}