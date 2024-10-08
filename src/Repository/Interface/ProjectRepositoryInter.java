package Repository.Interface;

import Entity.Labor;
import Entity.Material;
import Entity.Project;

import java.sql.ResultSet;
import java.util.List;

public interface ProjectRepositoryInter {
    Project createProject(Project project);
    Project getProjectById(int id);
    List<Project> getAllProjects();
    void updateProject(Project project);
    void deleteProject(int id);
    List<Project> getProjectsByUserId(int userId);
    void updateMarginAndTotalCost_Project(int id, double marginProfit, double totalCost);
    boolean updateStatus(int id, String status);

}
